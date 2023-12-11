import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  TextInput,
  Image,
  TouchableOpacity,
  FlatList,
} from 'react-native';
import { KeyboardMovingView } from 'react-native-keyboard-moving-view';
import { CameraMessageIcon, SendMessageIcon } from './assets';
/**
 * Header
 */
const Header = () => {
  return (
    <View style={styles.headerContainer}>
      <View style={styles.onlineDot} />
      <Text style={styles.headerText}>Michael</Text>
    </View>
  );
};

/**
 * Message
 */
interface MessageProps {
  message: string;
  direction: 'left' | 'right';
}

const Message = (props: MessageProps) => {
  return (
    <View
      style={[
        styles.messageContainer,
        props.direction === 'left' ? styles.messageLeft : styles.messageRight,
      ]}
    >
      <Text
        style={[
          styles.messageText,
          props.direction === 'left'
            ? styles.messageTextLeft
            : styles.messageTextRight,
        ]}
      >
        {props.message}
      </Text>
    </View>
  );
};

/**
 * Input
 */
const Input = () => {
  return (
    <View style={styles.inputContainer}>
      <TouchableOpacity>
        <Image source={CameraMessageIcon} style={styles.cameraIcon} />
      </TouchableOpacity>
      <TextInput
        placeholder="Start typing..."
        placeholderTextColor={'#C4C4C4'}
        style={styles.textInput}
      />
      <TouchableOpacity>
        <Image source={SendMessageIcon} style={styles.sendIcon} />
      </TouchableOpacity>
    </View>
  );
};

/**
 * Padding
 */
const messageList = [
  { direction: 'left', message: 'Hi. Sam' },
  { direction: 'right', message: 'Michael. Good to meet you!' },
  { direction: 'left', message: 'Did you just arrive here?' },
  { direction: 'right', message: 'Yeah, We arrived last week.' },
  { direction: 'left', message: 'How do you like it?' },
  {
    direction: 'right',
    message:
      'It’s exciting! It’s much busier than the last city we lived in. I was working in Seattle for the last 3 years.',
  },
  {
    direction: 'left',
    message:
      'It really is very busy. I moved here from Tokyo 5 years ago and I still have trouble sometimes. Did you move here with your wife?',
  },
  {
    direction: 'right',
    message:
      'Actually, I’m not married. I moved here with my dog, Charles. We are very close.',
  },
  { direction: 'left', message: 'Oh. I see.' },
  { direction: 'right', message: 'What about you?' },
  { direction: 'left', message: 'Yes, I am married and I have two children.' },
  { direction: 'right', message: 'How old are they?' },
  { direction: 'left', message: '6 and 8 years old' },
  { direction: 'right', message: 'Oh, great. That age is a lot of fun.' },
  { direction: 'left', message: 'But it is exhausting.' },
  {
    direction: 'right',
    message:
      'I understand. My brother has kids the same age. Every time we visit he falls asleep on the sofa.',
  },
  {
    direction: 'left',
    message:
      'Must be nice. We don’t have time for sleep, we have to drink a lot of coffee.',
  },
].reverse() as MessageProps[];

export const Padding = () => {
  const ref = React.useRef(null);

  return (
    <SafeAreaView style={styles.safeArea}>
      <KeyboardMovingView
        style={styles.keyboardMovingView}
        behavior="padding"
        extraHeight={0}
      >
        <Header />
        <View style={styles.messagesContainer}>
          <FlatList
            ref={ref}
            data={messageList}
            inverted
            renderItem={({ item }) => <Message {...item} />}
          />
        </View>
        <Input />
      </KeyboardMovingView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  //Container
  safeArea: { flex: 1, paddingTop: 10 },
  keyboardMovingView: { flex: 1 },
  messagesContainer: {
    flex: 1,
    paddingHorizontal: 15,
    backgroundColor: 'white',
  },
  //Header
  headerContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#EFEEEE',
    height: 50,
  },
  onlineDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: 'green',
    marginRight: 5,
  },
  headerText: {
    fontSize: 18,
    fontWeight: '500',
    color: 'black',
  },
  //Message
  messageContainer: {
    paddingVertical: 10,
    paddingHorizontal: 15,
    marginBottom: 15,
    alignSelf: 'baseline',
    borderRadius: 15,
  },
  messageLeft: {
    marginRight: 'auto',
    borderBottomLeftRadius: 5,
    backgroundColor: '#EFEEEE',
  },
  messageRight: {
    marginLeft: 'auto',
    borderBottomRightRadius: 5,
    backgroundColor: '#1C85FF',
  },
  messageText: {
    fontSize: 16,
    fontWeight: '500',
  },
  messageTextLeft: {
    color: 'black',
  },
  messageTextRight: {
    color: 'white',
  },
  //Input
  inputContainer: {
    backgroundColor: 'white',
    borderTopWidth: 1,
    borderTopColor: '#EFEEEE',
    width: '100%',
    marginTop: 'auto',
    flexDirection: 'row',
    height: 60,
    alignItems: 'center',
    paddingVertical: 10,
    paddingHorizontal: 15,
  },
  textInput: {
    fontSize: 16,
    height: '100%',
    flex: 1,
    backgroundColor: '#EFEEEE',
    borderRadius: 25,
    paddingHorizontal: 10,
  },
  sendIcon: {
    width: 30,
    height: 30,
    marginLeft: 10,
  },
  cameraIcon: {
    width: 30,
    height: 30,
    marginRight: 10,
  },
});
