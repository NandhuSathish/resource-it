/* eslint-disable react/react-in-jsx-scope */

import { Box } from '@mui/material';

import TableToolbar from 'src/components/table/table-toolbar';
import PropTypes from 'prop-types';
import { toast } from 'sonner';
import { useState } from 'react';
import CustomLoadingButton from 'src/components/ui/custom-loading-button';
import { Typography } from '@mui/material';
import useResourceDetailAnalysis from 'src/hooks/use-resource-details-analysis';
import useResourceHistoryQueryStore from '../../stores/resourceHistoryStore';

// ----------------------------------------------------------------------

export default function ResourceHistoryTableToolbar({resourceId}) {
  const resourceDetailsQuery = useResourceHistoryQueryStore((s) => s.resourceDetailsQuery);
  const { exportResourceDetail } = useResourceDetailAnalysis();
  const [isLoading, setIsLoading] = useState(false);

  const historyFilters = {
   ...resourceDetailsQuery, id: resourceId,
  }
  
  const handleResourceHistoryExport = () => {
    setIsLoading(true);
    // eslint-disable-next-line no-unused-vars
    exportResourceDetail.mutate(historyFilters, {
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
      <Box
        sx={{
          height: 96,
          display: 'flex',
          justifyContent: 'start',
          alignItems: 'center',
        }}
      >
        <Typography variant="h4">Allocation History</Typography>
      </Box>
      <Box
        sx={{
          height: 96,
          display: 'flex',
          justifyContent: 'end',
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
          onClick={handleResourceHistoryExport}
        >
          Export
        </CustomLoadingButton>
      </Box>
    </TableToolbar>
  );
}

ResourceHistoryTableToolbar.propTypes = {
  resourceId: PropTypes.any,
};

