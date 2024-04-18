/* eslint-disable react/react-in-jsx-scope */
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { describe, it } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ResourceHistoryTableFilters from '../resource-history-filter'

// Create a client
const queryClient = new QueryClient();
// Define a test suite named 'App'.
describe('It renders correctly', () => {
  it('Renders correctly', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ResourceHistoryTableFilters />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
