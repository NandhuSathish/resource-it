/* eslint-disable no-unused-vars */
import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { TableRow, TableCell, IconButton, Box, Typography, Tooltip } from '@mui/material';

import useAuth from 'src/hooks/use-auth';
import Iconify from 'src/components/iconify';
import { mapProjectRequestStatusIdToStatus, mapProjectTypeIdToType } from 'src/utils/utils';
import { getStatusColor } from './utils';
import Label from 'src/components/label';
import useProjectRequests from 'src/hooks/use-project-requests';
import { ProjectForm } from 'src/components/ui/project-form';
import DialogBox from 'src/components/dialog';
import {
  confirmationButtonProps,
  cancelButtonProps,
  dialogProps,
} from 'src/utils/confirmationDialogProps';
import { useConfirm } from 'material-ui-confirm';
import { toast } from 'sonner';
import { errorCodeMap } from 'src/utils/error-codes';

// ----------------------------------------------------------------------

export default function ProjectRequestTableRow({
  id,
  projectCode,
  project,
  projectName,
  projectType,
  projectManager,
  startDate,
  endDate,
  estimatedDays,
  approvalStatus,
  editedFields,
  fetch,
  approveEditedProjectFunction,
}) {
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  const [open, setOpen] = useState(false);
  const [initialValues, setInitialValues] = useState({});
  const { getRequestById, approveEditedProject, deleteProjectRequest } = useProjectRequests();
  const { projectRequestReject } = useProjectRequests();
  const confirm = useConfirm();
  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    fetch();
  };

  const getProjectToApprove = (id) => {
    getRequestById.mutate(id, {
      onSuccess: (data) => {
        setInitialValues(data);
        handleOpen();
      },
    });
  };

  const approveProject = (id, editedFields) => {
    const requests = {
      status: 1,
      projectRequestId: id,
    };

    if (editedFields.includes('startDate') || editedFields.includes('endDate')) {
      confirm({
        title: (
          <Typography variant="h6" style={{ fontSize: '18px' }}>
            Confirmation
          </Typography>
        ),
        description: (
          <Typography variant="body1" style={{ fontSize: '16px' }}>
            *This may result in the removal of allocations inside the project and allocation
            requests.
          </Typography>
        ),
        confirmationButtonProps: {
          ...confirmationButtonProps,
        },
        cancelButtonProps: { ...cancelButtonProps },
        dialogProps: {
          ...dialogProps,
        },
      })
        .then(() => {
          approveEditedProjectFunction(requests, fetch);
        })
        .catch(() => {
          console.log('not confirmed');
        });
    } else {
      approveEditedProjectFunction(requests, fetch);
    }
  };

  const rejectProject = (id) => {
    // if (editedFields.includes('startDate') || editedFields.includes('endDate')) {
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
    })
      .then(() => {
        requestReject(id);
      })
      .catch(() => {
        console.log('not confirmed');
      });
  };

  const requestReject = (id) => {
    const requests = {
      status: 2,
      projectRequestId: id,
    };
    projectRequestReject.mutate(requests, {
      onSuccess: (data) => {
        toast.success('Project request rejected successfully');
        fetch();
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
        fetch();
      },
    });
  };

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
    })
      .then(() => {
        handleDeleteRequest(id);
      })
      .catch(() => {});
  };

  function handleDeleteRequest(id) {
    deleteProjectRequest.mutate(id, {
      onSuccess: () => {
        fetch();
        toast.success('Project request deleted successfully');
      },
      onError: () => {
        toast.error('Deletion is restricted as the request is currently under process.');
      },
    });
  }

  const dialogContent = {
    content: initialValues && (
      <div>
        <ProjectForm
          isEdit={true}
          formMode={'approve'}
          handleClose={handleClose}
          currentRole={currentLoggedUser}
          initialValues={initialValues}
          id={id}
        />
      </div>
    ),
    name: 'Approve Project',
    width: '820px',
  };

  return (
    <TableRow hover>
      <TableCell>
        <div className="break-words w-[10rem]">{projectCode}</div>
      </TableCell>
      <TableCell>
        <div className="break-words w-[10rem]">{projectName}</div>
      </TableCell>
      <TableCell>{mapProjectTypeIdToType(projectType)}</TableCell>
      <TableCell>{projectManager}</TableCell>
      <TableCell>
        <Typography
          variant="gridCell"
          color={editedFields.includes('startDate') ? 'primary' : 'inherit'}
        >
          <span style={{ whiteSpace: 'nowrap' }}>{startDate}</span>
        </Typography>
      </TableCell>
      <TableCell>
        <Typography
          variant="gridCell"
          color={editedFields.includes('endDate') ? 'primary' : 'inherit'}
        >
          <span style={{ whiteSpace: 'nowrap' }}> {endDate}</span>
        </Typography>
      </TableCell>
      <TableCell align="center">
        <Typography
          variant="gridCell"
          color={editedFields.includes('manDay') ? 'primary' : 'inherit'}
        >
          {estimatedDays}
        </Typography>
      </TableCell>

      <TableCell align="center">
        <Label sx={{ width: 65 }} color={getStatusColor(approvalStatus)}>
          {mapProjectRequestStatusIdToStatus(approvalStatus)}
        </Label>
      </TableCell>

      {currentLoggedUser === 2 ? (
        <TableCell align="right">
          {approvalStatus == 0 && (
            <Box
              sx={{
                display: 'flex',
              }}
            >
              {project != null ? (
                <Tooltip title={'Approve'}>
                  <span>
                    <IconButton
                      onClick={() => {
                        console.log('tiytiuyoiu');
                        approveProject(id, editedFields);
                      }}
                      sx={{
                        backgroundHover: 'transparent',
                        '&:hover': {
                          backgroundColor: 'transparent',
                        },
                      }}
                      color="success"
                    >
                      <Iconify icon="charm:circle-tick" sx={{ mr: 0.5 }} />
                    </IconButton>
                  </span>
                </Tooltip>
              ) : (
                <>
                  <Tooltip title={'Approve'}>
                    <span>
                      <IconButton
                        onClick={() => {
                          getProjectToApprove(id);
                        }}
                        sx={{
                          backgroundHover: 'transparent',
                          '&:hover': {
                            backgroundColor: 'transparent',
                          },
                        }}
                        color="success"
                      >
                        <Iconify icon="charm:circle-tick" sx={{ mr: 0.5 }} />
                      </IconButton>
                    </span>
                  </Tooltip>
                  <DialogBox content={dialogContent} open={open} handleClose={handleClose} />
                </>
              )}

              {/* reject btn.  */}
              <Tooltip title={'Reject'}>
                <span>
                  <IconButton
                    onClick={() => {
                      rejectProject(id);
                    }}
                    color="error"
                    sx={{
                      backgroundHover: 'transparent',
                      '&:hover': {
                        backgroundColor: 'transparent',
                      },
                    }}
                  >
                    <Iconify icon="charm:circle-cross" sx={{ mr: 0.5 }} />
                  </IconButton>
                </span>
              </Tooltip>
              {/* Delete btn.  */}
            </Box>
          )}
        </TableCell>
      ) : (
        <TableCell>
          {' '}
          {(currentLoggedUser === 3 || currentLoggedUser === 5) && (
            <Box>
              <Tooltip title={'Delete'}>
                <span>
                  <IconButton
                    disabled={!approvalStatus == 0}
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
          )}{' '}
        </TableCell>
      )}
    </TableRow>
  );
}

ProjectRequestTableRow.propTypes = {
  id: PropTypes.any,
  project: PropTypes.any,
  projectCode: PropTypes.any,
  projectName: PropTypes.any,
  projectType: PropTypes.any,
  projectManager: PropTypes.any,
  startDate: PropTypes.any,
  endDate: PropTypes.any,
  estimatedDays: PropTypes.any,
  approvalStatus: PropTypes.any,
  editedFields: PropTypes.array,
  fetch: PropTypes.any,
  approveEditedProjectFunction: PropTypes.any,
};
