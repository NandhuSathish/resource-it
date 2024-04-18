/* eslint-disable react/react-in-jsx-scope */
// FILEPATH: /home/nandhusathish/Pictures/resource-it/UI/src/components/__tests__/conflict-table.test.jsx

import { render, within } from '@testing-library/react';
import ConflictTable from '../conflict-table';
import { expect, describe, it } from 'vitest';
describe('ConflictTable', () => {
  it('renders the correct number of rows and columns', async () => {
    const data = [
      {
        resourceName: 'Resource 1',
        projectName: 'Project 1',
        startDate: '2022-01-01',
        endDate: '2022-01-31',
      },
      {
        resourceName: 'Resource 2',
        projectName: 'Project 2',
        startDate: '2022-02-01',
        endDate: '2022-02-28',
      },
    ];
    const { getAllByRole } = render(<ConflictTable data={data} />);

    const rows = getAllByRole('row');
    expect(rows).toHaveLength(data.length);

    rows.forEach((row, index) => {
      const cells = within(row).getAllByRole('cell');
      expect(cells).toHaveLength(4);
      expect(cells[0].textContent).toBe(data[index].resourceName);
      expect(cells[1].textContent).toBe(data[index].projectName);
      expect(cells[2].textContent).toBe(data[index].startDate);
      expect(cells[3].textContent).toBe(data[index].endDate);
    });
  });
});
