import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Toolbar, OutlinedInput, InputAdornment, Box } from '@mui/material';

import Iconify from 'src/components/iconify';
import ResourceFilters from './resource-filter';
import useResourceQueryStore from 'src/components/ui/stores/resourceStore';
import ResourceState from './resource-state';
import useResources from 'src/hooks/useResources';
import { toast } from 'sonner';
import CustomLoadingButton from 'src/components/ui/custom-loading-button';
// ----------------------------------------------------------------------

export default function ResourceTableToolbar({ filterName, onFilterName }) {
  const resourceQuery = useResourceQueryStore((s) => s.resourceQuery);
  const { exportResources } = useResources();
  const [isLoading, setIsLoading] = useState(false);
  const handleResourceExport = () => {
    setIsLoading(true);
    // eslint-disable-next-line no-unused-vars
    const { pageSize, pageNumber, sortOrder, sortKey, ...restOfResourceQuery } = resourceQuery;
    exportResources.mutate(restOfResourceQuery, {
      onSuccess: () => {
        setIsLoading(false);
        toast.success('Exported successfully');
      },
      onError: () => {
        setIsLoading(false);
        toast.error('Export failed please try again later');
      },
    });
  };
  return (
    <Toolbar
      sx={{
        height: 96,
        display: 'flex',
        justifyContent: 'space-between',
      }}
    >
      <OutlinedInput
        // value={resourceQuery.name}
        value={filterName}
        onChange={onFilterName}
        placeholder="Search Resource..."
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
          color="primary"
          icon="solar:import-bold"
          onClick={handleResourceExport}
        >
          Export
        </CustomLoadingButton>

        <ResourceFilters />
        <ResourceState />
      </Box>
    </Toolbar>
  );
}

ResourceTableToolbar.propTypes = {
  filterName: PropTypes.string,
  onFilterName: PropTypes.func,
};
