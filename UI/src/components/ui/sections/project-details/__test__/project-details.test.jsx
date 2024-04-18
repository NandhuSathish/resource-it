import { render } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import { describe, it } from 'vitest';
import React from 'react';
import ProjectDetails from '../project-detaills';
import { MemoryRouter } from 'react-router-dom/dist';
const queryClient = new QueryClient();
describe('ProjectDetails', () => {
  it('renders without crashing', () => {
    const mockProjectDetails = {
      projectDetails: {
        startDate: '2022-01-01',
        endDate: '2022-12-31',
        skill: [{ name: 'JavaScript' }],
        manager: { name: 'Test Manager' },
      },
    };

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ProjectDetails {...mockProjectDetails} />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
