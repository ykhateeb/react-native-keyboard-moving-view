package com.keyboardmovingview;

import androidx.annotation.Nullable;

import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;

public abstract class KeyboardMovingViewViewManagerSpec<T extends ReactViewGroup> extends ReactViewManager  {
   public abstract void setBehavior(T view, String behavior);
  public abstract void setExtraHeight(T view, float extraHeight);
}
