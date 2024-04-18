import { render } from '@testing-library/react';
import React from 'react';
import { describe, it } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import DashboardResourceCounters from '../dashboard-project-counters';
import { vi,} from 'vitest';
const queryClient = new QueryClient();
vi.mock('react-query', () => ({
  useQuery: () => vi.fn(),
  useMutation: () => vi.fn(),
}));

describe('DashboardResourceCounters', () => {
  it('renders without crashing', () => {
    const mockContent = {
      totalResourceCount: 10,
      billableResourceCount: 5,
      internalProjectCount: 5,
      benchResourceCount: 2,
    };

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <DashboardResourceCounters {...mockContent} />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
