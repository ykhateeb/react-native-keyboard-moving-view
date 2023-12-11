import React from 'react';
import { Padding } from './Padding';
import { Position } from './Position';

const behavior: 'padding' | 'position' = 'position';

export default function App() {
  switch (behavior) {
    case 'padding':
      return <Padding />;
    case 'position':
      return <Position />;
  }
}
