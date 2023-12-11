//
//  KeyboardMovingView.m
//  react-native-keyboard-moving-view
//
//  Created by yazan on 21/09/2023.
//

#import "KeyboardMovingView.h"
#import "SystemUIUtils.h"
#import <React/RCTUIManager.h>
#import <React/RCTUIManagerUtils.h>
#import <React/RCTScrollView.h>
#import <React/RCTUITextField.h>
#import <React/RCTBaseTextInputView.h>
#import <React/RCTSafeAreaView.h>

typedef NS_ENUM(NSInteger, TranslationDirection) {
    TranslationDirectionTop,
    TranslationDirectionBottom
};

@implementation KeyboardMovingView{
    bool _isKeyboardWillShow;
    bool _isKeyboardDidShow;
    CGFloat _keyboardHeight;
    CGFloat _focusViewBottomHeight;
}

- (void)didMoveToWindow {
    [super didMoveToWindow];
    if (self.window) {
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(keyboardWillShow:)
                                                     name:UIKeyboardWillShowNotification
                                                   object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(keyboardWillHide:)
                                                     name:UIKeyboardWillHideNotification
                                                   object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(keyboardDidShow:)
                                                     name:UIKeyboardDidShowNotification
                                                   object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(keyboardDidHide:)
                                                     name:UIKeyboardDidHideNotification
                                                   object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(keyboardWillChangeFrame:)
                                                     name:UIKeyboardWillChangeFrameNotification
                                                   object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(textFieldDidBeginEditing:)
                                                     name:UITextFieldTextDidBeginEditingNotification
                                                   object:nil];
    }
}

- (void)willMoveToWindow:(UIWindow *)newWindow {
    if (!newWindow) {
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardDidShowNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardDidHideNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillChangeFrameNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidBeginEditingNotification object:nil];
    }
}

- (NSDictionary *)extractNotificationParams:(NSNotification *)notification {
    // Extract the duration of the keyboard animation
    double duration = [notification.userInfo[UIKeyboardAnimationDurationUserInfoKey] doubleValue];
    // Extract the final frame of the keyboard
    CGRect keyboardFrame  = [notification.userInfo[UIKeyboardFrameEndUserInfoKey] CGRectValue];
    // Extract the curve of the iOS keyboard animation
    NSInteger curve =[ notification.userInfo[UIKeyboardAnimationCurveUserInfoKey] integerValue];
    CGRect frameBegin = [notification.userInfo[UIKeyboardFrameBeginUserInfoKey] CGRectValue];
    CGRect frameEnd = [notification.userInfo[UIKeyboardFrameEndUserInfoKey] CGRectValue];
    
    return @{@"duration":@(duration), @"keyboardHeight":[NSNumber numberWithFloat:keyboardFrame.size.height], @"curve": @(curve), @"keyboardOriginYBegin":[NSNumber numberWithFloat:frameBegin.origin.y], @"keyboardOriginYEnd":[NSNumber numberWithFloat:frameEnd.origin.y]};
}

- (void)updatePadding:(CGFloat)keyboardHeight {
    double extraHeight = [self.extraHeight doubleValue];
    CGFloat containerYPosition = [SystemUIUtils viewYPosition:self];
    CGFloat screenHeight = [SystemUIUtils screenHeight];
    CGFloat borderBottomWidth = MAX(MAX(self.borderWidth, self.borderBottomWidth)/** return -1 if there is no border set */, 0);
    CGFloat containerBottomHeight = screenHeight - (containerYPosition - borderBottomWidth);

    NSDictionary *props = @{
        @"paddingBottom":@(keyboardHeight == 0 ?  keyboardHeight : MAX((keyboardHeight - containerBottomHeight), 0) + extraHeight)
    };
    
    [self.delegate updateProps:props];
}

- (void)transalteYPosition:(TranslationDirection)direction keyboardHeight:(NSInteger)keyboardHeight duration:(double)duration curve:(NSInteger)curve iskeyboardFrameChanged:(bool)iskeyboardFrameChanged{
    double extraHeight = [self.extraHeight doubleValue];
    //height between view and bottom of screen
    CGFloat diffHeight = MAX((keyboardHeight - _focusViewBottomHeight), 0);
    
    //if keyboard height changed just translate to top by keyboard height
    CGFloat translationValue = (iskeyboardFrameChanged ? (keyboardHeight - _focusViewBottomHeight) : diffHeight) +  extraHeight;
    
    self.transform = CGAffineTransformMakeTranslation(0, (direction == TranslationDirectionTop ? -translationValue : 0));
}

- (bool)shouldHandleTransition:(CGFloat)keyboardHeight{
    CGFloat screenHeight = [SystemUIUtils screenHeight];
    CGFloat focusViewYPosition = [SystemUIUtils focusViewYPosition:self];
    CGFloat bottomHeight = screenHeight - focusViewYPosition;/** height between View and bottom of screen */
    return bottomHeight < keyboardHeight;
}

