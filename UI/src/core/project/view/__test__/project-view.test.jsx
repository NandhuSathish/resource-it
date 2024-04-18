import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import ProjectView from '../project-view';
import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import '@testing-library/jest-dom';

// Create a client
const queryClient = new QueryClient();

describe('ProjectView', () => {
  it('renders without crashing', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <Router>
          <ProjectView />
        </Router>
      </QueryClientProvider>
    );
  });
  it('renders the project table', async () => {
    render(
      <QueryClientProvider client={queryClient}>
        <Router>
          <ProjectView />
        </Router>
      </QueryClientProvider>
    );

    // Check if the table headers are rendered
    expect(screen.getByText('Project Code')).toBeInTheDocument();
    expect(screen.getByText('Project Name')).toBeInTheDocument();
    expect(screen.getByText('Project Type')).toBeInTheDocument();
    expect(screen.getByText('Project Manager')).toBeInTheDocument();
    expect(screen.getByText('Start Date')).toBeInTheDocument();
    expect(screen.getByText('End Date')).toBeInTheDocument();
    expect(screen.getByText('Team Size')).toBeInTheDocument();
    expect(screen.getByText('Project Status')).toBeInTheDocument();
  });
});
