package com.keyboardmovingview;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.core.view.WindowCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.Spacing;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.ReactEventEmitter;
import com.facebook.react.views.view.ReactViewBackgroundDrawable;
import com.facebook.react.views.view.ReactViewGroup;

import java.util.ArrayList;
import java.util.Objects;

public class KeyboardMovingViewView extends ReactViewGroup {
  private final ThemedReactContext mContext;
  private final UIManagerModule mUIManagerModule;
  private String mBehavior;
  private float mExtraHeight = 0;
  private final int mDefaultSoftInputMode;
  private final RootViewDeferringInsetsHandler mRootViewDeferringInsetsHandler;
  private final ReactEventEmitter mEventEmitter ;
  private final int identityHashCode = System.identityHashCode(this);
  //to track life cycle events of attached and detached
  private static final ArrayList<String> lifeCycleEvents = new ArrayList<>();

  public KeyboardMovingViewView(Context context) {
    super(context);

    mContext = (ThemedReactContext) context;
    mDefaultSoftInputMode = getDefaultSoftInputMode();
    mUIManagerModule = mContext.getNativeModule(UIManagerModule.class);
    mRootViewDeferringInsetsHandler = new RootViewDeferringInsetsHandler(mContext, this);
    mEventEmitter =  new ReactEventEmitter(mContext.getReactApplicationContext());

    //lifeCycleEvents.add(identityHashCode + "-ATTACHED");
    //setEdgeToEdge(true);

  }

  public void onKeyboardWillShowEvent() {
    mEventEmitter.receiveEvent(getId(), "topKeyboardWillShow", null);
  }

  public void onKeyboardWillHideEvent() {
    mEventEmitter.receiveEvent(getId(), "topKeyboardWillHide", null);
  }

  public void onKeyboardDidShowEvent() {
    mEventEmitter.receiveEvent(getId(), "topKeyboardDidShow", null);
  }

  public void onKeyboardDidHideEvent() {
    mEventEmitter.receiveEvent(getId(), "topKeyboardDidHide", null);
  }

  public float getBorderBottomWidth(){
    ReactViewBackgroundDrawable background = (ReactViewBackgroundDrawable)getBackground();

    if(background == null){
      return 0;
    }

    return background.getFullBorderWidth() != 0 ? background.getFullBorderWidth() : background.getBorderWidthOrDefaultTo(0, Spacing.BOTTOM);
  }

  private int getDefaultSoftInputMode() {
    int softInputMode = Objects.requireNonNull(mContext.getCurrentActivity()).getWindow().getAttributes().softInputMode;
    return softInputMode == 0 ? WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED : softInputMode;
  }

  public void updateProps(ReadableMap props) {
    mContext.runOnNativeModulesQueueThread(() -> {
      assert mUIManagerModule != null;
      mUIManagerModule.updateView(getId(), KeyboardMovingViewViewManager.NAME, props);
      mUIManagerModule.onBatchComplete();
    });
  }

  public void setBehavior(String behavior) {
    mBehavior = behavior;
  }

  public String getBehavior() {
    return mBehavior;
  }

  public void setExtraHeight(float extraHeight) {
    mExtraHeight = PixelUtil.toPixelFromDIP(extraHeight);
  }

  public float getExtraHeight() {
    return mExtraHeight;
  }

  public void setContentViewInsets(int top, int bottom) {
    View contentView = getRootView().findViewById(androidx.appcompat.R.id.action_bar_root);
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
      FrameLayout.LayoutParams.MATCH_PARENT,
      FrameLayout.LayoutParams.MATCH_PARENT
    );
    params.setMargins(0, top, 0, bottom);
    contentView.setLayoutParams(params);
  }

  private boolean shouldEnableEdgeToEdge() {
    return lifeCycleEvents.size() == 0;
  }

  private boolean shouldDisableEdgeToEdge() {
    //if completed the life cycle events(attached and detached)
    // EX: assume we have 2 screens only first one contains KeyboardMovingView
    // lifeCycleEvents: Screen1-ATTACHED, Screen1-DETACHED
    //EX2: 3 Screens only first two have KeyboardMovingView
    // lifeCycleEvents: Screen1-ATTACHED, Screen2-ATTACHED, Screen1-DETACHED, Screen2-DETACHED
    return lifeCycleEvents.size() % 2 == 0;
  }

  private void setEdgeToEdge(boolean edgeToEdge) {
    try {
      WindowCompat.setDecorFitsSystemWindows(Objects.requireNonNull(mContext.getCurrentActivity()).getWindow(), !edgeToEdge);
      Objects.requireNonNull(mContext.getCurrentActivity()).getWindow().setSoftInputMode(
        edgeToEdge ? WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE : mDefaultSoftInputMode
      );

      if (!edgeToEdge) {
        setContentViewInsets(0, 0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (shouldEnableEdgeToEdge()) {
      setEdgeToEdge(true);
    }

    lifeCycleEvents.add(identityHashCode + "-ATTACHED");
    mRootViewDeferringInsetsHandler.enable();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    lifeCycleEvents.add(identityHashCode + "-DETACHED");
    if (shouldDisableEdgeToEdge()) {
      setEdgeToEdge(false);
      lifeCycleEvents.clear();
    }

    mRootViewDeferringInsetsHandler.disable();
  }
}


