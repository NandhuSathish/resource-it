import React from 'react';
import PropTypes from 'prop-types';
import { Box, TableRow, TableHead, TableCell, TableSortLabel, Typography } from '@mui/material';

import { visuallyHidden } from 'src/utils/utils';
// ----------------------------------------------------------------------

export default function TableHeaders({
  order = 'asc',
  orderBy = '',
  headLabel = [],
  onRequestSort = () => {},
  sortableHeads = [],
  isSelectable = false,
}) {
  const onSort = (property) => (event) => {
    onRequestSort(event, property);
  };

  return (
    <TableHead>
      <TableRow>
        {isSelectable && <TableCell sx={{ width: '10%' }} padding="checkbox"></TableCell>}

        {headLabel.map((headCell) => (
          <TableCell
            key={headCell.id}
            align={headCell.align || 'left'}
            sortDirection={orderBy === headCell.id ? order : false}
            sx={{ width: headCell.width, minWidth: headCell.minWidth, maxWidth: headCell.width }}
          >
            {sortableHeads.includes(headCell.id) ? (
              <TableSortLabel
                hideSortIcon
                active={orderBy === headCell.id}
                direction={orderBy === headCell.id ? order : 'asc'}
                onClick={onSort(headCell.id)}
              >
                <Typography variant="h6"> {headCell.label}</Typography>

                {orderBy === headCell.id ? (
                  <Box sx={{ ...visuallyHidden }}>
                    {order === 'desc' ? 'sorted descending' : 'sorted ascending'}
                  </Box>
                ) : null}
              </TableSortLabel>
            ) : (
              <Typography variant="h6"> {headCell.label}</Typography>
            )}
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
}
TableHeaders.propTypes = {
  order: PropTypes.oneOf(['asc', 'desc']),
  orderBy: PropTypes.string,
  rowCount: PropTypes.number,
  headLabel: PropTypes.array,
  numSelected: PropTypes.number,
  sortableHeads: PropTypes.array,
  onRequestSort: PropTypes.func,
  onSelectAllClick: PropTypes.func,
  isSelectable: PropTypes.bool,
};
