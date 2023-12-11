import React from 'react';
import {
  StyleSheet,
  View,
  TextInput,
  SafeAreaView,
  ScrollView,
} from 'react-native';
import { KeyboardMovingView } from 'react-native-keyboard-moving-view';

/**
 * Input
 */
interface InputProps {
  placeholder: string;
}

const Input = (props: InputProps) => {
  return (
    <TextInput
      placeholderTextColor={'#C4C4C4'}
      placeholder={props.placeholder}
      style={styles.input}
    />
  );
};

/**
 * Position
 */
const inputsList = Array.from({ length: 20 }, (_, i) => `TextInput - ${i + 1}`);

export const Position = () => {
  return (
    <SafeAreaView style={styles.safeArea}>
      <KeyboardMovingView behavior="position" style={styles.keyboardMovingView}>
        <View style={styles.inputsContainer}>
          <ScrollView>
            {inputsList.map((item, index) => (
              <Input key={index} placeholder={item.toString()} />
            ))}
          </ScrollView>
        </View>
      </KeyboardMovingView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: { flex: 1, paddingTop: 10 },
  keyboardMovingView: { flex: 1 },
  //Container
  inputsContainer: {
    flex: 1,
    paddingHorizontal: 15,
    backgroundColor: 'white',
  },
  //Input
  input: {
    fontSize: 17,
    height: 60,
    flex: 1,
    backgroundColor: '#EFEEEE',
    borderRadius: 5,
    paddingHorizontal: 15,
    marginBottom: 10,
  },
});
