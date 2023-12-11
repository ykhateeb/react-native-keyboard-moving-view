package com.keyboardmovingview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.HashMap;
import java.util.Map;

@ReactModule(name = KeyboardMovingViewViewManager.NAME)
public class KeyboardMovingViewViewManager extends com.keyboardmovingview.KeyboardMovingViewViewManagerSpec<KeyboardMovingViewView> {

  public static final String NAME = "KeyboardMovingViewView";


  @NonNull
  @Override
  public String getName() {
    return NAME;
  }

  @NonNull
  @Override
  public KeyboardMovingViewView createViewInstance(ThemedReactContext context) {
    return new KeyboardMovingViewView(context);
  }


  @Nullable
  @Override
  public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
    Map<String, Object> baseEventTypeConstants =
      super.getExportedCustomBubblingEventTypeConstants();
    Map<String, Object> eventTypeConstants =
      baseEventTypeConstants == null ? new HashMap<>() : baseEventTypeConstants;

    eventTypeConstants.putAll(MapBuilder.<String, Object>builder()
      .put("topKeyboardWillShow", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onKeyboardWillShow")))
      .put("topKeyboardWillHide", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onKeyboardWillHide")))
      .put("topKeyboardDidShow", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onKeyboardDidShow")))
      .put("topKeyboardDidHide", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onKeyboardDidHide")))
      .build());

     return eventTypeConstants;
  }

  @Override
  @ReactProp(name = "behavior")
  public void setBehavior(KeyboardMovingViewView view, String behavior) {
    view.setBehavior(behavior);
  }

  @Override
  @ReactProp(name = "extraHeight")
  public void setExtraHeight(KeyboardMovingViewView view, float extraHeight) {
    view.setExtraHeight(extraHeight);
  }
}
