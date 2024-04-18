/* eslint-disable react/react-in-jsx-scope */
import { useState } from 'react';
import { Box, Stack, Button, Drawer, Divider, Typography, IconButton } from '@mui/material';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import { selectStyles } from 'src/utils/cssStyles';
import useManager from 'src/hooks/use-manager';
import Iconify from 'src/components/iconify';
import useProjectRequestQueryStore from 'src/components/ui/stores/projectRequestStore';
import { mapManagerToOptions, mapToOptions } from 'src/utils/utils';
import { projectApprovalStatus, projectType } from 'src/utils/constants';
import useAuth from 'src/hooks/use-auth';

// ----------------------------------------------------------------------

const animatedComponents = makeAnimated();

export default function ProjectRequestFilters() {
  const { getUserDetails } = useAuth();
  const { role, resourceId } = getUserDetails();

  const setProjectTypeFilter = useProjectRequestQueryStore((s) => s.setProjectTypeFilter);
  const setManagerFilter = useProjectRequestQueryStore((s) => s.setManagerFilter);
  const setApprovalStatusFilters = useProjectRequestQueryStore((s) => s.setApprovalStatusFilters);
  const projectRequestFilters = useProjectRequestQueryStore((s) => s.projectRequestFilters);
  const setClearAllFilters = useProjectRequestQueryStore((s) => s.setClearAllFilters);
  const [openFilter, setOpenFilter] = useState(false);

  //project type options.
  const projectTypeOptions = mapToOptions(projectType, 'id', 'name');
  //band options
  const projectApprovalStatusOption = mapToOptions(projectApprovalStatus, 'id', 'name');
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
                isClearable={true}
                value={projectRequestFilters.projectTypeFilters}
                components={animatedComponents}
                closeMenuOnSelect={false}
                options={projectTypeOptions}
                isMulti
                onChange={(selectedOptions) => {
                  if (selectedOptions) {
                    setProjectTypeFilter(selectedOptions);
                  } else {
                    // Clear the filter if the selection is cleared
                    setProjectTypeFilter([]);
                  }
                }}
              />
            </Box>
          </div>
          {/* only show the project manager filter to the hod.  */}
          {role !== 5 && (
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
                  value={projectRequestFilters.managerFilters}
                  options={managerOptions}
                  onChange={(selectedOption) => {
                    setManagerFilter(selectedOption);
                  }}
                />
              </Box>
            </div>
          )}

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
                value={projectRequestFilters.approvalStatusFilters}
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
