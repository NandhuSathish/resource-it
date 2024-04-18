// FILEPATH: /home/shobithchandran/Documents/Updated ResourceIT/resource-it/UI/src/hooks/__tests__/use-dashboard.test.js

import { renderHook } from '@testing-library/react-hooks';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import useDashboard from '../use-dashboard'; // Corrected import
import { vi, describe, expect, it } from 'vitest';
import React from 'react';
vi.mock('react-query', () => ({
    useQuery: () => vi.fn(),
    useMutation: () => vi.fn(),
}));

const queryClient = new QueryClient();

describe('useDashboard', () => {
    it('returns object with correct methods', () => {
        const wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
        const { result } = renderHook(() => useDashboard(), { wrapper });

        expect(result.current).toHaveProperty('getDashboardData');
        expect(result.current).toHaveProperty('getDashboardPieChartData');
    });
});