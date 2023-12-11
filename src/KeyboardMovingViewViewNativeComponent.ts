import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';
import type { ViewProps } from 'react-native';

interface NativeProps extends ViewProps {
  /**
   * Determines the behavior of the component when the keyboard is shown.
   * Possible values are 'padding' and 'position'.
   */
  behavior: 'padding' | 'position';
  /**
   * Extra height between the keyboard and focused input(in position behavior) OR the content(in padding behavior).
   * @default 0
   */
  extraHeight?: number;
  /**
   * Called when the keyboard is about to be shown.
   */
  onKeyboardWillShow?: () => void;
  /**
   * Called when the keyboard is about to be hidden.
   */
  onKeyboardWillHide?: () => void;
  /**
   * Called when the keyboard is shown.
   */
  onKeyboardDidShow?: () => void;
  /**
   * Called when the keyboard is hidden.
   */
  onKeyboardDidHide?: () => void;
}

export default codegenNativeComponent<NativeProps>('KeyboardMovingViewView');
