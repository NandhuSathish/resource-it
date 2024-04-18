/* eslint-disable no-unused-vars */
import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { TableRow, TableCell, IconButton, Box, Button, Typography } from '@mui/material';

import useAuth from 'src/hooks/use-auth';
import Iconify from 'src/components/iconify';
import { convertDate, mapAllocationTypeId } from 'src/utils/utils';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import useProjects from 'src/hooks/use-projects';
import dayjs from 'dayjs';
import { format } from 'date-fns';
import { isPast } from 'date-fns';
import { toast } from 'sonner';
import { errorCodeMap } from 'src/utils/error-codes';
import {
  confirmationButtonProps,
  cancelButtonProps,
  dialogProps,
} from 'src/utils/confirmationDialogProps';
import { useConfirm } from 'material-ui-confirm';
import Label from 'src/components/label';
import { isAfter, isWithinInterval, startOfDay, endOfDay } from 'date-fns';
import isSameOrBefore from 'dayjs/plugin/isSameOrBefore';
import customParseFormat from 'dayjs/plugin/customParseFormat';
import useConflictCheck from 'src/hooks/use-conflict-check';
import ConflictTable from 'src/components/conflict-table';
import Scrollbar from 'src/components/scrollbar';
dayjs.extend(customParseFormat);
dayjs.extend(isSameOrBefore);

// ----------------------------------------------------------------------

