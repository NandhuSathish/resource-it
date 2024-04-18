/* eslint-disable react/react-in-jsx-scope */
import { Box, Button, Container, Typography } from '@mui/material';
import useAuth from 'src/hooks/use-auth';
import { RouterLink } from 'src/routes/components';
// ----------------------------------------------------------------------

export default function NotPermissionView() {
  const { getUserDetails } = useAuth();
  const { role } = getUserDetails();
  const renderHeader = (
    <Box
      component="header"
      sx={{
        top: 0,
        left: 0,
        width: 1,
        lineHeight: 0,
        position: 'fixed',
        p: (theme) => ({ xs: theme.spacing(3, 3, 0), sm: theme.spacing(5, 5, 0) }),
      }}
    >
      {/* <Logo /> */}
    </Box>
  );

  return (
    <>
      {renderHeader}

      <Container>
        <Box
          sx={{
            py: 12,
            maxWidth: 480,
            mx: 'auto',
            display: 'flex',
            minHeight: '100vh',
            textAlign: 'center',
            alignItems: 'center',
            flexDirection: 'column',
            justifyContent: 'center',
          }}
        >
          <Typography variant="h3" sx={{ mb: 3 }}>
            No permission
          </Typography>

          <Typography sx={{ color: 'text.secondary' }}>
            The page you are trying access has restricted access. Please refer to your system
            administrator
          </Typography>

          <Box
            component="img"
            src="/assets/illustrations/illustration_404.svg"
            sx={{
              mx: 'auto',
              height: 260,
              my: { xs: 5, sm: 10 },
            }}
          />

          <Button
            to={role == 6 ? '/resourceManagement' : '/'}
            size="large"
            color="inherit"
            variant="contained"
            component={RouterLink}
          >
            Go to Home
          </Button>
        </Box>
      </Container>
    </>
  );
}
