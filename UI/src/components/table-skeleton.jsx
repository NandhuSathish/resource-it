/* eslint-disable no-unused-vars */
/* eslint-disable react/react-in-jsx-scope */
import { Skeleton } from '@mui/material';
import { TableCell, TableRow } from 'src/components/ui/table';
import { v4 as uuidv4 } from 'uuid';

const TableSkeleton = ({ rowsNum, colsNum }) => {
  return [...Array(rowsNum)].map((row, index) => (
    <TableRow key={uuidv4()}>
      {[...Array(colsNum)].map((col, colIndex) => (
        <TableCell
          key={uuidv4()}
          component={colIndex === 0 ? 'th' : 'td'}
          //   sx={{ padding: colIndex === 0 ? '16px 16px 16px 24px' : '16px' }}
          className={colIndex === 0 ? 'p-4 pl-6' : 'p-4'}
          scope={colIndex === 0 ? 'row' : undefined}
        >
          <Skeleton animation="wave" variant="text" />
        </TableCell>
      ))}
    </TableRow>
  ));
};

export default TableSkeleton;
