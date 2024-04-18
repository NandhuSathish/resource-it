// FILEPATH: /home/shobithchandran/Documents/Updated ResourceIT/resource-it/UI/src/components/table/__tests__/table-headers.test.jsx

import { render } from '@testing-library/react';
import TableHeaders from '../table-headers';
import { expect, it, describe } from 'vitest';
import React from 'react';

describe('TableHeaders', () => {
    it('renders without crashing', () => {
        const { container } = render(
            <TableHeaders 
                order='asc'
                orderBy=''
                headLabel={[{id: '1', label: 'Test'}]}
                onRequestSort={() => {}}
                sortableHeads={['1']}
                onSelectAllClick={() => {}}
                numSelected={0}
                rowCount={0}
                isSelectable={false}
            />
        );

        expect(container.firstChild).to.exist;
    });
});