/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';
import { QueryClientProvider, QueryClient } from '@tanstack/react-query';
import { BrowserRouter as Router } from 'react-router-dom';
import { NavItemRoot, NavItemSub } from '../NavItem';
import { describe, it } from 'vitest';
const queryClient = new QueryClient();
describe('NavItemRoot', () => {
  it('renders without crashing', () => {
    const mockItem = {
      title: 'Item 1',
      path: '/item1',
      icon: <span>Icon</span>,
    };

    render(
      <QueryClientProvider client={queryClient}>
        <Router>
          <NavItemRoot item={mockItem} />
        </Router>
      </QueryClientProvider>
    );
  });
});

describe('NavItemSub', () => {
  it('renders without crashing', () => {
    const mockItem = {
      title: 'Sub Item 1',
      path: '/subitem1',
    };

    render(
      <QueryClientProvider client={queryClient}>
        <Router>
          <NavItemSub item={mockItem} />
        </Router>
      </QueryClientProvider>
    );
  });
});
