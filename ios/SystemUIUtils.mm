//
//  SystemUIUtils.m
//  react-native-keyboard-moving-view
//
//  Created by yazan on 21/09/2023.
//

#import "SystemUIUtils.h"
#import <React/RCTBaseTextInputView.h>
#import <React/RCTView.h>

@implementation SystemUIUtils

+ (CGFloat)screenHeight{
    //TODO: remove comments after all is good with testing
    //    CGRect screenRect = [[UIScreen mainScreen] bounds];
    //    return  screenRect.size.height;
    return CGRectGetHeight([[UIScreen mainScreen] bounds]);
}

+ (RCTUITextField *)findFocusView:(UIView *)view {
    if ([view isFirstResponder]) {
        return (RCTUITextField *)view;
    }
    
    for (UIView *child in view.subviews) {
        UIView *focus = [self.class findFocusView:child];
        if (focus) {
            return (RCTUITextField *)focus;
        }
    }
    
    return nil;
}

// View position from bottom border
+ (CGFloat)viewYPosition:(RCTView *)view{
  CGPoint viewPointInWindow = [view.superview convertPoint:view.frame.origin toView:nil];

    CGFloat borderBottomWidth = 0;
    
    if([view isKindOfClass:[RCTUITextField class]]){
        RCTBaseTextInputView *parentView = (RCTBaseTextInputView *)view.superview;
        borderBottomWidth = parentView.reactBorderInsets.bottom;
    }
    
    return viewPointInWindow.y + view.frame.size.height + borderBottomWidth;
}

+ (CGFloat)focusViewYPosition:(UIView *)view{
    UIView *focusView = [self.class findFocusView:view];
    CGFloat viewYPosition = [self.class viewYPosition:focusView];
    
    return viewYPosition;
}

+ (RCTScrollView *)findClosetScrollView:(UIView *)view {
    if ([view isKindOfClass:[RCTScrollView class]]) {
        return (RCTScrollView *)view;
    }
    
    if (view.superview) {
        return [self.class findClosetScrollView:view.superview];
    }
    
    return nil;
}

+ (id)alloc {
    [NSException raise:@"Cannot be instantiated!" format:@"Static class 'SystemUIUtils' cannot be instantiated!"];
    return nil;
}

@end
