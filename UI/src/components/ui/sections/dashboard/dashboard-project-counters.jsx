import * as React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import CardCounter from 'src/components/dashboard-component/count-up';
import ProjectProgressBar from 'src/components/dashboard-component/project-progress-bar';
import { getPercentage } from 'src/utils/utils';
import { Grid } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import useProjectQueryStore from '../../stores/projectStore';
import useAuth from 'src/hooks/use-auth';

export default function DashboardProjectCounters(content) {
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  const setProjectTypeFilter = useProjectQueryStore((s) => s.setProjectTypeFilter);
  const setProjectStatusFilter = useProjectQueryStore((s) => s.setProjectStatusFilter);
  const setClearAllProjectFilters = useProjectQueryStore((s) => s.setClearAllFilters);
  const setSearchProjectText = useProjectQueryStore((s) => s.setSearchText);

  const navigate = useNavigate();
  const cardStyle = {
    width: '100%',
    minHeight: 150,
    paddingTop: '15px',
    paddingLeft: '20px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  };

  const internalFilter = [
    {
      value: 0,
      label: 'Innovature Billing',
    },
  ];

  const billableFilter = [
    {
      value: 1,
      label: 'Customer Billing',
    },
  ];

  const projectInProgress = [
    {
      value: '1',
      label: 'In Progress',
    },
  ];

  function handleRunningProject() {
    if (currentLoggedUser !== 1) {
      setClearAllProjectFilters();
      setSearchProjectText(null);
      setProjectStatusFilter(projectInProgress);
      navigate('/projectManagement');
    }
  }

  function handleBillableProject() {
    if (currentLoggedUser !== 1) {
      setClearAllProjectFilters();
      setSearchProjectText(null);
      setProjectTypeFilter(billableFilter);
      setProjectStatusFilter(projectInProgress);
      navigate('/projectManagement');
    }
  }

  function handleInternalProject() {
    if (currentLoggedUser !== 1) {
      setClearAllProjectFilters();
      setSearchProjectText(null);
      setProjectTypeFilter(internalFilter);
      setProjectStatusFilter(projectInProgress);
      navigate('/projectManagement');
    }
  }

  return (
    <Grid container spacing={{ xs: 3, sm: 3, md: 3, xl: 3 }}>
      <Grid item xs={12} sm={6} md={4} lg={4} xl={4}>
        <Card sx={cardStyle} onClick={handleRunningProject}>
          <CardContent>
            <Typography sx={{ mb: 1.5 }} color="text.secondary">
              Running Projects
            </Typography>
            <Typography variant="h4" component="div">
              <CardCounter value={content?.totalProjectCount} />
            </Typography>
          </CardContent>
          <ProjectProgressBar
            width={70}
            height={70}
            percentage={getPercentage(content?.totalProjectCount, content?.totalProjectCount)}
          />
        </Card>
      </Grid>
      <Grid item xs={12} sm={6} md={4} lg={4} xl={4}>
        <Card sx={cardStyle} onClick={handleBillableProject}>
          <CardContent>
            <Typography sx={{ mb: 1.5 }} color="text.secondary">
              Customer Billing
            </Typography>
            <Typography variant="h4" component="div">
              <CardCounter value={content?.billableProjectCount} />
            </Typography>
          </CardContent>
          <ProjectProgressBar
            width={70}
            height={70}
            percentage={getPercentage(content?.billableProjectCount, content?.totalProjectCount)}
          />
        </Card>
      </Grid>
      <Grid item xs={12} sm={6} md={4} xl={4}>
        <Card sx={cardStyle} onClick={handleInternalProject}>
          <CardContent>
            <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
              Innovature Billing
            </Typography>
            <Typography variant="h4" component="div">
              <CardCounter value={content?.internalProjectCount} />
            </Typography>
          </CardContent>
          <ProjectProgressBar
            width={70}
            height={70}
            percentage={getPercentage(content?.internalProjectCount, content?.totalProjectCount)}
          />
        </Card>
      </Grid>
    </Grid>
  );
}
