/* eslint-disable react/react-in-jsx-scope */
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { describe, it } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ResourceRow from '../resource-table-row';
// Create a client
const queryClient = new QueryClient();
// Define a test suite named 'App'.
describe('ResourceRow', () => {
  // Render the 'ResourceRow' component
  it('Renders ResourceRow', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ResourceRow
            skill={[{ skillName: 'JavaScript', experience: '24', expertise: '2' }]}
            // Add other necessary props here
          />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
