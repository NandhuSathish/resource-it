/* eslint-disable react/react-in-jsx-scope */
import { useState } from 'react';
import { Box, Button, Typography } from '@mui/material';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import useManager from 'src/hooks/use-manager';
import Iconify from 'src/components/iconify';
import useProjectQueryStore from 'src/components/ui/stores/projectStore';
import { mapManagerToOptions, mapToOptions } from 'src/utils/utils';
import { selectStyles } from 'src/utils/cssStyles';
import { projectStatus, projectType } from 'src/utils/constants';
import { FilterDrawer } from 'src/components/ui/filter-drawer';
import useAuth from 'src/hooks/use-auth';
// ----------------------------------------------------------------------
const animatedComponents = makeAnimated();

export default function ProjectFilters() {
  const setProjectTypeFilter = useProjectQueryStore((s) => s.setProjectTypeFilter);
  const setManagerFilter = useProjectQueryStore((s) => s.setManagerFilter);
  const setStartDateFilter = useProjectQueryStore((s) => s.setStartDateFilter);
  const setEndDateFilter = useProjectQueryStore((s) => s.setEndDateFilter);
  const setProjectStatusFilter = useProjectQueryStore((s) => s.setProjectStatusFilter);
  const projectFilters = useProjectQueryStore((s) => s.projectFilters);
  const setClearAllFilters = useProjectQueryStore((s) => s.setClearAllFilters);
  const [openFilter, setOpenFilter] = useState(false);
  const { getUserDetails } = useAuth();
  const { resourceId } = getUserDetails();

  //project type options.
  const projectTypeOptions = mapToOptions(projectType, 'id', 'name');
  //band options
  const projectStatusOption = mapToOptions(projectStatus, 'id', 'name');
  // get the department options from the department api. and map it to the options.
  let managerOptions = [];
  const { data: managers } = useManager();
  if (managers) {
    managerOptions = mapManagerToOptions(managers, 'id', 'name', resourceId);
  }
  // handle the filter bar  state.
  const handleOpenFilter = () => {
    setOpenFilter(true);
  };
  const handleCloseFilter = () => {
    setOpenFilter(false);
  };

  const dateStyles = {
    height: '45px',
  };

  return (
    <FilterDrawer
      openFilter={openFilter}
      handleOpenFilter={handleOpenFilter}
      handleCloseFilter={handleCloseFilter}
      title="Filters"
      icon="ic:round-filter-list"
    >
      <div>
        <div id="projectType" className="px-6 py-2">
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
              Project Type
            </Typography>
            <Select
              styles={{
                control: (provided) => ({
                  ...provided,
                  ...selectStyles,
                }),
              }}
              value={projectFilters.projectTypeFilters}
              components={animatedComponents}
              closeMenuOnSelect={false}
              isMulti
              options={projectTypeOptions}
              onChange={(selectedOptions) => {
                setProjectTypeFilter(selectedOptions);
              }}
            />
          </Box>
        </div>

        <div id="projectManager" className="px-6 py-2">
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
              Project Manager
            </Typography>
            <Select
              styles={{
                control: (provided) => ({
                  ...provided,
                  ...selectStyles,
                }),
              }}
              components={animatedComponents}
              closeMenuOnSelect={false}
              isMulti
              value={projectFilters.managerFilters}
              options={managerOptions}
              onChange={(selectedOption) => {
                setManagerFilter(selectedOption);
              }}
            />
          </Box>
        </div>

        <div id="startDate" className="px-6 py-2">
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
              Start Date
            </Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                format="DD/MM/YYYY"
                sx={dateStyles}
                value={
                  projectFilters.startDateFilters ? dayjs(projectFilters.startDateFilters) : null
                }
                onChange={(newValue) => {
                  setStartDateFilter(newValue ? newValue.toDate() : null);
                }}
                slotProps={{
                  textField: {
                    readOnly: true,
                  },
                }}
              />
            </LocalizationProvider>
          </Box>
        </div>

        <div id="endDate" className="px-6 py-2">
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
              End Date
            </Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                format="DD/MM/YYYY"
                sx={dateStyles}
                value={projectFilters.endDateFilters ? dayjs(projectFilters.endDateFilters) : null}
                onChange={(newValue) => {
                  setEndDateFilter(newValue ? newValue.toDate() : null);
                }}
                slotProps={{
                  textField: {
                    readOnly: true,
                  },
                }}
              />
            </LocalizationProvider>
          </Box>
        </div>

        <div id="projectStatus" className="px-6 py-2">
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
              Project Status
            </Typography>
            <Select
              styles={{
                control: (provided) => ({
                  ...provided,
                  ...selectStyles,
                }),
              }}
              components={animatedComponents}
              closeMenuOnSelect={false}
              isMulti
              value={projectFilters.projectStatusFilters}
              options={projectStatusOption}
              onChange={(selectedOption) => {
                setProjectStatusFilter(selectedOption);
              }}
            />
          </Box>
        </div>
      </div>

      <Box sx={{ p: 3 }}>
        <Button
          fullWidth
          size="large"
          type="submit"
          color="inherit"
          variant="outlined"
          startIcon={<Iconify icon="ic:round-clear-all" />}
          onClick={() => setClearAllFilters()}
        >
          Clear All
        </Button>
      </Box>
    </FilterDrawer>
  );
}
