/* eslint-disable no-unused-vars */
import React from 'react';
import PropTypes from 'prop-types';
import { TableRow, TableCell, IconButton, Box, Typography, Tooltip } from '@mui/material';
import { useConfirm } from 'material-ui-confirm';
import useAuth from 'src/hooks/use-auth';
import Iconify from 'src/components/iconify';
import {
  confirmationButtonProps,
  cancelButtonProps,
  dialogProps,
} from 'src/utils/confirmationDialogProps';
import Label from 'src/components/label';
import { getApprovalColor, getApprovalStatusOfTechnologyWiseRequestList } from '../utils';
import { getProficency } from 'src/utils/utils';

// ----------------------------------------------------------------------

export default function TechnologyWiseRequestTableRow({
  id,
  projectCode,
  projectName,
  department,
  skill,
  experience,
  count,
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
      .catch(() => {});
  };

  return (
    <TableRow hover>
      <TableCell className="break-all">{projectCode}</TableCell>
      <TableCell className="break-all">{projectName}</TableCell>
      <TableCell>{department}</TableCell>
      <TableCell>
        {skill.map((s) => {
          return (
            <div
              key={s.skillId}
              style={{ display: 'block', whiteSpace: 'nowrap', textAlign: 'justify' }}
            >
              {s.name} : {s.skillMinValue}-{s.skillMaxValue} Y :
              <Tooltip
                title={s.proficiency.map((proficiency) => getProficency(proficiency)).join(', ')}
              >
                <span>
                  {s.proficiency.length > 1
                    ? `${getProficency(s.proficiency[0])}...`
                    : getProficency(s.proficiency[0])}
                </span>
              </Tooltip>
            </div>
          );
        })}
      </TableCell>
      <TableCell align="center">{experience} Years</TableCell>
      <TableCell align="center">{count}</TableCell>
      <TableCell align="center">
        <span style={{ whiteSpace: 'nowrap' }}>
          {startDate} <br />
        </span>
        to <br />
        <span style={{ whiteSpace: 'nowrap' }}> {endDate} </span>
      </TableCell>
      <TableCell align="center">
        <Label
          sx={{ width: 65 }}
          color={getApprovalColor(
            getApprovalStatusOfTechnologyWiseRequestList(currentLoggedUser, approvalStatus)
          )}
        >
          {getApprovalStatusOfTechnologyWiseRequestList(currentLoggedUser, approvalStatus)}
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
            <IconButton
              disabled={
                getApprovalStatusOfTechnologyWiseRequestList(currentLoggedUser, approvalStatus) !==
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
          </Tooltip>
        </Box>
      </TableCell>
    </TableRow>
  );
}

TechnologyWiseRequestTableRow.propTypes = {
  id: PropTypes.any,
  projectCode: PropTypes.any,
  projectName: PropTypes.any,
  department: PropTypes.any,
  skill: PropTypes.any,
  experience: PropTypes.any,
  count: PropTypes.any,
  startDate: PropTypes.any,
  endDate: PropTypes.any,
  approvalStatus: PropTypes.any,
  onDelete: PropTypes.func,
};
