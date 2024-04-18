import { renderHook } from '@testing-library/react-hooks';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { describe, it, expect } from 'vitest';
import React from 'react';
import useResourceDetailAnalysis from '../use-resource-details-analysis';

describe('useResourceDetailAnalysis', () => {
  it('returns two functions', () => {
    const queryClient = new QueryClient();
    const wrapper = ({ children }) => (
      <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    );

    const { result } = renderHook(() => useResourceDetailAnalysis(), { wrapper });

    expect(typeof result.current.getResorceDetailById.mutate).toBe('function');
    expect(typeof result.current.exportResourceDetail.mutate).toBe('function');
  });
});
