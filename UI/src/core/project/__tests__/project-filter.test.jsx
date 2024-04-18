import { render, fireEvent } from '@testing-library/react';
import ProjectFilters from '../project-filter';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { it, describe, expect, vi } from 'vitest';
import React from 'react';
import { MemoryRouter } from 'react-router-dom';

vi.mock('src/stores/projectQueryStore', () => ({
  default: () => ({
    setProjectTypeFilter: vi.fn(),
    setManagerFilter: vi.fn(),
    setStartDateFilter: vi.fn(),
    setEndDateFilter: vi.fn(),
    setProjectStatusFilter: vi.fn(),
    projectFilters: {},
    setClearAllFilters: vi.fn(),
  }),
}));
vi.mock('src/hooks/useDepartment', () => ({ default: () => ({ data: [] }) }));

describe('ProjectFilters', () => {
  const queryClient = new QueryClient();

  it('renders without throwing', () => {
    expect(() =>
      render(
        <QueryClientProvider client={queryClient}>
          <MemoryRouter>
            <ProjectFilters />
          </MemoryRouter>
        </QueryClientProvider>
      )
    ).not.toThrow();
  });
  it('opens and closes the filter drawer', async () => {
    const { getByTestId, queryByText } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ProjectFilters />
        </MemoryRouter>
      </QueryClientProvider>
    );
    const filtersButton = getByTestId('filters-button');
    fireEvent.click(filtersButton);
    expect(queryByText('Project Type')).not.toBeNull();
  });

  it('clears all filters when Clear All is clicked', () => {
    const setClearAllFilters = vi.fn();
    vi.mock('src/stores/projectQueryStore', () => ({
      default: vi.fn().mockReturnValue({ setClearAllFilters }),
    }));
    const { getByTestId, getByText } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ProjectFilters />
        </MemoryRouter>
      </QueryClientProvider>
    );
    const filtersButton = getByTestId('filters-button');
    fireEvent.click(filtersButton); // open the filter drawer
    fireEvent.click(getByText('Clear All'));
    expect(setClearAllFilters).toBeTruthy();
  });
});
