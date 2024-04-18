import { Box, Grid, Typography } from '@mui/material';
import React from 'react';
import ProjectProgressBar from 'src/components/dashboard-component/project-progress-bar';
import { getCompletionPercentage, getProjectDuration } from 'src/utils/utils';
import ProjectDetailsContent from './project-detail-content';

function ProjectDetails(projectDetails) {
  const { completedDays, remainingDays } = getProjectDuration(
    projectDetails.projectDetails.startDate,
    projectDetails.projectDetails.endDate
  );
  return (
    <Box maxWidth="xl" py="28px">
      <Grid container spacing={3}>
        <Grid item xs={6}>
          <ProjectDetailsContent projectDetails={projectDetails} />
        </Grid>
        <Grid sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <Grid container>
            <Box sx={{ marginTop: '50px', paddingLeft: '200px' }}>
              <Box sx={{ display: 'flex', marginBottom: '10px', justifyContent: 'center' }}>
                <Typography variant="h5" sx={{ mb: 1 }}>
                  Project Progress
                </Typography>
              </Box>
              <ProjectProgressBar
                width={260}
                height={260}
                percentage={getCompletionPercentage(
                  projectDetails.projectDetails.startDate,
                  projectDetails.projectDetails.endDate
                )}
              />
              <Box sx={{ marginTop: '20px', display: 'flex', justifyContent: 'center' }}>
                <Typography variant="body2">
                  {' '}
                  Completed Days: {isNaN(completedDays) ? 'Nil' : completedDays} <br></br> Remaining
                  Days: {isNaN(remainingDays) ? 'Nil' : remainingDays}{' '}
                </Typography>
              </Box>
            </Box>
          </Grid>
        </Grid>
      </Grid>
    </Box>
  );
}

export default ProjectDetails;
