// FILEPATH: /home/nandhusathish/Music/resource-it/UI/src/hooks/__tests__/use-skill.test.js

import { renderHook } from '@testing-library/react-hooks';
import { useQuery } from '@tanstack/react-query';
import useSkill from '../use-get-skill';
import { vi, describe, expect, it } from 'vitest';

vi.mock('@tanstack/react-query', () => ({
  useQuery: vi.fn(),
}));

vi.mock('src/services/interceptor', () => ({
  axiosInstance: {
    get: vi.fn(),
  },
}));

describe('useSkill', () => {
  it('calls useQuery with correct arguments', () => {
    renderHook(() => useSkill());

    expect(useQuery).toHaveBeenCalledWith(
      expect.objectContaining({
        queryKey: ['skills'],
        queryFn: expect.any(Function),
        staleTime: 1000 * 60 * 60 * 0.5, // 30 mins
      })
    );
  });
});
