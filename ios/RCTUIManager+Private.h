//
//  RCTUIManager+Private.h
//  react-native-keyboard-moving-view
//
//  Created by yazan on 21/09/2023.
//

#ifndef RCTUIManager_Private_h
#define RCTUIManager_Private_h

#import <React/RCTUIManager.h>

@interface RCTUIManager (Private)

- (void)updateView:(NSNumber *)reactTag viewName:(NSString *)viewName props:(NSDictionary *)props;
- (void)configureNextLayoutAnimation:(NSDictionary *)config withCallback:(RCTResponseSenderBlock)callback errorCallback: (__unused RCTResponseSenderBlock)errorCallback;

@end


#endif /* RCTUIManager_Private_h */
