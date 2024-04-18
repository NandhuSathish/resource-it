import { render } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import React from 'react';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import ResourceDetailAnalysisView from '../resource-detail-analysis-view';

import { describe, it,  } from 'vitest';
const queryClient = new QueryClient();

describe('ResourceDetailAnalysisView', () => {
  it('renders without crashing', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <Router>
          <ResourceDetailAnalysisView />
        </Router>
      </QueryClientProvider>
    );

  });
});
