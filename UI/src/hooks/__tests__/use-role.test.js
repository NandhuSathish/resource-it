import { renderHook } from '@testing-library/react-hooks';
import { useQuery } from '@tanstack/react-query';
import useRole from '../use-role';
import { vi, describe, expect, it } from 'vitest';

vi.mock('@tanstack/react-query', () => ({
  useQuery: vi.fn(),
}));

vi.mock('src/services/interceptor', () => ({
  axiosInstance: {
    get: vi.fn(),
  },
}));

describe('useRole', () => {
  it('calls useQuery with correct arguments', () => {
    renderHook(() => useRole());

    expect(useQuery).toHaveBeenCalledWith(
      expect.objectContaining({
        queryKey: ['roles'],
        queryFn: expect.any(Function),
        staleTime: 1000 * 60 * 60 * 0.5,
      })
    );
  });
});
