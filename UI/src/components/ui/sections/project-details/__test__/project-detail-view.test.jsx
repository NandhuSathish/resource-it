/* eslint-disable react/react-in-jsx-scope */
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { describe, it } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ProjectDetailView from '../view/project-details-view';
// Create a client
const queryClient = new QueryClient();
// Define a test suite named 'App'.
describe('App', () => {
  // Render the 'App' component
  it('Renders App', () => {
    const route = '/project-detail';
    const state = {
      project: {
        startDate: '2022-01-01',
        endDate: '2022-12-31',
        skill: [{ name: 'JavaScript' }],
        manager: { name: 'Test Manager' },
      },
      allocationData: {},
    };
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter initialEntries={[{ pathname: route, state }]}>
          <ProjectDetailView />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
