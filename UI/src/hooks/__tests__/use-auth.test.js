import { renderHook, act } from '@testing-library/react-hooks';
import { useMutation } from '@tanstack/react-query';
import { vi, describe, expect, it } from 'vitest';
import useAuth from '../use-auth';

vi.mock('@tanstack/react-query', () => ({
  useMutation: vi.fn(),
}));

vi.mock('src/services/interceptor', () => ({
  axiosInstance: {
    post: vi.fn(),
  },
}));

vi.mock('react-router-dom', () => ({
  useNavigate: () => vi.fn(),
}));

describe('useAuth', () => {
  it('calls useMutation with correct arguments', () => {
    renderHook(() => useAuth());

    expect(useMutation).toHaveBeenCalledWith(
      expect.objectContaining({
        mutationFn: expect.any(Function),
        onSuccess: expect.any(Function),
        onError: expect.any(Function),
      })
    );
  });

  it('returns user details correctly', () => {
    localStorage.setItem('email', 'test@example.com');
    localStorage.setItem('username', 'testuser');
    localStorage.setItem('role', '1');
    localStorage.setItem('pictureUrl', 'test.png');
    localStorage.setItem('resourceId', '1');

    const { result } = renderHook(() => useAuth());

    const userDetails = result.current.getUserDetails();

    expect(userDetails).toEqual({
      email: 'test@example.com',
      username: 'testuser',
      role: 1,
      pictureUrl: 'test.png',
      resourceId: '1',
    });
  });

  it('checks authentication status correctly', () => {
    localStorage.setItem('accessToken', 'testtoken');

    const { result } = renderHook(() => useAuth());

    const isAuthenticated = result.current.isAuthenticated();

    expect(isAuthenticated).toBe(true);
  });
  it('handles logout', () => {
    const { result } = renderHook(() => useAuth());
    act(() => result.current.logout());

    expect(localStorage.getItem('accessToken')).toBeNull();
    expect(localStorage.getItem('refreshToken')).toBeNull();
    expect(localStorage.getItem('role')).toBeNull();
    expect(localStorage.getItem('username')).toBeNull();
    expect(localStorage.getItem('resourceId')).toBeNull();
  });

  it('returns user details correctly', () => {
    localStorage.setItem('email', 'test@example.com');
    localStorage.setItem('username', 'testuser');
    localStorage.setItem('role', '1');
    localStorage.setItem('pictureUrl', 'test.png');
    localStorage.setItem('resourceId', '1');

    const { result } = renderHook(() => useAuth());

    const userDetails = result.current.getUserDetails();

    expect(userDetails).toEqual({
      email: 'test@example.com',
      username: 'testuser',
      role: 1,
      pictureUrl: 'test.png',
      resourceId: '1',
    });
  });

  it('returns user details correctly', () => {
    localStorage.setItem('email', 'test@example.com');
    localStorage.setItem('username', 'testuser');
    localStorage.setItem('role', '1');
    localStorage.setItem('pictureUrl', 'test.png');
    localStorage.setItem('resourceId', '1');


    const { result } = renderHook(() => useAuth());

    const userDetails = result.current.getUserDetails();

    expect(userDetails).toEqual({
      email: 'test@example.com',
      username: 'testuser',
      role: 1,
      pictureUrl: 'test.png',
      resourceId: '1',
    });
  });

  it('checks authentication status correctly', () => {
    localStorage.setItem('accessToken', 'testtoken');

    const { result } = renderHook(() => useAuth());

    const isAuthenticated = result.current.isAuthenticated();

    expect(isAuthenticated).toBe(true);
  });
});
16 - 22, 25;
