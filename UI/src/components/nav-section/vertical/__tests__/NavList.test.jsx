/* eslint-disable react/react-in-jsx-scope */
import { render, screen } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import { NavListRoot, NavListSub } from '../NavList';
import { QueryClientProvider, QueryClient } from '@tanstack/react-query';
import { describe, it, expect } from 'vitest';
import '@testing-library/jest-dom/extend-expect';
const queryClient = new QueryClient();
describe('NavListRoot', () => {
  it('renders without crashing', () => {
    const mockList = {
      title: 'Root Item',
      path: '/rootitem',
      children: [
        { title: 'Sub Item 1', path: '/subitem1' },
        { title: 'Sub Item 2', path: '/subitem2' },
      ],
    };

    render(
      <QueryClientProvider client={queryClient}>
        <Router>
          <NavListRoot list={mockList} isCollapse={false} />
        </Router>
      </QueryClientProvider>
    );
  });

  it('does not render children when isCollapse is true', () => {
    const mockList = {
      title: 'Root Item',
      path: '/rootitem',
      children: [
        { title: 'Sub Item 1', path: '/subitem1' },
        { title: 'Sub Item 2', path: '/subitem2' },
      ],
    };

    render(
      <Router>
        <NavListRoot list={mockList} isCollapse={true} />
      </Router>
    );

    expect(screen.getByText('Root Item')).toBeInTheDocument();
    expect(screen.queryByText('Sub Item 1')).not.toBeInTheDocument();
    expect(screen.queryByText('Sub Item 2')).not.toBeInTheDocument();
  });

  it('does not render children when open is false', () => {
    const mockList = {
      title: 'Sub Item',
      path: '/subitem',
      children: [
        { title: 'Sub Sub Item 1', path: '/subsubitem1' },
        { title: 'Sub Sub Item 2', path: '/subsubitem2' },
      ],
    };

    render(
      <QueryClientProvider client={queryClient}>
        <Router>
          <NavListSub list={mockList} />
        </Router>
      </QueryClientProvider>
    );

    expect(screen.getByText('Sub Item')).toBeInTheDocument();
    expect(screen.queryByText('Sub Sub Item 1')).not.toBeInTheDocument();
    expect(screen.queryByText('Sub Sub Item 2')).not.toBeInTheDocument();
  });
});
