/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';
import NavbarAccount from '../NavbarAccount';
import { describe, it } from 'vitest';
import { QueryClientProvider, QueryClient } from '@tanstack/react-query';
import { BrowserRouter as Router } from 'react-router-dom';

// Initialize a new QueryClient instance
const queryClient = new QueryClient();

describe('NavbarAccount', () => {
  it('renders without crashing', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <Router>
          <NavbarAccount isCollapse={false} />
        </Router>
      </QueryClientProvider>
    );
  });
});
