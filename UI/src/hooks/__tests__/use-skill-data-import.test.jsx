import { renderHook } from '@testing-library/react-hooks';
import { describe, it, expect, vi } from 'vitest';
import React from 'react';

import useSkillDataImport from '../use-skill-data-import';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';


vi.mock('src/services/interceptor');
vi.mock('sonner');
vi.mock('src/utils/error-codes');


describe('useSkillDataImport', () => {
  it('returns a function', () => {
    const queryClient = new QueryClient();
    const wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
    const { result } = renderHook(() => useSkillDataImport(), { wrapper });
    expect(typeof result.current.mutate).toBe('function');
  });
});