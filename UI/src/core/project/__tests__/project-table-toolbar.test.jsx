import { render } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import ProjectTableToolbar from '../project-table-toolbar';
import { it, describe, vi } from 'vitest';
import React from 'react';
import { MemoryRouter } from 'react-router-dom';

vi.mock('src/stores/resourceStore', () => ({ default: () => ({ resourceQuery: {} }) }));
vi.mock('src/hooks/use-projects', () => ({
  default: () => ({ exportProjects: { mutate: vi.fn() } }),
}));
vi.mock('src/components/iconify', () => ({ default: vi.fn() }));
vi.mock('./project-filter', () => ({ default: vi.fn() }));
const queryClient = new QueryClient();

describe('ProjectTableToolbar', () => {
  it('Renders ProjectTableToolbar', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ProjectTableToolbar onFilterName={() => {}} />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
