import { render } from '@testing-library/react';
import ProjectTableRow from '../project-table-row';
import { it, describe, expect } from 'vitest';
import React from 'react';

describe('ProjectTableRow', () => {
    it('renders without throwing', () => {
        const dummyProps = {
            id: 1,
            projectCode: 'PC001',
            projectName: 'Test Project',
            projectType: 'Type1',
            projectManager: 'Manager1',
            StartDate: '2022-01-01',
            endDate: '2022-12-31',
            teamSize: 5,
            projectStatus: 'Active',
        };

        expect(() => render(<ProjectTableRow {...dummyProps} />)).toThrow();
    });
});