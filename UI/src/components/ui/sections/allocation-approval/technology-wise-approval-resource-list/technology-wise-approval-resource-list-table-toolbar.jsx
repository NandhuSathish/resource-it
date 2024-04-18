/* eslint-disable no-unused-vars */
/* eslint-disable react/react-in-jsx-scope */
import PropTypes from 'prop-types';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import Iconify from 'src/components/iconify';
import { Box, Button, TextField, Typography } from '@mui/material';
import { mapToOptions } from 'src/utils/utils';
import Select from 'react-select';
import { selectStyles } from 'src/utils/cssStyles';
import useProjects from 'src/hooks/use-projects';
import dayjs from 'dayjs';
import TechnologyWiseApprovalResourceListFilters from './technology-wise-approval-resource-list-filter';
import useAllocationApprovalResourceQueryStore from 'src/components/ui/stores/allocationApprovalResourceStore';
// ----------------------------------------------------------------------
const breakpoints = [480, 768, 1600];
const mediaQuerySelect = breakpoints.map((bp) => `@media (min-width: ${bp}px)`);
export default function TechnologyWiseApprovalResourceListTableToolbar() {
  const { getProjectNameAndId } = useProjects();
  const requestDetails = useAllocationApprovalResourceQueryStore((s) => s.requestDetails);

  let projectOptions = [];
  const { data: projects } = getProjectNameAndId(false);
  if (projects) {
    projectOptions = mapToOptions(projects, 'projectId', 'projectName');
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
            Project
          </Typography>
          <Select
            styles={{
              control: (provided) => ({
                ...provided,
                ...selectStyles,
                width: '180px',
              }),
            }}
            value={projectOptions.find((option) => option.value === requestDetails.projectId)}
            closeMenuOnSelect={true}
            options={projectOptions}
            isSearchable={false}
            isClearable={false}
            menuIsOpen={false}
          />
        </Box>
        <Box sx={{ paddingRight: 1, display: 'flex', flexDirection: 'column' }}>
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            Start Date
          </Typography>

          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
              format="DD/MM/YYYY"
              readOnly={true}
              value={requestDetails.startDate ? dayjs(requestDetails.startDate) : null}
              renderInput={(props) => (
                <TextField
                  {...props}
                  sx={{
                    padding: 0,
                  }}
                />
              )}
              // other props...
            />
          </LocalizationProvider>
        </Box>
        <Box sx={{ paddingRight: 1, display: 'flex', flexDirection: 'column' }}>
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            End Date
          </Typography>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
              format="DD/MM/YYYY"
              readOnly={true}
              value={requestDetails.endDate ? dayjs(requestDetails.endDate) : null}
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
        <TechnologyWiseApprovalResourceListFilters />
      </Box>
    </Box>
  );
}

TechnologyWiseApprovalResourceListTableToolbar.propTypes = {
  numSelected: PropTypes.number,
  filterName: PropTypes.string,
  onFilterName: PropTypes.func,
  handleRequest: PropTypes.func,
};
