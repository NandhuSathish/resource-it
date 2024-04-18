import { renderHook, act } from '@testing-library/react-hooks';
import { vi, describe, expect, it } from 'vitest';
import useHolidayUpload from '../use-import-holiday';

let mutationOptions;

vi.mock('@tanstack/react-query', () => ({
  useMutation: (options) => {
    mutationOptions = options;
    options.mutationFn(new File(['(⌐□_□)'], 'chill.png'));
    options.onSuccess(true);

    const mockError = {
      response: {
        data: {
          errorCode: 'SOME_ERROR_CODE',
        },
      },
    };

    options.onError(mockError);

    return { mutate: vi.fn() };
  },
}));

vi.mock('src/services/interceptor', () => ({
  axiosInstance: {
    post: vi.fn().mockResolvedValue({ data: {} }),
  },
}));

vi.mock('sonner', () => ({
  toast: {
    success: vi.fn(),
    error: vi.fn(),
  },
}));

describe('useHolidayUpload', () => {
  it('calls useMutation with correct arguments and executes mutationFn, onSuccess, onError', () => {
    const { result } = renderHook(() => useHolidayUpload());

    act(() => {
      expect(result.current.mutate).toBeDefined();
    });

    expect(mutationOptions).toEqual(
      expect.objectContaining({
        mutationFn: expect.any(Function),
        onSuccess: expect.any(Function),
        onError: expect.any(Function),
      })
    );
  });
});
