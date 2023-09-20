package com.keyboardmovingview;

import android.graphics.Color;

import androidx.annotation.Nullable;

import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

@ReactModule(name = KeyboardMovingViewViewManager.NAME)
public class KeyboardMovingViewViewManager extends KeyboardMovingViewViewManagerSpec<KeyboardMovingViewView> {

  public static final String NAME = "KeyboardMovingViewView";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public KeyboardMovingViewView createViewInstance(ThemedReactContext context) {
    return new KeyboardMovingViewView(context);
  }

  @Override
  @ReactProp(name = "color")
  public void setColor(KeyboardMovingViewView view, @Nullable String color) {
    view.setBackgroundColor(Color.parseColor(color));
  }
}
