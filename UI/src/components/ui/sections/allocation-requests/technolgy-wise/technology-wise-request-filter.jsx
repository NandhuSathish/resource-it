/* eslint-disable react/react-in-jsx-scope */
import { useState } from 'react';
import { Box, Stack, Button, Drawer, Divider, Typography, IconButton } from '@mui/material';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import { selectStyles } from 'src/utils/cssStyles';
import Iconify from 'src/components/iconify';
import useTechnologyWiseRequestQueryStore from 'src/components/ui/stores/technologyWiseRequestStore';
import { mapToOptions } from 'src/utils/utils';
import { resourceRequestApprovalStatusFilter } from 'src/utils/constants';
import useProjects from 'src/hooks/use-projects';
import useDepartment from 'src/hooks/use-department';
// ----------------------------------------------------------------------

const animatedComponents = makeAnimated();

export default function TechnologyWiseRequestFilters() {
  const setProjectFilter = useTechnologyWiseRequestQueryStore((s) => s.setProjectFilter);
  const setDepartmentFilter = useTechnologyWiseRequestQueryStore((s) => s.setDepartmentFilter);
  const setApprovalStatusFilters = useTechnologyWiseRequestQueryStore(
    (s) => s.setApprovalStatusFilters
  );
  const technologyWiseRequestFilters = useTechnologyWiseRequestQueryStore(
    (s) => s.technologyWiseRequestFilters
  );
  const setClearAllFilters = useTechnologyWiseRequestQueryStore((s) => s.setClearAllFilters);
  const { getProjectNameAndId } = useProjects();
  const [openFilter, setOpenFilter] = useState(false);

  //project type options.
  let projectOptions = [];
  const { data: projects } = getProjectNameAndId(false);
  if (projects) {
    projectOptions = mapToOptions(projects, 'projectId', 'projectCode', 'projectName');
  }
  // get the department options from the department api. and map it to the options.
  let departmentOptions = [];
  const { data: departments } = useDepartment();
  if (departments) {
    departmentOptions = mapToOptions(departments, 'departmentId', 'name');
  }
  //approvalStatusOption
  const approvalStatusOption = mapToOptions(resourceRequestApprovalStatusFilter, 'id', 'name');
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
          <div id="project" className="px-6 py-2">
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
                value={technologyWiseRequestFilters.departmentFilters}
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
                options={approvalStatusOption}
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
