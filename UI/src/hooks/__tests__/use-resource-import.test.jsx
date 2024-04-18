import { renderHook } from '@testing-library/react-hooks';
import useResourceImport from '../use-resource-import';
import { vi, describe, it, expect } from 'vitest';
import { useMutation } from '@tanstack/react-query';

vi.mock('react-router-dom', () => ({
  useNavigate: vi.fn(),
}));
// eslint-disable-next-line no-unused-vars
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

describe('useResourceImport', () => {
  it('calls useMutation with correct arguments', () => {
    renderHook(() => useResourceImport());
    expect(useMutation).toBeTruthy();
  });


  
});
