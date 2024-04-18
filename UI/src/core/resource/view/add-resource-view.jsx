/* eslint-disable react/react-in-jsx-scope */
// @mui
import { Box,  Grid, Typography } from '@mui/material';

import { AddResourceForm } from 'src/components/forms/addResource';

// ----------------------------------------------------------------------

export default function AddResourceView() {
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
            Create new resource
          </Typography>
          <Typography variant="body1">
            Please fill in the mandatory fields to create a resource.{' '}
          </Typography>
        </Box>
      </Grid>
      <Grid item xs={7}>
        {/* <Card sx={{ width: '100%' , pb:4 }}> */}
        <AddResourceForm isEdit={false} initialValues={{}} />
        {/* </Card> */}
      </Grid>
    </Grid>
  );
}
