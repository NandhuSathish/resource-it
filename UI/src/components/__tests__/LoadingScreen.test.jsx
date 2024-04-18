/* eslint-disable react/react-in-jsx-scope */
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import LoadingScreen from '../LoadingScreen';
import { expect, describe, it } from 'vitest';

describe('LoadingScreen', () => {
  it('does not render the RootStyle component when isDashboard is true', () => {
    render(<LoadingScreen isDashboard={true} />);
    const rootStyle = screen.queryByTestId('root-style');
    expect(rootStyle).not.toBeInTheDocument();
  });
});
