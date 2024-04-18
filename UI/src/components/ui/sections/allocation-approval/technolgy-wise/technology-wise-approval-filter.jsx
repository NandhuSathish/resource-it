/* eslint-disable react/react-in-jsx-scope */
import { useState } from 'react';
import { Box, Stack, Button, Drawer, Divider, Typography, IconButton } from '@mui/material';
import Select from 'react-select';
import { selectStyles } from 'src/utils/cssStyles';
import makeAnimated from 'react-select/animated';

import Iconify from 'src/components/iconify';
import useTechnologyWiseApprovalQueryStore from 'src/components/ui/stores/technologyWiseApprovalStore';
import { mapToOptions } from 'src/utils/utils';
import { projectApprovalStatus } from 'src/utils/constants';
import useManager from 'src/hooks/use-manager';
import useProjects from 'src/hooks/use-projects';
import useDepartment from 'src/hooks/use-department';

// ----------------------------------------------------------------------

const animatedComponents = makeAnimated();

export default function TechnologyWiseApprovalFilters() {
  const { getProjectNameAndId } = useProjects();
  const setProjectFilter = useTechnologyWiseApprovalQueryStore((s) => s.setProjectFilter);
  const setManagerFilter = useTechnologyWiseApprovalQueryStore((s) => s.setManagerFilter);
  const setDepartmentFilter = useTechnologyWiseApprovalQueryStore((s) => s.setDepartmentFilter);
  const setClearAllFilters = useTechnologyWiseApprovalQueryStore((s) => s.setClearAllFilters);
  const technologyWiseRequestFilters = useTechnologyWiseApprovalQueryStore(
    (s) => s.technologyWiseRequestFilters
  );
  const setApprovalStatusFilters = useTechnologyWiseApprovalQueryStore(
    (s) => s.setApprovalStatusFilters
  );
  const [openFilter, setOpenFilter] = useState(false);

  const projectApprovalStatusOption = mapToOptions(projectApprovalStatus, 'id', 'name');
  // get the department options from the department api. and map it to the options.
  let managerOptions = [];
  const { data: managers } = useManager();
  if (managers) {
    managerOptions = mapToOptions(managers, 'id', 'name');
  }

  let projectOptions = [];
  const { data: projects } = getProjectNameAndId(false); //if i pass false it will return all the projects.if true it will return only the projects of current logged manager.
  if (projects) {
    projectOptions = mapToOptions(projects, 'projectId', 'projectCode', 'projectName');
  }

  //department options
  let departmentOptions = [];
  const { data: departments } = useDepartment();
  if (departments) {
    departmentOptions = mapToOptions(departments, 'departmentId', 'name');
  }

  // handle the filter bar  state.
  const handleOpenFilter = () => {
    setOpenFilter(true);
  };

  const handleCloseFilter = () => {
    setOpenFilter(false);
  };

  return (
    <>
      <Button
        disableRipple
        color="inherit"
        endIcon={<Iconify icon="ic:round-filter-list" />}
        onClick={handleOpenFilter}
      >
        <Typography variant="subtitle2">Filters&nbsp;</Typography>
      </Button>
      <Drawer
        anchor="right"
        open={openFilter}
        onClose={handleCloseFilter}
        PaperProps={{
          sx: { width: 360, border: 'none', overflow: 'auto' },
        }}
      >
        <Stack
          direction="row"
          alignItems="center"
          justifyContent="space-between"
          sx={{ px: 1, py: 2 }}
        >
          <Typography variant="h5" sx={{ ml: 1 }}>
            Filters
          </Typography>
          <IconButton onClick={handleCloseFilter}>
            <Iconify icon="eva:close-fill" />
          </IconButton>
        </Stack>
        <Divider />
        <div>
          <div id="projectName" className="px-6 py-2">
            <Box sx={{ display: 'flex', flexDirection: 'column' }}>
              <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
                Project Name
              </Typography>
              <Select
                styles={{
                  control: (provided) => ({
                    ...provided,
                    ...selectStyles,
                  }),
                }}
                value={technologyWiseRequestFilters.projectFilters}
                components={animatedComponents}
                closeMenuOnSelect={false}
                isMulti
                options={projectOptions}
                onChange={(selectedOptions) => {
                  setProjectFilter(selectedOptions);
                }}
              />
            </Box>
          </div>

          <div id="requestedBy" className="px-6 py-2">
            <Box sx={{ display: 'flex', flexDirection: 'column' }}>
              <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
                Requested By
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
                value={technologyWiseRequestFilters.managerFilters}
                options={managerOptions}
                onChange={(selectedOption) => {
                  setManagerFilter(selectedOption);
                }}
              />
            </Box>
          </div>

          <div id="department" className="px-6 py-2">
            <Box sx={{ display: 'flex', flexDirection: 'column' }}>
              <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
                Department
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
                // value={resourceRequestFilters.departmentFilters}
                options={departmentOptions}
                onChange={(selectedOption) => {
                  setDepartmentFilter(selectedOption);
                }}
              />
            </Box>
          </div>

          <div id="approvalStatus" className="px-6 py-2">
            <Box sx={{ display: 'flex', flexDirection: 'column' }}>
              <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
                Approval Status
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
                value={technologyWiseRequestFilters.approvalStatusFilters}
                options={projectApprovalStatusOption}
                onChange={(selectedOption) => {
                  setApprovalStatusFilters(selectedOption);
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
      </Drawer>
    </>
  );
}
