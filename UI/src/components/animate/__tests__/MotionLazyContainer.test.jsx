/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';

import MotionLazyContainer from '../MotionLazyContainer';
import { describe, it } from 'vitest';

describe('MotionLazyContainer', () => {
  it('renders without crashing', () => {
    render(<MotionLazyContainer />);
  });
});
