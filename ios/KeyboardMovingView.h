//
//  Header.h
//  react-native-keyboard-moving-view
//
//  Created by yazan on 21/09/2023.
//

#ifndef KeyboardMovingView_h
#define KeyboardMovingView_h

#import <React/RCTView.h>
#import "UpdateViewDelegate.h"

@interface KeyboardMovingView : RCTView

@property (nonatomic, weak) id<UpdateViewDelegate> delegate;
@property(nonatomic, copy) NSString *behavior;
@property(nonatomic, copy) NSNumber *extraHeight;
@property (nonatomic, copy) RCTBubblingEventBlock onKeyboardWillShow;
@property (nonatomic, copy) RCTBubblingEventBlock onKeyboardWillHide;
@property (nonatomic, copy) RCTBubblingEventBlock onKeyboardDidShow;
@property (nonatomic, copy) RCTBubblingEventBlock onKeyboardDidHide;

@end

#endif /* Header_h */
