/* eslint-disable react/react-in-jsx-scope */
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { describe, it } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ResourceTableToolbar from '../resource-table-toolbar';
// Create a client
const queryClient = new QueryClient();
// Define a test suite named 'App'.
describe('ResourceRow', () => {
  // Render the 'ResourceRow' component
  it('Renders ResourceRow', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ResourceTableToolbar />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
