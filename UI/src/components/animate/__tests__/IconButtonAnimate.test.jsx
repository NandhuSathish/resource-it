/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';

import IconButtonAnimate from '../IconButtonAnimate';
import { describe, it } from 'vitest';

describe('IconButtonAnimate', () => {
  it('renders without crashing', () => {
    render(<IconButtonAnimate />);
  });
});
