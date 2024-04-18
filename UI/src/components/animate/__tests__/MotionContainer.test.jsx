/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';

import MotionContainer from '../MotionContainer';
import { describe, it } from 'vitest';

describe('MotionContainer', () => {
  it('renders without crashing', () => {
    render(<MotionContainer />);
  });
});
