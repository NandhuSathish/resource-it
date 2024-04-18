import React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import CardCounter from 'src/components/dashboard-component/count-up';
import ProjectProgressBar from 'src/components/dashboard-component/project-progress-bar';
import { getPercentage } from 'src/utils/utils';
import { useNavigate } from 'react-router-dom';
import { Grid } from '@mui/material';
import useResourceQueryStore from '../../stores/resourceStore';

const cardStyle = {
  width: '100%',
  minHeight: 150,
  paddingTop: '15px',
  paddingLeft: '20px',
  mx: 'auto',
};

const benchedResourceFilter = [
  {
    value: 0,
    label: 'Bench',
  },
];

const internalResourceFilter = [
  {
    value: 1,
    label: 'Internal',
  },
];
const externalResourceFilter = [
  {
    value: 2,
    label: 'Billable',
  },
];
export default function DashboardResourceCounters(content) {
  const setAllocationStatusFilter = useResourceQueryStore((s) => s.setAllocationStatusFilter);
  const setClearAllFilters = useResourceQueryStore((s) => s.setClearAllFilters);
  const setSearchResourceText = useResourceQueryStore((s) => s.setSearchText);
  const navigate = useNavigate();

  const loadExternaleResources = () => {
    setClearAllFilters();
    setSearchResourceText(null);
    setAllocationStatusFilter(externalResourceFilter);
    navigate('/resourceManagement/resourceList', {
      state: { flag: 1 },
    });
  };

  const loadTotalResources = () => {
    setClearAllFilters();
    setSearchResourceText(null);
    navigate('/resourceManagement/resourceList', {
      state: { flag: 1 },
    });
  };

  const loadInternalResources = () => {
    setClearAllFilters();
    setSearchResourceText(null);
    setAllocationStatusFilter(internalResourceFilter);
    navigate('/resourceManagement/resourceList', {
      state: { flag: 1 },
    });
  };

  const loadBenchedResource = () => {
    setClearAllFilters();
    setSearchResourceText(null);
    setAllocationStatusFilter(benchedResourceFilter);
    navigate('/resourceManagement/resourceList', {
      state: { flag: 1 },
    });
  };

  return (
    <Grid container spacing={3}>
      <Grid item xs={12} sm={12} md={12} lg={12} xl={12}>
        <Card sx={cardStyle} onClick={loadTotalResources}>
          <div className="flex justify-center items-center">
            <CardContent>
              <Typography color="text.secondary" gutterBottom>
                Total Resources
              </Typography>
              <Typography variant="h4" component="div">
                <CardCounter value={content?.totalResourceCount} />
              </Typography>
            </CardContent>
            <pre> </pre>
            <ProjectProgressBar
              width={70}
              height={70}
              percentage={getPercentage(content?.totalResourceCount, content?.totalResourceCount)}
            />
          </div>
        </Card>
      </Grid>

      <Grid item xs={12} sm={12} md={12} lg={12} xl={12}>
        <Card sx={cardStyle} onClick={loadExternaleResources}>
          <div className="flex justify-center items-center">
            <CardContent>
              <Typography color="text.secondary" gutterBottom>
                Billable Resources
              </Typography>
              <Typography variant="h4" component="div">
                <CardCounter value={content?.billableResourceCount} />
              </Typography>
            </CardContent>
            <ProjectProgressBar
              width={70}
              height={70}
              percentage={getPercentage(
                content?.billableResourceCount,
                content?.totalResourceCount
              )}
            />
          </div>
        </Card>
      </Grid>
      <Grid item xs={12}>
        <Card sx={cardStyle} onClick={loadInternalResources}>
          <div className="flex justify-center items-center">
            <CardContent>
              <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                Internal Resources
              </Typography>

              <Typography variant="h4" component="div">
                <CardCounter value={content?.internalResourceCount} />
              </Typography>
            </CardContent>

            <ProjectProgressBar
              width={70}
              height={70}
              percentage={getPercentage(
                content?.internalResourceCount,
                content?.totalResourceCount
              )}
            />
          </div>
        </Card>
      </Grid>

      <Grid item xs={12}>
        <Card sx={cardStyle} onClick={loadBenchedResource}>
          <div className="flex justify-center items-center">
            <CardContent>
              <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                Benched Resources
              </Typography>

              <Typography variant="h4" component="div">
                <CardCounter value={content?.benchResourceCount} />
              </Typography>
            </CardContent>
            <ProjectProgressBar
              width={70}
              height={70}
              percentage={getPercentage(content?.benchResourceCount, content?.totalResourceCount)}
            />
          </div>
        </Card>
      </Grid>
    </Grid>
  );
}
