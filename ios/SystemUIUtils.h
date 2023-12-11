//
//  SystemUIUtils.h
//  react-native-keyboard-moving-view
//
//  Created by yazan on 21/09/2023.
//

#ifndef SystemUIUtils_h
#define SystemUIUtils_h

#import <React/RCTScrollView.h>
#import <React/RCTUITextField.h>

@interface SystemUIUtils : NSObject

+ (CGFloat)screenHeight;
+ (RCTUITextField *)findFocusView:(UIView *)view;
+ (CGFloat)viewYPosition:(UIView *)view;
+ (CGFloat)focusViewYPosition:(UIView *)view;
+ (RCTScrollView *)findClosetScrollView:(UIView *)view;

@end

#endif /* SystemUIUtils_h */
