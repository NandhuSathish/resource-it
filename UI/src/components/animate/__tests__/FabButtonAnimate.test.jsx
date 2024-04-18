/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';

import FabButtonAnimate from '../FabButtonAnimate';
import { describe, it } from 'vitest';

describe('FabButtonAnimate', () => {
  it('renders without crashing', () => {
    render(<FabButtonAnimate />);
  });
});