export default function ProjectAllocationRequestTableRow({
  id,
  resourceId,
  projectId,
  employeeId,
  requestedBy,
  startDate,
  endDate,
  remainingWorkingDays,
  resourceName,
  departmentName,
  band,
  isEdited,
  isRemoved,
  role,
  projectStartDate,
  projectEndDate,
  projectManagerId,
  projectType,
  handleFetch,
  handleDetailsUpdate,
  tabMode,
}) {
  const today = new Date();
    const { bulkConflictCheck } = useConflictCheck();
  let initialStartDate = startDate;
  let initialEndDate = endDate;
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  const currentUserData = getUserDetails();
  const [newStartDate, setNewStartDate] = useState(
    dayjs(convertDate(initialStartDate)).format('YYYY-MM-DD')
  );
  const [key, setKey] = useState(0);

  const [buttonDisabled, setButtonDisabled] = useState(true);
  const [newEndDate, setNewEndDate] = useState(
    dayjs(convertDate(initialEndDate)).format('YYYY-MM-DD')
  );
  const confirm = useConfirm();

  const { projectAllocationEdit, deleteAllocation } = useProjects();

  const handleRequestAllocation = (id, projectId, resourceId) => {
    const data = {
      allocationId: id,
      startDate: newStartDate,
      endDate: newEndDate,
      resourceId: resourceId,
      projectId: projectId,
    };

    if (dayjs(newStartDate).isSameOrBefore(dayjs(newEndDate))) {
      projectAllocationEdit.mutate(data, {
        onSuccess: () => {
          handleFetch();
          setKey((prevKey) => prevKey + 1);
          toast.success('Edit Request sent');
        },
        onError: (error) => {
          handleFetch();
          startDate = dayjs(convertDate(initialStartDate));
          endDate = dayjs(convertDate(initialEndDate));
          toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
        },
      });
    } else {
      toast.error('Start date must be before end date');
    }
  };

  startDate = convertDate(startDate);
  endDate = convertDate(endDate);
  projectStartDate = convertDate(projectStartDate);
  projectEndDate = convertDate(projectEndDate);
  const handleStartDateChange = (date) => {
    setNewStartDate(date);
    setButtonDisabled(false);
  };

  const handleEndDateChange = (date) => {
    setNewEndDate(date);
    setButtonDisabled(false);
  };

  const handleApprove = (id, projectId, resourceId) => {
    const data = {
      allocationId: id,
      resourceId: resourceId,
      projectId: projectId,
      allocationStartDate: newStartDate,
      allocationEndDate: newEndDate,
    };
    //check for conflicts
    bulkConflictCheck.mutate(data, {
      onSuccess: (data) => {
        if (data.length > 0) {
          handleConflict(data);
        } else {
          handleRequestAllocation(id, projectId, resourceId);
        }
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
      },
    });
  };


    const handleConflict = (data) => {
      handleFetch();
      startDate = dayjs(convertDate(initialStartDate));
      endDate = dayjs(convertDate(initialEndDate));
      setKey((prevKey) => prevKey + 1);
      setButtonDisabled(true);

      confirm({
        title: (
          <Typography variant="h6" style={{ fontSize: '18px' }}>
            Conflicts Found
          </Typography>
        ),
        description: (
          <Scrollbar>
            <ConflictTable data={data} />
          </Scrollbar>
        ),
        confirmationButtonProps: {
          ...confirmationButtonProps,
        },
        dialogProps: {
          ...dialogProps,
        },
        cancellationButtonProps: {
          style: {
            visibility: 'hidden',
          },
        },
      })
        .then(() => {
          return;
        })
        .catch(() => {
          return;
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
        handleDeleteAllocation(id);
      })
      .catch(() => {});
  };

  const handleDeleteAllocation = (id) => {
    deleteAllocation.mutate(id, {
      onSuccess: () => {
        toast.success('Allocation deleted successfully');
        handleFetch();
        setTimeout(handleDetailsUpdate, 2000); // 2000 milliseconds = 2 seconds
      },
      onError: (error) => {
        startDate = dayjs(convertDate(initialStartDate));
        endDate = dayjs(convertDate(initialEndDate));
        handleFetch();
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
      },
    });
  };

  return (
    <TableRow hover>
      <TableCell>{employeeId}</TableCell>
      <TableCell style={{ whiteSpace: 'nowrap' }}>{resourceName}</TableCell>
      <TableCell>{departmentName}</TableCell>
      <TableCell>{requestedBy}</TableCell>
      <TableCell>
        <LocalizationProvider dateAdapter={AdapterDayjs} key={key}>
          <DatePicker
            format="DD/MM/YYYY"
            sx={{
              width: 160,
            }}
            slotProps={{
              textField: {
                error: false,
              },
            }} //to hold the error state
            value={dayjs(startDate)}
            minDate={
              currentLoggedUser == 4 || currentLoggedUser == 5
                ? dayjs(today)
                : dayjs(projectStartDate)
            }
            disabled={
              (!(currentUserData.resourceId == projectManagerId) &&
                !(currentLoggedUser === 2 || currentLoggedUser === 3)) ||
              isPast(new Date(projectEndDate)) ||
              resourceId === projectManagerId ||
              isRemoved == 1 ||
              [2, 3, 5].includes(parseInt(role)) ||
              isEdited == 1 ||
              (!(currentLoggedUser === 2 || currentLoggedUser === 3) &&
                isAfter(today, endDate.toDate()))
            }
            onChange={(date) => handleStartDateChange(format(date.toDate(), 'yyyy-MM-dd'))}
          />
        </LocalizationProvider>
      </TableCell>
      <TableCell>
        <LocalizationProvider dateAdapter={AdapterDayjs} key={key}>
          <DatePicker
            format="DD/MM/YYYY"
            sx={{
              width: 160,
            }}

            slotProps={{
              textField: {
                error: false,
              },
            }} //to hold the error state
            
            minDate={dayjs(newStartDate)}
            maxDate={dayjs(projectEndDate)}
            disabled={
              (!(currentUserData.resourceId == projectManagerId) &&
                !(currentLoggedUser === 2 || currentLoggedUser === 3)) ||
              isPast(new Date(projectEndDate)) ||
              resourceId === projectManagerId ||
              isRemoved == 1 ||
              [2, 3, 5].includes(parseInt(role)) ||
              isEdited == 1 ||
              (!(currentLoggedUser === 2 || currentLoggedUser === 3) &&
                isAfter(today, endDate.toDate()))
            }
            value={endDate}
            onChange={(date) => handleEndDateChange(format(date.toDate(), 'yyyy-MM-dd'))}
          />
        </LocalizationProvider>
      </TableCell>
      {tabMode == 1 && <TableCell align="center">{remainingWorkingDays} </TableCell>}
      <TableCell align="right">
        <Box
          sx={{
            display: 'flex',
          }}
        >
          {/* request btn.  */}
          <IconButton
            disabled={
              buttonDisabled ||
              resourceId === projectManagerId ||
              isRemoved == 1 ||
              [2, 3, 5].includes(parseInt(role)) ||
              isEdited == 1
            }
            onClick={() => {
              handleApprove(id, projectId, resourceId);
            }}
            color="primary"
            sx={{
              backgroundHover: 'transparent',
              '&:hover': {
                backgroundColor: 'transparent',
              },
            }}
          >
            <Iconify icon="streamline:send-email-solid" />
          </IconButton>
          {/* delete btn.  */}
          {/* {projectType !== 2 && ( */}
          <IconButton
            onClick={() => {
              handleDeleteClick(id);
            }}
            color="error"
            disabled={
              (!(currentUserData.resourceId == projectManagerId) &&
                !(currentLoggedUser === 2 || currentLoggedUser === 3)) ||
              (resourceId == projectManagerId &&
                !(currentLoggedUser === 2 || currentLoggedUser === 3)) ||
              resourceId == projectManagerId ||
              isPast(
                new Date(startDate) && !(currentLoggedUser === 2 || currentLoggedUser === 3)
              ) ||
              isRemoved == 1 ||
              [2, 3, 5].includes(parseInt(role)) ||
              (!(currentLoggedUser === 2 || currentLoggedUser === 3) &&
                isWithinInterval(today, {
                  start: startOfDay(startDate.toDate()),
                  end: endOfDay(endDate.toDate()),
                })) ||
              (!(currentLoggedUser === 2 || currentLoggedUser === 3) &&
                isAfter(today, endDate.toDate()))
            }
            sx={{
              backgroundHover: 'transparent',
              '&:hover': {
                backgroundColor: 'transparent',
              },
            }}
          >
            <Iconify icon="eva:trash-2-outline" sx={{ mr: 0.5 }} />
          </IconButton>
          {/* )} */}
        </Box>
      </TableCell>
    </TableRow>
  );
}

ProjectAllocationRequestTableRow.propTypes = {
  id: PropTypes.any,
  resourceId: PropTypes.any,
  allocationType: PropTypes.any,
  projectId: PropTypes.any,
  employeeId: PropTypes.any,
  resourceName: PropTypes.any,
  requestedBy: PropTypes.any,
  departmentName: PropTypes.any,
  band: PropTypes.any,
  isEdited: PropTypes.any,
  isRemoved: PropTypes.any,
  role: PropTypes.any,
  startDate: PropTypes.any,
  endDate: PropTypes.any,
  remainingWorkingDays: PropTypes.any,
  projectStartDate: PropTypes.any,
  projectEndDate: PropTypes.any,
  projectManagerId: PropTypes.any,
  projectType: PropTypes.any,
  handleFetch: PropTypes.any,
  handleDetailsUpdate: PropTypes.any,
  tabMode: PropTypes.any,
};
