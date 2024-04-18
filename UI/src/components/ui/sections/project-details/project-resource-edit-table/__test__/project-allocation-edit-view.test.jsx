// FILEPATH: /home/shobithchandran/Documents/Updated ResourceIT/resource-it/UI/src/sections/project-details/project-resource-edit-table/__tests__/project-allocation-edit-view.test.jsx

import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ProjectAllocationEditView from '../project-allocation-edit-view';
import { it, describe } from 'vitest';
import React from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
const queryClient = new QueryClient();

describe('ProjectAllocationEditView', () => {
  const allocationData = {
    projectDetails: {
      startDate: '2022-01-01',
      endDate: '2022-12-31',
    },
  };

  it('renders without crashing', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ProjectAllocationEditView allocationData={allocationData} />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
