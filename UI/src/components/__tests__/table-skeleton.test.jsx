/* eslint-disable react/react-in-jsx-scope */
// FILEPATH: /home/nandhusathish/Pictures/resource-it/UI/src/components/__tests__/table-skeleton.test.jsx

import { render, within } from '@testing-library/react';
import TableSkeleton from '../table-skeleton';
import { expect, describe, it } from 'vitest';

describe('TableSkeleton', () => {
  it('renders the correct number of rows and columns', () => {
    const rowsNum = 5;
    const colsNum = 3;
    const { getAllByRole } = render(<TableSkeleton rowsNum={rowsNum} colsNum={colsNum} />);

    const rows = getAllByRole('row');
    expect(rows).toHaveLength(rowsNum);

    rows.forEach((row) => {
      const cells = within(row).getAllByRole('cell');
      expect(cells).toHaveLength(colsNum);
    });
  });
});
