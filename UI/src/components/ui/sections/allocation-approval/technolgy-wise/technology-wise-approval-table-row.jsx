/* eslint-disable no-unused-vars */
import React from 'react';
import PropTypes from 'prop-types';
import { TableRow, TableCell, IconButton, Box, Tooltip } from '@mui/material';
import useAuth from 'src/hooks/use-auth';
import Iconify from 'src/components/iconify';
import { getTechnologyApprovalStatus } from '../utils';
import Label from 'src/components/label';
import { getApprovalColor } from 'src/components/ui/sections/allocation-requests/utils';
import { getProficency } from 'src/utils/utils';

// ----------------------------------------------------------------------

export default function TechnologyWiseApprovalTableRow({
  id,
  projectId,
  projectCode,
  projectName,
  managerName,
  department,
  skill,
  experience,
  count,
  allocationType,
  startDate,
  endDate,
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
        <Box
          sx={{
            width: {
              xs: '100px', // width on extra-small screens
              sm: '150px', // width on small screens
              md: '200px', // width on medium screens
              lg: '88px', // width on large screens
              xl: '140px', // width on extra-large screens
            },
          }}
        >
          <div className="break-words">{projectCode}</div>
        </Box>
      </TableCell>
      <TableCell>
        <Box
          sx={{
            width: {
              xs: '100px', // width on extra-small screens
              sm: '150px', // width on small screens
              md: '200px', // width on medium screens
              lg: '92px', // width on large screens
              xl: '140px', // width on extra-large screens
            },
          }}
        >
          <div className="break-words">{projectName}</div>
        </Box>
      </TableCell>
      <TableCell>{managerName}</TableCell>
      <TableCell>{department}</TableCell>
      <TableCell>
        {skill.map((s) => {
          return (
            <Tooltip
              key={s.skillId}
              title={`${s.name} : ${s.skillMinValue}-${s.skillMaxValue} Y : ${s.proficiency
                .map((proficiency) => getProficency(proficiency))
                .join(', ')}`}
            >
              <Box
                key={s.skillId}
                style={{
                  display: 'block',
                  whiteSpace: 'nowrap',
                  textAlign: 'justify',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                }}
                sx={{
                  width: {
                    xs: '100px', // width on extra-small screens
                    sm: '150px', // width on small screens
                    md: '200px', // width on medium screens
                    lg: '158px', // width on large screens
                    xl: '210px', // width on extra-large screens
                  },
                }}
              >
                {s.name} : {s.skillMinValue}-{s.skillMaxValue} Y :{' '}
                {s.proficiency.map((proficiency) => getProficency(proficiency)).join(', ')}
              </Box>
            </Tooltip>
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
        <span style={{ whiteSpace: 'nowrap' }}>{endDate}</span>
      </TableCell>
      <TableCell>
        <Label
          sx={{ width: 65 }}
          color={getApprovalColor(getTechnologyApprovalStatus(currentLoggedUser, approvalStatus))}
        >
          {getTechnologyApprovalStatus(currentLoggedUser, approvalStatus)}
        </Label>
      </TableCell>
      <TableCell align="right">
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'center',
            width: {
              xs: '100px', // width on extra-small screens
              sm: '150px', // width on small screens
              md: '200px', // width on medium screens
              lg: '60px', // width on large screens
              xl: '140px', // width on extra-large screens
            },
          }}
        >
          {/* approve btn.  */}
          <Tooltip title="Approve">
            <IconButton
              onClick={() => {
                onApprove({
                  id: id,
                  projectId: projectId,
                  startDate: startDate,
                  endDate: endDate,
                  allocationId: allocationId,
                });
              }}
              sx={{
                backgroundHover: 'transparent',
                '&:hover': {
                  backgroundColor: 'transparent',
                },
              }}
              color="success"
              disabled={
                getTechnologyApprovalStatus(currentLoggedUser, approvalStatus) !== 'Pending'
              }
            >
              <Iconify icon="charm:circle-tick" sx={{ mr: 0.5 }} />
            </IconButton>
          </Tooltip>
          {/* reject btn.  */}
          <Tooltip title="Reject">
            <IconButton
              onClick={() => {
                onReject(id);
              }}
              color="error"
              sx={{
                backgroundHover: 'transparent',
                '&:hover': {
                  backgroundColor: 'transparent',
                },
              }}
              disabled={
                getTechnologyApprovalStatus(currentLoggedUser, approvalStatus) !== 'Pending'
              }
            >
              <Iconify icon="charm:circle-cross" sx={{ mr: 0.5 }} />
            </IconButton>
          </Tooltip>
        </Box>
      </TableCell>
    </TableRow>
  );
}

TechnologyWiseApprovalTableRow.propTypes = {
  id: PropTypes.any,
  projectCode: PropTypes.any,
  projectId: PropTypes.any,
  projectName: PropTypes.any,
  department: PropTypes.any,
  skill: PropTypes.any,
  experience: PropTypes.any,
  count: PropTypes.any,
  allocationType: PropTypes.any,
  startDate: PropTypes.any,
  endDate: PropTypes.any,
  approvalStatus: PropTypes.any,
  allocationId: PropTypes.any,
  managerName: PropTypes.any,
  onApprove: PropTypes.func,
  onReject: PropTypes.func,
};
