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
import makeAnimated from 'react-select/animated';
import { selectStyles } from 'src/utils/cssStyles';
import Iconify from 'src/components/iconify';
import useResourceRequestQueryStore from 'src/components/ui/stores/resourceRequestStore';
import { mapToOptions } from 'src/utils/utils';
import { resourceRequestApprovalStatusFilter } from 'src/utils/constants';
import useProjects from 'src/hooks/use-projects';
import useDepartment from 'src/hooks/use-department';

// ----------------------------------------------------------------------

const animatedComponents = makeAnimated();

export default function ResourceWiseRequestFilters() {
  const resourceRequestQuery = useResourceRequestQueryStore((s) => s.resourceRequestQuery);
  const resourceRequestFilters = useResourceRequestQueryStore((s) => s.resourceRequestFilters);
  // eslint-disable-next-line no-unused-vars
  const [filterName, setFilterName] = useState(
    resourceRequestQuery.searchKey ? resourceRequestQuery.searchKey : ''
  );
  const setResourceName = useResourceRequestQueryStore((s) => s.setSearchText);
  const setProjectFilter = useResourceRequestQueryStore((s) => s.setProjectFilter);
  const setDepartmentFilter = useResourceRequestQueryStore((s) => s.setDepartmentFilter);
  const setApprovalStatusFilters = useResourceRequestQueryStore((s) => s.setApprovalStatusFilters);
  const setClearAllFilters = useResourceRequestQueryStore((s) => s.setClearAllFilters);
  const [openFilter, setOpenFilter] = useState(false);
  const { getProjectNameAndId } = useProjects();

  useEffect(() => {
    setFilterName(resourceRequestQuery.searchKey ? resourceRequestQuery.searchKey : '');
  }, [resourceRequestQuery]);

  //project options
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

  //resourceRequestApprovalStatusOption
  const resourceRequestApprovalStatusOption = mapToOptions(
    resourceRequestApprovalStatusFilter,
    'id',
    'name'
  );

  // handle the filter bar  state.
  const handleOpenFilter = () => {
    setOpenFilter(true);
  };

  const handleCloseFilter = () => {
    setOpenFilter(false);
  };

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
                options={resourceRequestApprovalStatusOption}
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
