import { render } from '@testing-library/react';
import ProjectDetailsContent from '../project-detail-content';
import { describe, it } from 'vitest';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';
import { MemoryRouter } from 'react-router-dom/dist';

const queryClient = new QueryClient();

describe('ProjectDetailsContent', () => {
  it('renders without crashing', () => {
    const mockProjectDetails = {
      projectDetails: {
        teamSize: 5,
        manDay: 30,
        name: 'Test Project',
        projectCode: 'TP123',
        projectType: 'Test Type',
        status: 'Active',
        skill: [{ name: 'JavaScript' }],
        manager: { name: 'Test Manager' },
        clientName: 'Test Client',
        startDate: '2022-01-01',
        endDate: '2022-12-31',
      },
    };

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ProjectDetailsContent projectDetails={mockProjectDetails} />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
