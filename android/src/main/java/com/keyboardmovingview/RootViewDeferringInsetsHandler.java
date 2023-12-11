package com.keyboardmovingview;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.scroll.ReactScrollView;

import java.util.List;
import java.util.Objects;

public class RootViewDeferringInsetsHandler extends WindowInsetsAnimationCompat.Callback implements OnApplyWindowInsetsListener, ViewTreeObserver.OnGlobalFocusChangeListener {
  private final ThemedReactContext mContext;
  private final KeyboardMovingViewView mView;
  //the height from focus View to bottom of screen
  private float mFocusViewBottomHeight = 0;
  //the height from bottom View to bottom of screen
  private float mBottomHeight = 0;
  private int mLastIMEHeight = 0;
  private boolean mIsIMEDidShow = false;

  public RootViewDeferringInsetsHandler(ThemedReactContext context, KeyboardMovingViewView view) {
    super(WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP);
    mContext = context;
    mView = view;
  }

  public void enable() {
    ViewCompat.setOnApplyWindowInsetsListener(mView, this);
    ViewCompat.setWindowInsetsAnimationCallback(mView, this);
    Objects.requireNonNull(mContext.getCurrentActivity()).getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener(this);
  }

  public void disable() {
    ViewCompat.setOnApplyWindowInsetsListener(mView, null);
    ViewCompat.setWindowInsetsAnimationCallback(mView, null);
    Objects.requireNonNull(mContext.getCurrentActivity()).getWindow().getDecorView().getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
  }

  @Override
  public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
    Insets systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
    int imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
    boolean isIMEVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
    String behavior = mView.getBehavior();

    mView.setContentViewInsets(systemInsets.top, systemInsets.bottom);

    //handle change of IME height
    if (isIMEVisible) {
      boolean isIMEHeightChanged = mLastIMEHeight != 0 && mLastIMEHeight != imeHeight;
      if (isIMEHeightChanged) {
        float extraHeight = mView.getExtraHeight();

        if (behavior.equals("padding")) {
          mView.updateProps(JavaOnlyMap.of("paddingBottom", PixelUtil.toDIPFromPixel((imeHeight - mBottomHeight) + extraHeight)));
        }

        if (behavior.equals("position")) {
          float translationY = (imeHeight - mFocusViewBottomHeight) + extraHeight;
          mView.setTranslationY(-Math.max(translationY, 0));
        }
      }
      // remember the last value of the IME height
      mLastIMEHeight = imeHeight;
    } else {
      mLastIMEHeight = 0;
    }

    return WindowInsetsCompat.CONSUMED;
  }

  @NonNull
  @Override
  public WindowInsetsCompat onProgress(@NonNull WindowInsetsCompat insets, @NonNull List<WindowInsetsAnimationCompat> runningAnimations) {

    WindowInsetsAnimationCompat imeAnimation = null;
    for (WindowInsetsAnimationCompat animation : runningAnimations) {
      if ((animation.getTypeMask() & WindowInsetsCompat.Type.ime()) != 0) {
        imeAnimation = animation;
        break;
      }
    }

    int currentIMEHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
    boolean isIMEVisible = SystemUIUtils.isIMEVisible(mView);
    String behavior = mView.getBehavior();
    float extraHeight = mView.getExtraHeight();

     assert imeAnimation != null;
    float interpolatedFraction = isIMEVisible ? imeAnimation.getInterpolatedFraction() : 1 - imeAnimation.getInterpolatedFraction();


    if (behavior.equals("padding")) {
      mView.updateProps(JavaOnlyMap.of("paddingBottom", PixelUtil.toDIPFromPixel((currentIMEHeight + (extraHeight * interpolatedFraction)) - (mBottomHeight * interpolatedFraction))));
    }

    if (behavior.equals("position")) {
      float translationY = (currentIMEHeight - (mFocusViewBottomHeight * interpolatedFraction)) + (extraHeight * interpolatedFraction);
      mView.setTranslationY(-Math.max(translationY, 0));
    }

    return insets;
  }

  @NonNull
  @Override
  public WindowInsetsAnimationCompat.BoundsCompat onStart(@NonNull WindowInsetsAnimationCompat animation, @NonNull WindowInsetsAnimationCompat.BoundsCompat bounds) {
    String behavior = mView.getBehavior();

    boolean isIMEVisible = SystemUIUtils.isIMEVisible(mView);

    if(isIMEVisible){
      mView.onKeyboardWillShowEvent();
    }else{
      mView.onKeyboardWillHideEvent();
    }

    if (behavior.equals("padding")) {
      if (isIMEVisible) {
        float borderBottomWidth = mView.getBorderBottomWidth();
        mBottomHeight = SystemUIUtils.getScreenHeight(mContext) - (SystemUIUtils.getViewBottomBorderYPosition(mView) - borderBottomWidth);
      }
    }

    if (behavior.equals("position")) {
      if (isIMEVisible && shouldHandleTransition()) {
        adjustScrollViewOffsetIfNeeded(SystemUIUtils.findFocusView(mView), false);
        mFocusViewBottomHeight = SystemUIUtils.getScreenHeight(mContext) - SystemUIUtils.getFocusViewYPosition(mView);
      }
    }

    return super.onStart(animation, bounds);
  }

  @Override
  public void onEnd(@NonNull WindowInsetsAnimationCompat animation) {
    super.onEnd(animation);
    boolean isIMEVisible = SystemUIUtils.isIMEVisible(mView);
    if(isIMEVisible){
      mIsIMEDidShow =  true ;
      mView.onKeyboardDidShowEvent();
    }else{
      mIsIMEDidShow =  false ;
      mView.onKeyboardDidHideEvent();
    }

  }

  private boolean shouldHandleTransition() {
    int focusViewYPosition = SystemUIUtils.getFocusViewYPosition(mView);
    int screenHeight = SystemUIUtils.getScreenHeight(mContext);
    int imeHeight = SystemUIUtils.getIMEHeight(mView);

    int bottomSpace = screenHeight - focusViewYPosition;

    return bottomSpace < imeHeight;
  }

  private void adjustScrollViewOffsetIfNeeded(View focusView, boolean isAnimationEnabled) {
    ReactScrollView scrollView = SystemUIUtils.findClosestScrollView(focusView);

    if (scrollView != null) {
      Rect offset = new Rect();
      focusView.getDrawingRect(offset);
      scrollView.offsetDescendantRectToMyCoords(focusView, offset);
      float scrollY = scrollView.getScrollY();

      float dy;
      if (mIsIMEDidShow) {
        float extraHeight = mView.getExtraHeight();
        int focusViewBottomHeight = SystemUIUtils.getScreenHeight(mContext) - SystemUIUtils.getFocusViewYPosition(mView);
        int imeHeight = SystemUIUtils.getIMEHeight(mView);
        float translationYValue = Math.max((imeHeight + extraHeight) - focusViewBottomHeight, 0);
        dy = -translationYValue;
      } else {
        dy = (scrollView.getHeight() + scrollY) - offset.bottom;
      }

      if (dy < 0) {
        if (isAnimationEnabled) {
          scrollView.reactSmoothScrollTo(0, (int) (scrollY - dy));
        } else {
          scrollView.scrollTo(0, (int) (scrollY - dy));
        }
//         scrollView.requestLayout();
      }
    }
  }

  @Override
  public void onGlobalFocusChanged(View oldFocus, View newFocus) {
    if (newFocus instanceof EditText && SystemUIUtils.isIMEVisible(mView)) {
      adjustScrollViewOffsetIfNeeded(newFocus, true);
    }
  }
}
