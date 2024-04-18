import { renderHook } from '@testing-library/react-hooks';
import { useRouter } from '../use-router';
import { vi, describe, expect, it } from 'vitest';

vi.mock('react-router-dom', () => ({
  useNavigate: () => vi.fn(),
}));

describe('useRouter', () => {
  it('returns router object with correct methods', () => {
    const { result } = renderHook(() => useRouter());

    expect(result.current).toHaveProperty('back');
    expect(result.current).toHaveProperty('forward');
    expect(result.current).toHaveProperty('reload');
    expect(result.current).toHaveProperty('push');
    expect(result.current).toHaveProperty('replace');
  });
});
