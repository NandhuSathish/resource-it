import { render } from '@testing-library/react';
import React from 'react';
import { describe, it } from 'vitest';
import DashboardView from '../view/dashboard-view';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
const queryClient = new QueryClient();

describe('DashboardView', () => {
  it('renders without crashing', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <DashboardView />
      </QueryClientProvider>
    );
  });
});
