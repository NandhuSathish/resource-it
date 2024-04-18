import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { TableRow, TableCell, IconButton, Box, Tooltip, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom/dist';
import Label from 'src/components/label';
import Iconify from 'src/components/iconify';
import {
  convertMonthsToYears,
  getProficency,
  mapAllocationStatusIdToStatus,
} from 'src/utils/utils';

import {
  confirmationButtonProps,
  cancelButtonProps,
  dialogProps,
} from 'src/utils/confirmationDialogProps';

import { canDelete, canEdit } from './utils';
import useAuth from 'src/hooks/use-auth';
import DialogBox from 'src/components/dialog';
import { AddResourceForm } from 'src/components/forms/addResource';
import useResources from 'src/hooks/useResources';
import { toast } from 'sonner';
import { errorCodeMap } from 'src/utils/error-codes';
import { useConfirm } from 'material-ui-confirm';

// ----------------------------------------------------------------------

const getStatusColor = (status) => {
  status = Number(status);
  let color;
  switch (status) {
    case 0:
      color = 'warning';
      break;
    case 1:
      color = 'primary';
      break;
    default:
      color = 'success';
  }
  return color;
};

export default function ResourceTableRow({
  id,
  empId,
  name,
  department,
  project,
  exp,
  role,
  skill,
  bench,
  status,
  state,
  handleFetch,
}) {
  const [open, setOpen] = useState(false);
  const [initialValues, setInitialValues] = useState({});
  const { getResourceById, deleteResource } = useResources();
  const confirm = useConfirm();
  const navigate = useNavigate();

  const handleClose = () => {
    setOpen(false);
  };

  const getResource = (id) => {
    getResourceById.mutate(id, {
      onSuccess: (data) => {
        setInitialValues(data);
        navigate('/resourceManagement/resourceList/editResource', {
          state: { resourceData: data },
        });
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
      //   onConfirm: handleConfirm,
    })
      .then(() => {
        handleDeleteResource(id);
      })
      .catch(() => {});
  };

  function handleDeleteResource(id) {
    deleteResource.mutate(id, {
      onSuccess: () => {
        toast.success('Resource deleted successfully');
        handleFetch();
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong!');
      },
    });
  }

  const dialogContent = {
    content: initialValues && (
      <div>
        <AddResourceForm
          isEdit={true}
          initialValues={initialValues}
          id={id}
          handleClose={handleClose}
          handleFetch={handleFetch}
        />
      </div>
    ),
    name: 'Edit Resource',
    width: '820px',
  };

  const { getUserDetails } = useAuth();
  return (
    <TableRow hover>
      <TableCell>{empId}</TableCell>

      <TableCell>{name}</TableCell>

      <TableCell>{department}</TableCell>
      <TableCell>
        {project && project !== 'null'
          ? project.split(',').map((item, index, arr) => (
              <div className="break-words w-[10rem]" key={item}>
                {item}
                {index !== arr.length - 1 ? ',' : ''} <br />
              </div>
            ))
          : ''}
      </TableCell>

      <TableCell align="center">
        {skill.map((s) => {
          return (
            <div
              key={s.skillName}
              style={{ display: 'block', whiteSpace: 'nowrap', textAlign: 'justify' }}
            >
              {s.skillName} : {convertMonthsToYears(s.experience)}y : {getProficency(s.proficiency)}
            </div>
          );
        })}
      </TableCell>

      <TableCell align="center">{exp}</TableCell>

      <TableCell align="center">{bench}</TableCell>

      <TableCell align="center">
        <Label color={getStatusColor(status)}>{mapAllocationStatusIdToStatus(status)}</Label>
      </TableCell>

      <TableCell align="right">
        <Box
          sx={{
            display: 'flex',
          }}
        >
          <Tooltip title={'Edit'}>
            <span>
              <IconButton
                onClick={() => {
                  getResource(id);
                }}
                disabled={!canEdit(getUserDetails(), state)}
                sx={{
                  backgroundHover: 'transparent',
                  '&:hover': { backgroundColor: 'transparent' },
                }}
              >
                <Iconify icon="eva:edit-fill" sx={{ mr: 0.5 }} />
              </IconButton>
            </span>
          </Tooltip>
          <DialogBox content={dialogContent} open={open} handleClose={handleClose} />
          <Tooltip title={'Delete'}>
            <span>
              <IconButton
                disabled={!canDelete(getUserDetails(), role, state)}
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

ResourceTableRow.propTypes = {
  id: PropTypes.any,
  role: PropTypes.any,
  empId: PropTypes.any,
  name: PropTypes.any,
  department: PropTypes.any,
  project: PropTypes.any,
  skill: PropTypes.any,
  exp: PropTypes.any,
  bench: PropTypes.any,
  status: PropTypes.string,
  state: PropTypes.any,
  handleFetch: PropTypes.any,
};
