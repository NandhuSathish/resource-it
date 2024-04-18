// FILEPATH: /home/shobithchandran/Documents/Updated ResourceIT/resource-it/UI/src/components/dashboard-component/__tests__/animated-progress-provider.test.jsx

import { render } from '@testing-library/react';
import AnimatedProgressProvider from '../AnimatedProgressProvider';
import { expect, it, describe } from 'vitest';
import React from 'react';
describe('AnimatedProgressProvider', () => {
  it('renders without crashing', () => {
    const { container } = render(
      <AnimatedProgressProvider
        valueStart={0}
        valueEnd={100}
        duration={1}
        easingFunction={(t) => t}
      >
        {(value) => <div>{value}</div>}
      </AnimatedProgressProvider>
    );

    expect(container.firstChild).to.exist;
  });
});
