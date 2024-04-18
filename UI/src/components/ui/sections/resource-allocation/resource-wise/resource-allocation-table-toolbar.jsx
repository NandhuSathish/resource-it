/* eslint-disable react/react-in-jsx-scope */
import PropTypes from 'prop-types';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';

import { Box, Typography } from '@mui/material';
import { mapToOptions } from 'src/utils/utils';
import Select from 'react-select';
import ResourceAllocationFilters from './resource-allocation-filter';
import { useEffect, useState } from 'react';
import useProjects from 'src/hooks/use-projects';
import dayjs from 'dayjs';
import useResourceAllocationQueryStore from 'src/components/ui/stores/resourceAllocationStore';
import { getAllocationStartMinDate, getMaxDate } from '../utils';
import { toast } from 'sonner';
import { errorCodeMap } from 'src/utils/error-codes';
import { selectStyles } from 'src/utils/cssStyles';

import useAuth from 'src/hooks/use-auth';

// ----------------------------------------------------------------------

export default function ResourceAllocationTableToolbar({ handleProjectChange }) {
  const [projectSelectTrigger, setProjectSelectTrigger] = useState(0);
  const { getProjectNameAndId, getProjectById } = useProjects();
  const { getUserDetails } = useAuth();
  const { role } = getUserDetails();
  const projectQuery = useResourceAllocationQueryStore((s) => s.projectQuery);
  //query we used to send request to the api
  const allocationRequestQuery = useResourceAllocationQueryStore((s) => s.allocationRequestQuery);
  const setProjectId = useResourceAllocationQueryStore((s) => s.setProjectId);
  const [project, setProject] = useState(projectQuery.projectId);
  const setProjectStartDate = useResourceAllocationQueryStore((s) => s.setProjectStartDate);
  const setProjectEndDate = useResourceAllocationQueryStore((s) => s.setProjectEndDate);
  const setAllocationStartDate = useResourceAllocationQueryStore((s) => s.setAllocationStartDate);
  const setAllocationEndDate = useResourceAllocationQueryStore((s) => s.setAllocationEndDate);

  // Effect that triggers when the selected project changes
  useEffect(() => {
    handleProjectChange();
    const fetchProjectDetails = async () => {
      try {
        const data = await getProjectById.mutateAsync(project);
        setProjectStartDate(data.startDate);
        setProjectEndDate(data.endDate);
      } catch (error) {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
      }
    };
    if (project !== null) {
      fetchProjectDetails();
    }
  }, [project, projectSelectTrigger]);

  const handleProjectSelect = (selectedProject) => {
    setProject(selectedProject.value);
    setProjectId(selectedProject);
    setProjectSelectTrigger((prev) => prev + 1);
  };

  let projectOptions = [];
  const { data: projects } = getProjectNameAndId(!(role === 2 || role === 3 || role === 4));

  if (projects) {
    projectOptions = mapToOptions(projects, 'projectId', 'projectCode', 'projectName');
  }

  return (
    <Box
      sx={{
        height: 86,
        display: 'flex',
        paddingLeft: 0,
        pr: 0,
        justifyContent: 'space-between',
      }}
    >
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-evenly',
          alignItems: 'center',
        }}
      >
        <Box id="project" sx={{ paddingRight: 1, display: 'flex', flexDirection: 'column' }}>
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            Project Name
          </Typography>
          <Select
            styles={{
              control: (provided) => ({
                ...provided,
                ...selectStyles,
                width: '180px',
              }),
            }}
            value={
              projectQuery.projectId !== null
                ? projectOptions.find((option) => option.value == projectQuery.projectId.value)
                : null
            }
            closeMenuOnSelect={true}
            options={projectOptions}
            onChange={handleProjectSelect}
          />
        </Box>

        <Box
          sx={{
            paddingRight: 1,
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            Start Date
          </Typography>

          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
              format="DD/MM/YYYY"
              value={
                allocationRequestQuery.startDate ? dayjs(allocationRequestQuery.startDate) : null
              }
              minDate={
                projectQuery.projectStartDate
                  ? getAllocationStartMinDate(projectQuery.projectStartDate)
                  : null
              }
              maxDate={projectQuery.projectEndDate ? getMaxDate(projectQuery.projectEndDate) : null}
              onChange={(newDate) => {
                setAllocationStartDate(newDate);
              }}
              slotProps={{
                textField: {
                  readOnly: true,
                  //   placeholder: '',
                },
              }}
              disabled={allocationRequestQuery.resources.length > 0}
            />
          </LocalizationProvider>
        </Box>
        <Box
          sx={{
            paddingRight: 1,
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            End Date
          </Typography>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
              format="DD/MM/YYYY"
              value={allocationRequestQuery.endDate ? dayjs(allocationRequestQuery.endDate) : null}
              // setting the min date to the allocation start date
              minDate={
                allocationRequestQuery.startDate ? dayjs(allocationRequestQuery.startDate) : null
              }
              //   setting the max date to the project end date
              maxDate={projectQuery.projectEndDate ? dayjs(projectQuery.projectEndDate) : null}
              onChange={(date) => {
                setAllocationEndDate(date);
              }}
              slotProps={{
                textField: {
                  readOnly: true,
                },
              }}
              disabled={allocationRequestQuery.resources.length > 0}
            />
          </LocalizationProvider>
        </Box>
      </Box>

      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          paddingTop: 2,
        }}
      >
        <ResourceAllocationFilters />
      </Box>
    </Box>
  );
}

ResourceAllocationTableToolbar.propTypes = {
  numSelected: PropTypes.number,
  filterName: PropTypes.string,
  onFilterName: PropTypes.func,
  handleRequest: PropTypes.func,
  handleProjectChange: PropTypes.func,
};
