#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>
#import <React/RCTUIManagerUtils.h>
#import <React/RCTShadowView.h>
#import "RCTBridge.h"
#import "KeyboardMovingView.h"
#import "RCTUIManager+Private.h"

@interface KeyboardMovingViewViewManager : RCTViewManager<UpdateViewDelegate>
@end

@implementation KeyboardMovingViewViewManager{
  KeyboardMovingView *_keyboardMovingView;
}

RCT_EXPORT_MODULE(KeyboardMovingViewView)

RCT_EXPORT_VIEW_PROPERTY(behavior, NSString)
RCT_EXPORT_VIEW_PROPERTY(extraHeight, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(onKeyboardWillShow, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onKeyboardWillHide, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onKeyboardDidShow, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onKeyboardDidHide, RCTBubblingEventBlock)

- (UIView *)view {
   
    KeyboardMovingView *keyboardMovingView = [KeyboardMovingView new];
    keyboardMovingView.delegate = self;
    _keyboardMovingView = keyboardMovingView;
     return keyboardMovingView;
    
    return keyboardMovingView;
}

- (void)updateProps:(NSDictionary *)props
{

    RCTExecuteOnUIManagerQueue(^(){
        [self.bridge.uiManager configureNextLayoutAnimation:@{@"update":@{@"type": @"keyboard"}} withCallback:nil errorCallback:nil];
        RCTShadowView *shadowView  =  [self.bridge.uiManager  shadowViewForReactTag:self->_keyboardMovingView.reactTag];
        [self.bridge.uiManager updateView:self->_keyboardMovingView.reactTag viewName:shadowView.viewName props:props];
        [self.bridge.uiManager  batchDidComplete];
    });
}


@end
