/* eslint-disable no-unused-vars */
import React from 'react';
import PropTypes from 'prop-types';
import { TableRow, TableCell, IconButton, Box, Tooltip } from '@mui/material';
import useAuth from 'src/hooks/use-auth';
import Iconify from 'src/components/iconify';
import { getApprovalColor } from 'src/components/ui/sections/allocation-requests/utils';
import Label from 'src/components/label';
import { getResourceApprovalStatus } from '../utils';
// ----------------------------------------------------------------------

export default function ResourceWiseApprovalTableRow({
  id,
  projectCode,
  projectName,
  projectManager,
  resourceName,
  empId,
  departmentName,
  startDate,
  endDate,
  conflictDays,
  resourceId,
  projectId,
  approvalStatus,
  allocationId,
  onApprove,
  onReject,
}) {
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();

  return (
    <TableRow hover>
      <TableCell>
        <div className="break-words w-[8rem]">{projectCode}</div>
      </TableCell>
      <TableCell>
        <div className="break-words w-[10rem]">{projectName}</div>
      </TableCell>
      <TableCell>{projectManager}</TableCell>
      <TableCell>
        {resourceName}({empId})
      </TableCell>
      <TableCell>{departmentName}</TableCell>
      <TableCell align="center">
        <span style={{ whiteSpace: 'nowrap' }}>
          {startDate} <br />
        </span>
        to <br />
        <span style={{ whiteSpace: 'nowrap' }}>{endDate}</span>
      </TableCell>
      <TableCell align="center">{conflictDays}</TableCell>
      <TableCell align="center">
        <Label
          sx={{ width: 65 }}
          color={getApprovalColor(getResourceApprovalStatus(currentLoggedUser, approvalStatus))}
        >
          {getResourceApprovalStatus(currentLoggedUser, approvalStatus)}
        </Label>
      </TableCell>

      <TableCell align="right">
        <Box
          sx={{
            display: 'flex',
          }}
        >
          {/* approve btn.  */}
          <Tooltip title="Approve">
            <span>
              <IconButton
                onClick={() => {
                  onApprove(id, projectId, resourceId, allocationId, startDate, endDate);
                }}
                sx={{
                  backgroundHover: 'transparent',
                  '&:hover': {
                    backgroundColor: 'transparent',
                  },
                }}
                color="success"
                disabled={
                  getResourceApprovalStatus(currentLoggedUser, approvalStatus) !== 'Pending'
                }
              >
                <Iconify icon="charm:circle-tick" sx={{ mr: 0.5 }} />
              </IconButton>
            </span>
          </Tooltip>
          {/* reject btn.  */}
          <Tooltip title="Reject">
            <span>
              <IconButton
                onClick={() => {
                  onReject(id,projectId, resourceId, allocationId, startDate, endDate);
                }}
                color="error"
                sx={{
                  backgroundHover: 'transparent',
                  '&:hover': {
                    backgroundColor: 'transparent',
                  },
                }}
                disabled={
                  getResourceApprovalStatus(currentLoggedUser, approvalStatus) !== 'Pending'
                }
              >
                <Iconify icon="charm:circle-cross" sx={{ mr: 0.5 }} />
              </IconButton>
            </span>
          </Tooltip>
        </Box>
      </TableCell>
    </TableRow>
  );
}

ResourceWiseApprovalTableRow.propTypes = {
  id: PropTypes.any,
  projectCode: PropTypes.any,
  projectName: PropTypes.any,
  resourceName: PropTypes.any,
  empId: PropTypes.any,
  departmentName: PropTypes.any,
  projectManager: PropTypes.any,
  startDate: PropTypes.any,
  endDate: PropTypes.any,
  resourceId: PropTypes.any,
  projectId: PropTypes.any,
  conflictDays: PropTypes.any,
  approvalStatus: PropTypes.any,
  allocationId: PropTypes.any,
  onApprove: PropTypes.func,
  onReject: PropTypes.func,
};
