// FILEPATH: /home/shobithchandran/Documents/Updated ResourceIT/resource-it/UI/src/sections/project-details/project-resource-edit-table/__tests__/project-allocation-request-table-row.test.jsx

import { render, screen } from '@testing-library/react';
import ProjectAllocationRequestTableRow from '../project-allocation-edit-table-row';
import { vi, describe, expect, it } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import React from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
const queryClient = new QueryClient();
vi.mock('react-query', () => ({
    useQuery: () => vi.fn(),
    useMutation: () => vi.fn(),
}));

describe('ProjectAllocationRequestTableRow', () => {
    it('renders without crashing', () => {
        const props = {
            id: '1',
            resourceId: '1',
            projectId: '1',
            allocationType: 'type',
            employeeId: '1',
            startDate: '2022-01-01',
            endDate: '2022-12-31',
            remainingWorkingDays: 10,
            resourceName: 'Resource',
            departmentName: 'Department',
            band: 'Band',
            projectStartDate: '2022-01-01',
            projectEndDate: '2022-12-31',
        };
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ProjectAllocationRequestTableRow {...props} />
                </MemoryRouter>
            </QueryClientProvider>
        );

        expect(screen.getByText('Resource')).to.be.ok;
    });
});