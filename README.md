# react-native-keyboard-moving-view

[![npm](https://img.shields.io/npm/v/react-native-keyboard-moving-view)](https://www.npmjs.com/package/react-native-keyboard-moving-view)
[![npm](https://img.shields.io/npm/dm/react-native-keyboard-moving-view)](https://www.npmjs.com/package/react-native-keyboard-moving-view)
![GitHub](https://img.shields.io/github/license/ykhateeb/react-native-keyboard-moving-view)
[![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/ykhateeb/react-native-keyboard-moving-view/ci.yml)](https://github.com/ykhateeb/react-native-keyboard-moving-view/actions/workflows/ci.yml)

Out-of-the-box alternative to [KeyboardAvoidingView](https://reactnative.dev/docs/keyboardavoidingview), that provides identical behavior on both iOS and Android, with more extra features.

## Demonstration

### IOS

|              Padding               |               Position               |
| :--------------------------------: | :----------------------------------: |
| ![padding](assets/ios-padding.gif) | ![position](assets/ios-position.gif) |

### Android

|                Padding                 |                 Position                 |
| :------------------------------------: | :--------------------------------------: |
| ![padding](assets/android-padding.gif) | ![position](assets/android-position.gif) |

## Installation

```sh
yarn add react-native-keyboard-moving-view
# OR
npm install react-native-keyboard-moving-view
```

#### IOS

```sh
cd ios && pod install
```

## Usage

```js
import React from 'react';
import { KeyboardMovingView } from 'react-native-keyboard-moving-view';
import { TextInput, StyleSheet, SafeAreaView } from 'react-native';

export default function App() {
  return (
    <SafeAreaView style={styles.safeAreaView}>
      <KeyboardMovingView
        style={styles.keyboardMovingView}
        behavior="position"
        extraHeight={25}
      >
        <TextInput style={styles.textInput} placeholder="Start typing" />
      </KeyboardMovingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeAreaView: { flex: 1 },
  keyboardMovingView: { flex: 1 },
  textInput: {
    fontSize: 17,
    height: 60,
    width: '100%',
    backgroundColor: '#EFEEEE',
    borderRadius: 5,
    paddingHorizontal: 15,
    marginTop: 'auto',
  },
});
```

Note: if you are using `SafeAreaView` component make sure to wrap it around the `KeyboardMovingView` component to work as expected in IOS like in the example above.

---

## Props

| Name                 | Type                    | Default | Description                                                                                                        |
| -------------------- | ----------------------- | ------- | ------------------------------------------------------------------------------------------------------------------ |
| `behavior`           | `padding` OR `position` | `null`  | Determines the behavior of the component when the keyboard is shown                                                |
| `extraHeight`        | `number`                | `0`     | Extra height between the keyboard and focused input(in `position` behavior) OR the content (in `padding` behavior) |
| `onKeyboardWillShow` | `() => void`            | `null`  | Called when the keyboard is about to be shown                                                                      |
| `onKeyboardWillHide` | `() => void`            | `null`  | Called when the keyboard is about to be hidden                                                                     |
| `onKeyboardDidShow`  | `() => void`            | `null`  | Called when the keyboard is shown                                                                                  |
| `onKeyboardDidHide`  | `() => void`            | `null`  | Called when the keyboard is hidden                                                                                 |
