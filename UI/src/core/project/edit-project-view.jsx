/* eslint-disable react/react-in-jsx-scope */
// @mui
import { Box, Grid, Typography } from '@mui/material';
import useAuth from 'src/hooks/use-auth';
import { useLocation } from 'react-router-dom';
import { ProjectForm } from 'src/components/ui/project-form'; // ----------------------------------------------------------------------

export default function EditProjectView() {
  const location = useLocation();
  const { project, id } = location.state;
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();

  return (
    <Grid
      container
      gap={6}
      sx={{
        display: 'flex',
        py: 6,
        px: 3,
      }}
    >
      <Grid item xs={4} maxWidth={'100px'}>
        <Box
          width={'100%'}
          sx={{
            display: 'flex',
            flexDirection: 'column',
            py: 2,
            px: 3,
            backgroundColor: '#F2F3F5',
            borderRadius: '15px',
          }}
        >
          <Typography variant="h5" gutterBottom>
            Edit existing project
          </Typography>
          <Typography variant="body1">
            Please edit the existing project and ensure that all mandatory fields are filled.{' '}
          </Typography>
        </Box>
      </Grid>
      <Grid item xs={7}>
        <ProjectForm
          isEdit={true}
          formMode={'edit'}
          currentRole={currentLoggedUser}
          initialValues={project}
          id={id}
        />
      </Grid>
    </Grid>
  );
}
