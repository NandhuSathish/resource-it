/* eslint-disable react/react-in-jsx-scope */
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { describe, it, vi } from 'vitest';
import { render, fireEvent } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ResourceView from '../view/resource-view';

vi.mock('...', () => ({
  // replace '...' with the path to the getResources function
  getResources: {
    mutate: vi.fn(),
  },
}));

const queryClient = new QueryClient();
// Define a test suite named 'App'.
describe('ResourceView', () => {
  // Render the 'App' component
  it('Renders App', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ResourceView />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });

  it('handles sort correctly', () => {
    const { getByText } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ResourceView />
        </MemoryRouter>
      </QueryClientProvider>
    );

    fireEvent.click(getByText('Name'));
    // Add assertions to check if the order and orderBy states have been set correctly
  });

  it('handles page change correctly', () => {
    const { getByLabelText } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ResourceView />
        </MemoryRouter>
      </QueryClientProvider>
    );

    // Simulate a page change event
    fireEvent.click(getByLabelText('Go to next page'));
  });

  it('handles filter by name correctly', () => {
    const { getByPlaceholderText } = render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ResourceView />
        </MemoryRouter>
      </QueryClientProvider>
    );

    // Simulate a filter by name change event
    fireEvent.change(getByPlaceholderText('Search Resource...'), { target: { value: 'John' } });

    // Add assertions to check if the filterName state has been updated correctly
    // This might be tricky because the state is internal to the component
    // You might need to check for some visible change in the component (like the rendering of a different set of resources)
  });
});
