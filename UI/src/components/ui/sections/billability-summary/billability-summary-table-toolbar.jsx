import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Toolbar, OutlinedInput, InputAdornment, Box } from '@mui/material';
import Iconify from 'src/components/iconify';
import useBillabilitySummaryQueryStore from 'src/components/ui/stores/billabilitySummaryStore';
import { toast } from 'sonner';
import CustomLoadingButton from 'src/components/ui/custom-loading-button';
import { Typography } from '@mui/material';
import BillabilitySummaryResourceState from './billability-summary-resource-state';
import BillabilitySummaryFilters from './billability-summary-filter';
import useBillabilitySummary from 'src/hooks/useBillabilitySummary';
import dayjs from 'dayjs';
// ----------------------------------------------------------------------

export default function BillabilitySummaryTableToolbar({ filterName, onFilterName }) {
  const billabilitySummaryQuery = useBillabilitySummaryQueryStore((s) => s.billabilitySummaryQuery);
  const { exportBillabilitySummary, exportBillabilitySummaryDetail } = useBillabilitySummary();
  const [isLoading, setIsLoading] = useState(false);
  const [isPending, setIsPending] = useState(false);
  const formattedStartDate = dayjs(billabilitySummaryQuery.startDate).format('MMM DD YYYY');
  const formattedEndDate = dayjs(billabilitySummaryQuery.endDate).format('MMM DD YYYY');

  const currentDate = new Date();
  const nextDate = new Date();
  nextDate.setDate(currentDate.getDate() + 1);

  const handleExport =
    (exportFunction, setLoadingState, data = null) =>
    () => {
      setLoadingState(true);
      const options = {
        onSuccess: () => {
          setLoadingState(false);
          toast.success('Exported successfully');
        },
        onError: () => {
          setLoadingState(false);
          toast.error('Export failed please try again later');
        },
      };
      exportFunction.mutate(data, options);
    };

  const handleBillabilitySummaryExport = handleExport(
    exportBillabilitySummary,
    setIsLoading,
    billabilitySummaryQuery
  );
  const handleBillabilitySummaryDetailExport = handleExport(
    exportBillabilitySummaryDetail,
    setIsPending
  );

  return (
    <Toolbar
      sx={{
        height: 96,
        display: 'flex',
        justifyContent: 'space-between',
      }}
    >
      <OutlinedInput
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
      <Box>
        <Typography variant="h5" sx={{ fontWeight: 'bold' }}>
          {formattedStartDate} <span style={{ margin: '0 10px' }}>To</span> {formattedEndDate}
        </Typography>
      </Box>
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
          onClick={handleBillabilitySummaryExport}
        >
        Billability Summary
        </CustomLoadingButton>

        <CustomLoadingButton
          isLoading={isPending}
          loadingPosition="start"
          sx={{
            marginRight: 2,
          }}
          variant="contained"
          color="primary"
          icon="solar:import-bold"
          onClick={handleBillabilitySummaryDetailExport}
        >
          Allocation History
        </CustomLoadingButton>

        <BillabilitySummaryFilters />
        <BillabilitySummaryResourceState />
      </Box>
    </Toolbar>
  );
}

BillabilitySummaryTableToolbar.propTypes = {
  filterName: PropTypes.string,
  onFilterName: PropTypes.func,
};
