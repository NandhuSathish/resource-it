/* eslint-disable no-unused-vars */
import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { TableRow, TableCell } from '@mui/material';
import {  getProjectType } from 'src/utils/utils';

// ----------------------------------------------------------------------

export default function ResourceHistoryTableRow({

  projectCode,
  projectName,
  projectType,
  startDate,
  endDate,
}) {

  return (
    <TableRow hover>
      <TableCell>
        <div className="break-words w-[10rem]">{projectCode}</div>
      </TableCell>
      <TableCell>
        <div className="break-words w-[10rem]">{projectName}</div>
      </TableCell>
      <TableCell>{getProjectType(projectType)}</TableCell>
      <TableCell align="center" style={{ whiteSpace: 'nowrap' }}>
        {startDate} to {endDate}
      </TableCell>
    </TableRow>
  );
}

ResourceHistoryTableRow.propTypes = {
  projectCode: PropTypes.any,
  projectName: PropTypes.any,
  projectType: PropTypes.any,
  startDate: PropTypes.any,
  endDate: PropTypes.any,

};
