import React from 'react';
import PropTypes from 'prop-types';
import { Paper, TableRow, TableCell, Typography } from '@mui/material';
// ----------------------------------------------------------------------

export default function TableNoData() {
  return (
    <TableRow>
      <TableCell align="center" colSpan={8} sx={{ py: 3 }}>
        <Paper
          sx={{
            textAlign: 'center',
          }}
        >
          <Typography variant="h6" paragraph>
            No data found
          </Typography>
        </Paper>
      </TableCell>
    </TableRow>
  );
}

TableNoData.propTypes = {
  query: PropTypes.string,
};
