
/* eslint-disable react/react-in-jsx-scope */
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { describe, it } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ResourceHistoryTableView from '../resource-history-table/view/resource-history-view';
// Create a client
const queryClient = new QueryClient();
describe('It renders correctly', () => {
  it('Renders correctly', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ResourceHistoryTableView />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});

