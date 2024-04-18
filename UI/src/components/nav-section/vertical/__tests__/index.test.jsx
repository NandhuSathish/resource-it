/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';
import NavSectionVertical from '../index';
import { describe, it } from 'vitest';
import { QueryClientProvider, QueryClient } from '@tanstack/react-query';
import { BrowserRouter as Router } from 'react-router-dom';
// Initialize a new QueryClient instance
const queryClient = new QueryClient();
describe('NavSectionVertical', () => {
  it('renders without crashing', () => {
    const mockNavConfig = [
      {
        subheader: 'Group 1',
        items: [
          { title: 'Item 1', path: '/item1', roles: [1, 2, 3] },
          { title: 'Item 2', path: '/item2', roles: [1, 2, 3] },
        ],
      },
      {
        subheader: 'Group 2',
        items: [
          { title: 'Item 3', path: '/item3', roles: [1, 2, 3] },
          { title: 'Item 4', path: '/item4', roles: [1, 2, 3] },
        ],
      },
    ];

    render(
      <QueryClientProvider client={queryClient}>
        <Router>
          <NavSectionVertical navConfig={mockNavConfig} isCollapse={false} />
        </Router>
      </QueryClientProvider>
    );
  });
});
