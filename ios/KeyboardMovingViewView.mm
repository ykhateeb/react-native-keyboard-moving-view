#ifdef RCT_NEW_ARCH_ENABLED
#import "KeyboardMovingViewView.h"

#import <react/renderer/components/RNKeyboardMovingViewViewSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNKeyboardMovingViewViewSpec/EventEmitters.h>
#import <react/renderer/components/RNKeyboardMovingViewViewSpec/Props.h>
#import <react/renderer/components/RNKeyboardMovingViewViewSpec/RCTComponentViewHelpers.h>

#import "RCTFabricComponentsPlugins.h"
#import "Utils.h"

using namespace facebook::react;

@interface KeyboardMovingViewView () <RCTKeyboardMovingViewViewViewProtocol>

@end

@implementation KeyboardMovingViewView {
    UIView * _view;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
    return concreteComponentDescriptorProvider<KeyboardMovingViewViewComponentDescriptor>();
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps = std::make_shared<const KeyboardMovingViewViewProps>();
    _props = defaultProps;

    _view = [[UIView alloc] init];

    self.contentView = _view;
  }

  return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
    const auto &oldViewProps = *std::static_pointer_cast<KeyboardMovingViewViewProps const>(_props);
    const auto &newViewProps = *std::static_pointer_cast<KeyboardMovingViewViewProps const>(props);

    if (oldViewProps.color != newViewProps.color) {
        NSString * colorToConvert = [[NSString alloc] initWithUTF8String: newViewProps.color.c_str()];
        [_view setBackgroundColor: [Utils hexStringToColor:colorToConvert]];
    }

    [super updateProps:props oldProps:oldProps];
}

Class<RCTComponentViewProtocol> KeyboardMovingViewViewCls(void)
{
    return KeyboardMovingViewView.class;
}

@end
#endif