- (void)keyboardWillShow:(NSNotification *)notification {
    
    if(_isKeyboardWillShow){
        return;
    }
    
    NSDictionary *params = [self extractNotificationParams:notification];
    
    double duration = [params[@"duration"] doubleValue];
    NSInteger curve = [params[@"curve"] integerValue];
    CGFloat keyboardHeight = [params[@"keyboardHeight"] floatValue];
    
    _keyboardHeight = keyboardHeight;
    
    if([self.behavior isEqualToString:@"padding"]){
        [self updatePadding:keyboardHeight];
    }
    
    if([self.behavior isEqualToString:@"position"]){
        if(![self shouldHandleTransition:keyboardHeight]){
            return;
        }
        
        [self adjustScrollViewOffsetIfNeeded:[SystemUIUtils findFocusView:self] isAnimationEnabled:NO];
        
        CGFloat screenHeight = [SystemUIUtils screenHeight];
        CGFloat focusViewYPosition = [SystemUIUtils focusViewYPosition:self];
        _focusViewBottomHeight = MAX(screenHeight - focusViewYPosition, 0);
        
        [self transalteYPosition:TranslationDirectionTop keyboardHeight:keyboardHeight duration:duration curve:curve iskeyboardFrameChanged:NO];
    }
    
    if(self.onKeyboardWillShow){
        self.onKeyboardWillShow(nil);
    }

    _isKeyboardWillShow  = YES;
}


- (void)keyboardWillHide:(NSNotification *)notification {
    NSDictionary *params = [self extractNotificationParams:notification];
    double duration = [params[@"duration"] doubleValue];
    NSInteger curve = [params[@"curve"] integerValue];
    CGFloat keyboardHeight = [params[@"keyboardHeight"] floatValue];
    
    if([self.behavior isEqualToString:@"padding"]){
        [self updatePadding:0];
    }
    
    if([self.behavior isEqualToString:@"position"]){
        [self transalteYPosition:TranslationDirectionBottom keyboardHeight:keyboardHeight  duration:duration curve:curve iskeyboardFrameChanged:NO];
    }
    
    if(self.onKeyboardWillHide){
        self.onKeyboardWillHide(nil);
    }

    _isKeyboardWillShow  = NO;
}


- (void)keyboardWillChangeFrame:(NSNotification *)notification {
    NSDictionary *params = [self extractNotificationParams:notification];
    CGFloat keyboardOriginYBegin = [params[@"keyboardOriginYBegin"]  floatValue];
    CGFloat keyboardOriginYEnd = [params[@"keyboardOriginYEnd"]  floatValue];
    CGFloat keyboardHeight = [params[@"keyboardHeight"] floatValue];
    double duration = [params[@"duration"] doubleValue];
    NSInteger curve = [params[@"curve"] integerValue];
    
    CGFloat screenHeight = [SystemUIUtils screenHeight];
    
    bool isWillShow = keyboardOriginYBegin == screenHeight;
    bool isFrameChanged = keyboardOriginYBegin != keyboardOriginYEnd;
    bool isWillHide = keyboardOriginYEnd == screenHeight;
    
    if(!isWillShow && isFrameChanged && !isWillHide){
        
        if([self.behavior isEqualToString:@"padding"]){
            [self updatePadding:keyboardHeight];
        }
        
        if([self.behavior isEqualToString:@"position"]){
            [self transalteYPosition:TranslationDirectionTop keyboardHeight:keyboardHeight  duration:duration curve:curve iskeyboardFrameChanged:YES];
        }
    }
}

- (void)keyboardDidShow:(NSNotification *)notification {
    if(_isKeyboardDidShow){
        return;
    }
    
    if(self.onKeyboardDidShow){
        self.onKeyboardDidShow(nil);
    }
    
    _isKeyboardDidShow = YES;
}

- (void)keyboardDidHide:(NSNotification *)notification {
    if( self.onKeyboardDidHide){
        self.onKeyboardDidHide(nil);
    }
    
    _isKeyboardDidShow = NO;
}

/**
 * For showing the hidden part from TextInput by ScrollView
 */
- (void)adjustScrollViewOffsetIfNeeded:(RCTUITextField *)focusView isAnimationEnabled:(bool)isAnimationEnabled {
    RCTScrollView *rct = [SystemUIUtils findClosetScrollView:focusView];
    
    if (rct) {
        UIScrollView *scrollView = rct.scrollView;
        
        CGFloat dy;
        if(_isKeyboardWillShow){
            CGFloat screenHeight =[SystemUIUtils screenHeight];
            //view position realtive to screen
            CGFloat screenFocusViewYPosition = [SystemUIUtils  viewYPosition:focusView];
            CGFloat focusViewBottomHeight = screenHeight- screenFocusViewYPosition;
            double extraHeight = [self.extraHeight doubleValue];
            CGFloat translationYValue = MAX((_keyboardHeight + extraHeight) - focusViewBottomHeight, 0);
            dy = -translationYValue;
        }else{
            CGRect frame = [rct.contentView convertRect:focusView.frame fromView:focusView.superview];
            RCTBaseTextInputView *parentView = (RCTBaseTextInputView *)focusView.superview;
            UIEdgeInsets borderInsets = parentView.reactBorderInsets;
            //view position realtive to scrollView container
            CGFloat containerFocusViewYPosition = CGRectGetMaxY(frame) + borderInsets.bottom;
            dy = CGRectGetHeight(rct.frame) - (containerFocusViewYPosition - scrollView.contentOffset.y);
        }
        
        if (dy < 0) {
            CGFloat range = scrollView.contentSize.height - scrollView.frame.size.height;
            CGPoint offset = scrollView.contentOffset;
            offset.y = MIN(range, offset.y - dy);
            //TODO: add isAnimationEnabled arq for enalbe and disable animation when keyboard will open
            [rct scrollToOffset:offset animated:isAnimationEnabled];//TODO:
        }
    }
}

- (void)textFieldDidBeginEditing:(NSNotification *)notification{
    if(_isKeyboardWillShow){
        RCTUITextField *textField = (RCTUITextField *)notification.object;
        [self adjustScrollViewOffsetIfNeeded:textField isAnimationEnabled:YES];
    }
}

@end
