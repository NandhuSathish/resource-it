/* eslint-disable react/react-in-jsx-scope */
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { describe, it } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ResourceState from '../resource-state';
// Create a client
const queryClient = new QueryClient();
// Define a test suite named 'App'.
describe('ResourceState', () => {
  // Render the 'App' component
  it('Renders App', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ResourceState />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
