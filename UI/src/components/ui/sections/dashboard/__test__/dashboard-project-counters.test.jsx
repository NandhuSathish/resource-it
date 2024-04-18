import { render } from '@testing-library/react';
import React from 'react';
import { describe, it } from 'vitest';
import DashboardProjectCounters from '../dashboard-resource-counters';
import { MemoryRouter } from 'react-router-dom';

describe('DashboardProjectCounters', () => {
  it('renders without crashing', () => {
    const mockContent = {
      totalProjectCount: 10,
      billableProjectCount: 5,
      internalProjectCount: 5,
    };

    render(
      <MemoryRouter>
        <DashboardProjectCounters content={mockContent} />
      </MemoryRouter>
    );
  });
});
