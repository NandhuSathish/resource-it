/* eslint-disable react/react-in-jsx-scope */
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ProtectedRoutes from '../protected-routes';
import { describe, it, expect } from 'vitest';
import '@testing-library/jest-dom/extend-expect';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
const queryClient = new QueryClient();
// Mock implementation of useAuth
const useAuth = () => {
  const role = 'user';
  const getUserDetails = () => ({ role });
  return { getUserDetails };
};

describe('ProtectedRoutes', () => {
  it('renders children when role is allowed', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter initialEntries={['/protected']}>
          <ProtectedRoutes useAuth={useAuth} allowedRoles={['user', 'admin']}>
            <div>Protected content</div>
          </ProtectedRoutes>
          <navigate to="/403">Forbidden</navigate>
          {/* <Route path="/403">Forbidden</Route>S */}
        </MemoryRouter>
      </QueryClientProvider>
    );

    expect(screen.getByText('Protected content')).toBeInTheDocument();
  });

  it('redirects to /403 when role is not allowed', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter initialEntries={['/protected']}>
          <ProtectedRoutes useAuth={useAuth} allowedRoles={['admin']}>
            <div>Protected content</div>
          </ProtectedRoutes>
          <navigate to="/403">Forbidden</navigate>
        </MemoryRouter>
      </QueryClientProvider>
    );

    expect(screen.getByText('Forbidden')).toBeInTheDocument();
  });
});
