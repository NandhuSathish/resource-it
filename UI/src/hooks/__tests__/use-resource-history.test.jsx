import { renderHook } from '@testing-library/react-hooks';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { describe, it, expect } from 'vitest';
import React from 'react';
import useResourceHistoryAnalysis from '../use-resource-history-analysis';

describe('useResourceHistoryAnalysis', () => {
  it('returns a function', () => {
    const queryClient = new QueryClient();
    const wrapper = ({ children }) => (
      <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    );

    const { result } = renderHook(() => useResourceHistoryAnalysis(), { wrapper });

    expect(typeof result.current.getResorceHistoryById.mutate).toBe('function');
  });
});
