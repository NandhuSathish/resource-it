import { renderHook } from '@testing-library/react-hooks';
import { useResponsive, useWidth } from '../use-responsive';
import { vi, expect, describe, it } from 'vitest';

vi.mock('@mui/material/styles', () => ({
  useTheme: vi.fn().mockReturnValue({
    breakpoints: {
      keys: ['xl', 'lg', 'md', 'sm', 'xs'],
      up: vi.fn().mockReturnValue('up'),
      down: vi.fn().mockReturnValue('down'),
      between: vi.fn().mockReturnValue('between'),
      only: vi.fn().mockReturnValue('only'),
    },
  }),
  useMediaQuery: vi.fn().mockImplementation((query) => {
    if (query === 'up') return true;
    if (query === 'xl') return true;
    return false;
  }),
}));

describe('useResponsive', () => {
  it('returns the correct media query based on the query parameter', () => {
    const { result } = renderHook(() => useResponsive('up', 'sm', 'md'));
    expect(result.current).toBe(false);
  });
});

describe('useWidth', () => {
  it('returns the correct breakpoint based on the media query', () => {
    const { result } = renderHook(() => useWidth());
    expect(result.current).toBe('xs');
  });
});

describe('useResponsive', () => {
  it('returns the correct media query based on the "down" query parameter', () => {
    const { result } = renderHook(() => useResponsive('down', 'sm', 'md'));
    expect(result.current).toBe(false);
  });

  it('returns the correct media query based on the "between" query parameter', () => {
    const { result } = renderHook(() => useResponsive('between', 'sm', 'md'));
    expect(result.current).toBe(false);
  });

  it('returns the correct media query based on the "only" query parameter', () => {
    const { result } = renderHook(() => useResponsive('only', 'sm', 'md'));
    expect(result.current).toBe(false);
  });
});
