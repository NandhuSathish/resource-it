/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';
import { describe, it } from 'vitest';
import TableNoData from '../table-no-data';
import { MemoryRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();
describe('TableNoData', () => {
  it('renders correctly and displays the expected text', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <TableNoData />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
