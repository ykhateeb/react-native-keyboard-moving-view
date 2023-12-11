//
//  UpdateViewDelegate.h
//  react-native-keyboard-moving-view
//
//  Created by yazan on 21/09/2023.
//

#ifndef UpdateViewDelegate_h
#define UpdateViewDelegate_h

#import <React/RCTUIManager.h>

@protocol UpdateViewDelegate <NSObject>

- (void)updateProps:(NSDictionary *)props;

@end

#endif /* UpdateViewDelegate_h */
