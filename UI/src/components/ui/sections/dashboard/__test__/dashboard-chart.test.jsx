import { render} from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import DashboardChart from '../dashboard-chart';
import { vi, describe, it } from 'vitest';
import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';

vi.mock('react-query', () => ({
  useQuery: () => vi.fn(),
  useMutation: () => vi.fn(),
}));

const queryClient = new QueryClient();

describe('DashboardChart', () => {
  it('renders without crashing', () => {
    const wrapper = ({ children }) => (
      <QueryClientProvider client={queryClient}>
        <Router>{children}</Router>
      </QueryClientProvider>
    );
    render(<DashboardChart />, { wrapper });
  });
});
