/* eslint-disable react/react-in-jsx-scope */
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { describe, it } from 'vitest';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom';
import Index from '../index';
import { MemoryRouter } from 'react-router-dom';

// Create a client
const queryClient = new QueryClient();

describe('Index', () => {
  it('Renders Index ', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <Index />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
