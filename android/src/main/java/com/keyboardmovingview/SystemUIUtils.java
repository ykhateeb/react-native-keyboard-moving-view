package com.keyboardmovingview;

import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowMetrics;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.scroll.ReactScrollView;

import java.util.Objects;

public class SystemUIUtils {

  public static int getScreenHeight(ThemedReactContext context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      WindowMetrics windowMetrics = Objects.requireNonNull(context.getCurrentActivity()).getWindowManager().getCurrentWindowMetrics();
      return windowMetrics.getBounds().height();
    } else{
      DisplayMetrics displayMetrics = new DisplayMetrics();
      Objects.requireNonNull(context.getCurrentActivity()).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
      return displayMetrics.heightPixels;
    }
  }

  public static boolean isStatusBarsVisible(View view) {
    WindowInsetsCompat insetsCompat = ViewCompat.getRootWindowInsets(view);
    assert insetsCompat != null;
    return insetsCompat.isVisible(WindowInsetsCompat.Type.statusBars());
  }

  public static boolean isNavigationBarsVisible(View view) {
    WindowInsetsCompat insetsCompat = ViewCompat.getRootWindowInsets(view);
    assert insetsCompat != null;
    return insetsCompat.isVisible(WindowInsetsCompat.Type.navigationBars());
  }

  public static boolean isIMEVisible(View view) {
    WindowInsetsCompat insetsCompat = ViewCompat.getRootWindowInsets(view);
    assert insetsCompat != null;
    return insetsCompat.isVisible(WindowInsetsCompat.Type.ime());
  }

  public static int getIMEHeight(View view) {
    WindowInsetsCompat insetsCompat = ViewCompat.getRootWindowInsets(view);
    assert insetsCompat != null;
    return insetsCompat.getInsets(WindowInsetsCompat.Type.ime()).bottom;
  }
  public static boolean hasWindowInsets(View view) {
    WindowInsetsCompat insetsCompat = ViewCompat.getRootWindowInsets(view);
    return insetsCompat != null;
  }

  public static int getStatusBarsHeight(View view) {
    WindowInsetsCompat insetsCompat = ViewCompat.getRootWindowInsets(view);
    assert insetsCompat != null;
    Insets statusBarsInsets = insetsCompat.getInsets(WindowInsetsCompat.Type.statusBars());
    return statusBarsInsets.top;
  }

  public static int getNavigationBarsHeight(View view) {
    WindowInsetsCompat insetsCompat = ViewCompat.getRootWindowInsets(view);
    assert insetsCompat != null;
    Insets navigationBarsInsets = insetsCompat.getInsets(WindowInsetsCompat.Type.navigationBars());
    return navigationBarsInsets.bottom;
  }
  public static View findFocusView(View view) {
    return view.findFocus();
  }

  public static int getViewTopBorderYPosition(View view) {
    int[] viewLocation = new int[2];
    view.getLocationOnScreen(viewLocation);

    return viewLocation[1];
  }

  public static int getViewBottomBorderYPosition(View view) {
    return getViewTopBorderYPosition(view) + view.getHeight();
  }

  public static int getFocusViewYPosition(View view) {
    View focusView = findFocusView(view);
    if(focusView == null) {
      return 0;
    }

    return getViewBottomBorderYPosition(focusView);
  }

  public static ReactScrollView findClosestScrollView(View view) {
    ViewParent viewParent = view.getParent();
    if (viewParent instanceof ReactScrollView) {
      return (ReactScrollView) viewParent;
    }

    if (viewParent instanceof View) {
      return findClosestScrollView((View) viewParent);
    }

    return null;
  }

  public static void scrollScrollViewToEnd(ReactScrollView scrollView) {
    if (scrollView != null) {
      View child = scrollView.getChildAt(0);
      int bottom = child.getHeight() + scrollView.getPaddingBottom();
      scrollView.reactSmoothScrollTo(scrollView.getScrollX(), bottom);
      scrollView.requestLayout();
    }
  }
}
