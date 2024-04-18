/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import {
  Box,
  Stack,
  Button,
  Drawer,
  Divider,
  Typography,
  IconButton,
  OutlinedInput,
} from '@mui/material';
import Select from 'react-select';
import { selectStyles } from 'src/utils/cssStyles';
import makeAnimated from 'react-select/animated';
import Iconify from 'src/components/iconify';
import useResourceApprovalQueryStore from 'src/components/ui/stores/resourceApprovalStore';
import { mapToOptions } from 'src/utils/utils';
import { projectApprovalStatus } from 'src/utils/constants';
import useDepartment from 'src/hooks/use-department';
import useProjects from 'src/hooks/use-projects';
import useManager from 'src/hooks/use-manager';
// ----------------------------------------------------------------------
const animatedComponents = makeAnimated();

export default function ResourceWiseApprovalFilters() {
  const setProjectFilter = useResourceApprovalQueryStore((s) => s.setProjectFilter);
  const setProjectManagerFilter = useResourceApprovalQueryStore((s) => s.setProjectManagerFilter);
  const setApprovalStatusFilters = useResourceApprovalQueryStore((s) => s.setApprovalStatusFilters);
  const setDepartmentFilter = useResourceApprovalQueryStore((s) => s.setDepartmentFilter);
  const setClearAllFilters = useResourceApprovalQueryStore((s) => s.setClearAllFilters);
  const setResourceName = useResourceApprovalQueryStore((s) => s.setSearchText);
  const resourceRequestFilters = useResourceApprovalQueryStore((s) => s.resourceRequestFilters);
  const resourceRequestQuery = useResourceApprovalQueryStore((s) => s.resourceRequestQuery);
  const [openFilter, setOpenFilter] = useState(false);
  // eslint-disable-next-line no-unused-vars
  const [filterName, setFilterName] = useState(
    resourceRequestQuery.searchKey ? resourceRequestQuery.searchKey : ''
  );
  const { getProjectNameAndId } = useProjects();
  useEffect(() => {
    setFilterName(resourceRequestQuery.searchKey ? resourceRequestQuery.searchKey : '');
  }, [resourceRequestQuery]);

  //project approval status options
  const projectApprovalStatusOption = mapToOptions(projectApprovalStatus, 'id', 'name');
  //manager options
  let managerOptions = [];
  const { data: managers } = useManager();
  if (managers) {
    managerOptions = mapToOptions(managers, 'id', 'name');
  }
  //department options
  let departmentOptions = [];
  const { data: departments } = useDepartment();
  if (departments) {
    departmentOptions = mapToOptions(departments, 'departmentId', 'name');
  }
  //project options
  let projectOptions = [];
  const { data: projects } = getProjectNameAndId(false); //if i pass false it will return all the projects.if true it will return only the projects of current logged manager.
  if (projects) {
    projectOptions = mapToOptions(projects, 'projectId', 'projectCode', 'projectName');
  }
  // handle the filter bar  state.
  const handleOpenFilter = () => {
    setOpenFilter(true);
  };
  const handleCloseFilter = () => {
    setOpenFilter(false);
  };
  // resourcename search
  const handleFilterByName = (event) => {
    setFilterName(event.target.value);
    setResourceName(event.target.value.trim());
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
                value={resourceRequestFilters.projectFilters}
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
                value={resourceRequestFilters.projectManagerFilters}
                options={managerOptions}
                onChange={(selectedOption) => {
                  setProjectManagerFilter(selectedOption);
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
                value={resourceRequestFilters.departmentFilters}
                options={departmentOptions}
                onChange={(selectedOption) => {
                  setDepartmentFilter(selectedOption);
                }}
              />
            </Box>
          </div>

          <div id="resource name" className="px-6 py-2">
            <Box sx={{ display: 'flex', flexDirection: 'column' }}>
              <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
                Resource Name
              </Typography>
              <OutlinedInput
                sx={{ paddingLeft: 1, paddingRight: 1 }}
                value={filterName}
                onChange={handleFilterByName}
                placeholder="Search name..."
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
                value={resourceRequestFilters.approvalStatusFilters}
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
