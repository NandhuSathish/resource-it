import React from 'react';
import { render } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import '@testing-library/jest-dom/extend-expect';
import LogoOnlyLayout from '../LogoOnlyLayout';

describe('MyComponent', () => {
  it('renders without crashing', () => {
    const { container } = render(<LogoOnlyLayout />);
    expect(container).toBeInTheDocument();
  });
});
