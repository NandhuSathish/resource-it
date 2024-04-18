import React, { useEffect, useState } from 'react';
import DashboardChart from '../dashboard-chart';

import useDashboard from 'src/hooks/use-dashboard';
import { Box, Container, Grid, Typography } from '@mui/material';
import DashboardResourceCounters from '../dashboard-resource-counters';
import DashboardProjectCounters from '../dashboard-project-counters';

export default function DashboardView() {
  const [dashboardData, setDashboardData] = useState(null);

  const { getDashboardData } = useDashboard();

  useEffect(() => {
    if (getDashboardData.isLoading) {
      // The query is still loading
      return;
    }

    if (getDashboardData.isError) {
      return;
    }

    // The query has completed
    if (getDashboardData.data) {
      const { data } = getDashboardData;
      setDashboardData(data);
    }
  }, [getDashboardData]);

  if (!dashboardData) {
    return <div></div>; // Or your loading spinner
  }

  return (
    <Container maxWidth={false}>
      <Box mb={3}>
        <DashboardProjectCounters
          totalResourceCount={dashboardData?.totalResourceCount}
          internalResourceCount={dashboardData?.internalResourceCount}
          billableResourceCount={dashboardData?.billableResourceCount}
          benchResourceCount={dashboardData?.benchResourceCount}
          totalProjectCount={dashboardData?.totalProjectCount}
          billableProjectCount={dashboardData?.billableProjectCount}
          internalProjectCount={dashboardData?.internalProjectCount}
        />
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12} sm={12} md={4} lg={4} xl={4}>
          <Typography variant="h5" sx={{ paddingBottom: 2 }}>
            Resources
          </Typography>
          <DashboardResourceCounters
            totalResourceCount={dashboardData?.totalResourceCount}
            internalResourceCount={dashboardData?.internalResourceCount}
            billableResourceCount={dashboardData?.billableResourceCount}
            benchResourceCount={dashboardData?.benchResourceCount}
          />
        </Grid>
        <Grid item xs={12} sm={12} md={12} lg={8} xl={8}>
          <Typography variant="h5" sx={{ paddingBottom: 2 }}>
            Resource Diagram
          </Typography>
          <DashboardChart />
        </Grid>
      </Grid>
    </Container>
  );
}
