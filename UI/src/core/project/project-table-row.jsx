/* eslint-disable no-unused-vars */
import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { TableRow, TableCell, IconButton, Box, Typography, Tooltip } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import useAuth from 'src/hooks/use-auth';
import Iconify from 'src/components/iconify';
import Label from 'src/components/label';
import { mapProjectStatusIdToStatus, mapProjectTypeIdToType } from 'src/utils/utils';
import { canEditProject, getStatusColor } from './utils';
import { ProjectForm } from 'src/components/ui/project-form';
import useProjects from 'src/hooks/use-projects';
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

export default function ProjectTableRow({
  id,
  projectCode,
  projectName,
  projectType,
  projectManager,
  projectManagerId,
  startDate,
  endDate,
  teamSize,
  isEdited,
  projectStatus,
  fetchProjects,
}) {
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  const currentLoggedUserData = getUserDetails();
  const [open, setOpen] = useState(false);
  const [initialValues] = useState({});
  const { getProjectById, deleteProject } = useProjects();
  const confirm = useConfirm();

  const navigate = useNavigate();
  const handleClose = () => {
    setOpen(false);
    fetchProjects();
  };

  const getProject = (id) => {
    getProjectById.mutate(id, {
      onSuccess: (data) => {
        navigate('/projectManagement/projectList/editProject', {
          state: { project: data, id: id },
        });
      },
    });
  };

  const getProjectToView = (id) => {
    getProjectById.mutate(id, {
      onSuccess: (projectData) => {
        getAllocationData(id, projectData);
      },
    });
  };

  const getAllocationData = (id, projectData) => {
    navigate(`/projectManagement/projectList/projectDetails/${id}`, {
      state: { project: projectData, allocationData: id },
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
        handleDeleteProject(id);
      })
      .catch(() => {});
  };

  function handleDeleteProject(id) {
    deleteProject.mutate(id, {
      onSuccess: () => {
        fetchProjects();
        toast.success('Project deleted successfully');
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong!');
      },
    });
  }

  const dialogContent = {
    content: initialValues && (
      <div>
        <ProjectForm
          isEdit={true}
          formMode={'edit'}
          handleClose={handleClose}
          currentRole={currentLoggedUser}
          initialValues={initialValues}
          id={id}
        />
      </div>
    ),
    name: 'Edit Project',
    width: '820px',
  };

  return (
    <TableRow hover>
      <TableCell>
        <div className="break-words w-[10rem]">{projectCode}</div>
      </TableCell>
      <TableCell
        onClick={() => {
          getProjectToView(id);
        }}
        sx={{
          cursor: 'pointer',
          textDecoration: 'underline',
          color: 'primary.main',
          '&:hover': {
            color: 'primary.dark',
          },
        }}
      >
        <div className="break-words w-[10rem]">{projectName}</div>
      </TableCell>
      <TableCell>{mapProjectTypeIdToType(projectType)}</TableCell>
      <TableCell>{projectManager}</TableCell>
      <TableCell style={{ whiteSpace: 'nowrap' }}>{startDate}</TableCell>
      <TableCell style={{ whiteSpace: 'nowrap' }}>{endDate}</TableCell>
      <TableCell align="center">{teamSize}</TableCell>
      <TableCell align="center">
        <Label sx={{ width: 65 }} color={getStatusColor(projectStatus)}>
          {mapProjectStatusIdToStatus(projectStatus)}
        </Label>
      </TableCell>

      <TableCell align="right">
        <Box
          sx={{
            display: 'flex',
          }}
        >
          {/* view btn.  */}
          <Tooltip title={'View'}>
            <span>
              <IconButton
                onClick={() => {
                  getProjectToView(id);
                }}
                sx={{
                  backgroundHover: 'transparent',
                  '&:hover': {
                    backgroundColor: 'transparent',
                  },
                }}
              >
                <Iconify icon="solar:eye-bold" sx={{ mr: 0.5 }} />
              </IconButton>
            </span>
          </Tooltip>
          {/* edit_btn */}
          <Tooltip title={'Edit'}>
            <span>
              <IconButton
                disabled={canEditProject(
                  currentLoggedUser,
                  currentLoggedUserData.resourceId,
                  projectManagerId,
                  isEdited,
                  projectType
                )}
                onClick={() => {
                  getProject(id);
                }}
                sx={{
                  backgroundHover: 'transparent',
                  '&:hover': {
                    backgroundColor: 'transparent',
                  },
                }}
              >
                <Iconify icon="eva:edit-fill" sx={{ mr: 0.5 }} />
              </IconButton>
            </span>
          </Tooltip>
          <DialogBox content={dialogContent} open={open} handleClose={handleClose} />
          {/* delete btn.  */}
          <Tooltip title={'Delete'}>
            <span>
              <IconButton
                disabled={currentLoggedUser !== 3 || projectType === 2}
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

ProjectTableRow.propTypes = {
  id: PropTypes.any,
  projectCode: PropTypes.any,
  projectName: PropTypes.any,
  projectType: PropTypes.any,
  projectManager: PropTypes.any,
  projectManagerId: PropTypes.any,
  startDate: PropTypes.any,
  endDate: PropTypes.any,
  teamSize: PropTypes.any,
  isEdited: PropTypes.any,
  projectStatus: PropTypes.any,
  fetchProjects: PropTypes.any,
};
