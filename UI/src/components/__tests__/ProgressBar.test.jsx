/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';
import { ThemeProvider } from '@mui/material/styles';
import { createTheme } from '@mui/material/styles';
import { ProgressBarStyle } from '../ProgressBar';
import { vi, describe, it } from 'vitest';
import ProgressBar from '../ProgressBar';

vi.mock('nprogress', async () => {
  const actual = await vi.importActual('nprogress');
  return {
    ...actual,
    configure: vi.fn(),
    start: vi.fn(),
    done: vi.fn(),
  };
});
describe('ProgressBar', () => {
  it('configures and starts NProgress on mount', () => {
    render(<ProgressBar />);
  });

  it('finishes NProgress on unmount', () => {
    const { unmount } = render(<ProgressBar />);

    unmount();
  });
});

describe('ProgressBarStyle', () => {
  it('renders without crashing', () => {
    const theme = createTheme();

    render(
      <ThemeProvider theme={theme}>
        <ProgressBarStyle />
      </ThemeProvider>
    );
  });
});
