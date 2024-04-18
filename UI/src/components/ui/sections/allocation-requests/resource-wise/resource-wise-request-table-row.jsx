/* eslint-disable react/react-in-jsx-scope */
/* eslint-disable no-unused-vars */

import PropTypes from 'prop-types';
import { TableRow, TableCell, IconButton, Box, Typography, Tooltip } from '@mui/material';
import useAuth from 'src/hooks/use-auth';
import Iconify from 'src/components/iconify';
import { getApprovalColor, getApprovalStatusOfResourceRequestList } from '../utils';
import Label from 'src/components/label';
import { useConfirm } from 'material-ui-confirm';
import {
  confirmationButtonProps,
  cancelButtonProps,
  dialogProps,
} from 'src/utils/confirmationDialogProps';
// ----------------------------------------------------------------------

export default function ResourceWiseRequestTableRow({
  id,
  projectCode,
  projectName,
  resourceName,
  departmentName,
  empId,
  startDate,
  endDate,
  approvalStatus,
  onDelete,
}) {
  const confirm = useConfirm();
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();

  const handleDeleteClick = (id) => {
    confirm({
      title: (
        <Typography variant="h6" style={{ fontSize: '18px' }}>
          Confirmation
        </Typography>
      ),
      description: (
        <Typography variant="body1" style={{ fontSize: '16px' }}>
          Are you sure you want to proceed?
        </Typography>
      ),
      confirmationButtonProps: {
        ...confirmationButtonProps,
      },
      cancelButtonProps: { ...cancelButtonProps },
      dialogProps: {
        ...dialogProps,
      },
      //   onConfirm: handleConfirm,
    })
      .then(() => {
        onDelete(id);
      })
      .catch(() => {
        /* ... */
        console.log('not confirmed');
      });
  };

  return (
    <TableRow hover>
      <TableCell className="break-all">{projectCode}</TableCell>
      <TableCell className="break-all">{projectName}</TableCell>
      <TableCell>
        {resourceName} ({empId})
      </TableCell>
      <TableCell>{departmentName}</TableCell>
      <TableCell align="center">
        <span style={{ whiteSpace: 'nowrap' }}>
          {startDate} <br />
        </span>
        to <br />
        <span style={{ whiteSpace: 'nowrap' }}>{endDate}</span>
      </TableCell>

      <TableCell align="center">
        <Label
          sx={{ width: 65 }}
          color={getApprovalColor(
            getApprovalStatusOfResourceRequestList(currentLoggedUser, approvalStatus)
          )}
        >
          {getApprovalStatusOfResourceRequestList(currentLoggedUser, approvalStatus)}
        </Label>
      </TableCell>
      <TableCell align="right">
        <Box
          sx={{
            display: 'flex',
          }}
        >
          {/* delete btn.  */}
          <Tooltip title={'Delete'}>
            <span>
              <IconButton
                disabled={
                  getApprovalStatusOfResourceRequestList(currentLoggedUser, approvalStatus) !==
                  'Pending'
                }
                onClick={() => {
                  handleDeleteClick(id);
                }}
                color="error"
                sx={{
                  backgroundHover: 'transparent',
                  '&:hover': {
                    backgroundColor: 'transparent',
                  },
                }}
              >
                <Iconify icon="eva:trash-2-outline" sx={{ mr: 0.5 }} />
              </IconButton>
            </span>
          </Tooltip>
        </Box>
      </TableCell>
    </TableRow>
  );
}

ResourceWiseRequestTableRow.propTypes = {
  id: PropTypes.any,
  empId: PropTypes.any,
  projectCode: PropTypes.any,
  projectName: PropTypes.any,
  resourceName: PropTypes.any,
  departmentName: PropTypes.any,
  startDate: PropTypes.any,
  endDate: PropTypes.any,
  approvalStatus: PropTypes.any,
  onDelete: PropTypes.func,
};
