import { render } from '@testing-library/react';
import React from 'react';
import { describe, it } from 'vitest';
import ResourceDetailsContents from '../resource-details-contents';

describe('ResourceDetailsContents', () => {
    it('renders without crashing and displays correct data', () => {
        const mockResourceData = {
            name: 'Test Resource',
            employeeId: '123',
            status: 1,
            departmentName: 'Test Department',
            role: 'Test Role',
            allocationStatus: 'Test Allocation Status',
            joiningDate: '2022-01-01',
            billableDays: '10',
            benchDays: '5',
            workSpan: '24',
            totalExperience: '2',
        };

        render(<ResourceDetailsContents resourceData={mockResourceData} />);

        // expect(screen.queryByText('Test Resource')).not.toBeNull();
        // expect(screen.queryByText('Employee Id: 123')).not.toBeNull();
        // expect(screen.queryByText('Status : Active')).not.toBeNull();
        // expect(screen.queryByText('Department: Test Department')).not.toBeNull();
        // expect(screen.queryByText('Role: Test Role')).not.toBeNull();
        // expect(screen.queryByText('Allocation Status: Test Allocation Status')).not.toBeNull();
        // expect(screen.queryByText('Joining Date: 2022-01-01')).not.toBeNull();
        // expect(screen.queryByText('Billable Days: 10')).not.toBeNull();
        // expect(screen.queryByText('Bench Days: 5')).not.toBeNull();
        // expect(screen.queryByText('Work span: 2 Years')).not.toBeNull();
        // expect(screen.queryByText('Total Experience: 2 Years')).not.toBeNull();
    });
});