/* eslint-disable react/react-in-jsx-scope */
// @mui
import { Box, Grid, Typography } from '@mui/material';
import { useLocation } from 'react-router-dom';
import { AddResourceForm } from 'src/components/forms/addResource';
// ----------------------------------------------------------------------

export default function EditResourceView() {
  const location = useLocation();
  const { resourceData } = location.state;

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
            Edit existing resource
          </Typography>
          <Typography variant="body1">
            Please edit the existing resource and ensure that all mandatory fields are filled.{' '}
          </Typography>
        </Box>
      </Grid>
      <Grid item xs={7}>
        <AddResourceForm isEdit={true} initialValues={resourceData} />
      </Grid>
    </Grid>
  );
}
