// FILEPATH: /home/nandhusathish/Downloads/resource-itNew2/resource-it/UI/src/hooks/__tests__/use-debounce.test.js
import { renderHook, act } from '@testing-library/react-hooks';
import useDebounce from '../use-debounce';
import { vi, describe, expect, it } from 'vitest';

vi.useFakeTimers();

describe('useDebounce hook', () => {
  it('should return the same value after delay', () => {
    const { result } = renderHook(() => useDebounce('test', 500));

    expect(result.current).toBe('test');

    act(() => {
      vi.advanceTimersByTime(500);
    });

    expect(result.current).toBe('test');
  });

  it('should return the initial value before delay', () => {
    const { result, rerender } = renderHook(({ value, delay }) => useDebounce(value, delay), {
      initialProps: { value: 'test1', delay: 500 },
    });

    rerender({ value: 'test2', delay: 500 });

    expect(result.current).toBe('test1');
  });

  it('should return the updated value after delay', () => {
    const { result, rerender } = renderHook(({ value, delay }) => useDebounce(value, delay), {
      initialProps: { value: 'test1', delay: 500 },
    });

    rerender({ value: 'test2', delay: 500 });

    act(() => {
      vi.advanceTimersByTime(500);
    });

    expect(result.current).toBe('test2');
  });
});
