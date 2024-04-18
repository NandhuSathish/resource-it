/* eslint-disable react/react-in-jsx-scope */

import PropTypes from 'prop-types';
import { OutlinedInput, InputAdornment, Box } from '@mui/material';
import useProjects from 'src/hooks/use-projects';
import Iconify from 'src/components/iconify';
import ProjectFilters from './project-filter';
import useProjectQueryStore from 'src/components/ui/stores/projectStore';
import TableToolbar from 'src/components/table/table-toolbar';
import { toast } from 'sonner';
import { useState } from 'react';
import CustomLoadingButton from 'src/components/ui/custom-loading-button';
// ----------------------------------------------------------------------

export default function ProjectTableToolbar({ filterName, onFilterName }) {
  const projectQuery = useProjectQueryStore((s) => s.projectQuery);
  const { exportProjects } = useProjects();
  const [isLoading, setIsLoading] = useState(false);

  const handleProjectExport = () => {
    setIsLoading(true);
    // eslint-disable-next-line no-unused-vars
    const { pageSize, pageNumber, sortOrder, sortKey, ...restOfProjectQuery } = projectQuery;
    exportProjects.mutate(restOfProjectQuery, {
      onSuccess: () => {
        toast.success('Exported successfully');
        setIsLoading(false);
      },
      onError: () => {
        toast.error('Export failed please try again later');
        setIsLoading(false);
      },
    });
  };
  return (
    <TableToolbar
      styles={{
        justifyContent: 'space-between',
      }}
    >
      <OutlinedInput
        value={filterName}
        onChange={onFilterName}
        placeholder="Search Project..."
        startAdornment={
          <InputAdornment position="start">
            <Iconify
              icon="eva:search-fill"
              sx={{ color: 'text.disabled', width: 20, height: 20 }}
            />
          </InputAdornment>
        }
      />

      <Box
        sx={{
          height: 96,
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <CustomLoadingButton
          isLoading={isLoading}
          loadingPosition="start"
          sx={{
            marginRight: 2,
          }}
          variant="contained"
          color="inherit"
          icon="solar:import-bold"
          onClick={handleProjectExport}
        >
          Export
        </CustomLoadingButton>

        <ProjectFilters />
      </Box>
    </TableToolbar>
  );
}

ProjectTableToolbar.propTypes = {
  onFilterName: PropTypes.func,
  filterName: PropTypes.any,
};
