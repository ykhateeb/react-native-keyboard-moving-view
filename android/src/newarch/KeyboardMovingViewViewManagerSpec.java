package com.keyboardmovingview;

import android.view.View;

import androidx.annotation.Nullable;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.viewmanagers.KeyboardMovingViewViewManagerDelegate;
import com.facebook.react.viewmanagers.KeyboardMovingViewViewManagerInterface;
import com.facebook.soloader.SoLoader;

public abstract class KeyboardMovingViewViewManagerSpec<T extends View> extends SimpleViewManager<T> implements KeyboardMovingViewViewManagerInterface<T> {
  static {
    if (BuildConfig.CODEGEN_MODULE_REGISTRATION != null) {
      SoLoader.loadLibrary(BuildConfig.CODEGEN_MODULE_REGISTRATION);
    }
  }

  private final ViewManagerDelegate<T> mDelegate;

  public KeyboardMovingViewViewManagerSpec() {
    mDelegate = new KeyboardMovingViewViewManagerDelegate(this);
  }

  @Nullable
  @Override
  protected ViewManagerDelegate<T> getDelegate() {
    return mDelegate;
  }
}
