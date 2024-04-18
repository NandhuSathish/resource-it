/* eslint-disable react/react-in-jsx-scope */
import { m } from 'framer-motion';
import { Link as RouterLink } from 'react-router-dom';
// @mui
import { styled } from '@mui/material/styles';
import { Box, Button, Typography, Container } from '@mui/material';
// components

import { MotionContainer, varBounce } from 'src/components/animate';
// assets
import { PageNotFoundIllustration } from 'src/assets';
import Page from 'src/components/Page';
import useAuth from 'src/hooks/use-auth';

// ----------------------------------------------------------------------

const RootStyle = styled('div')(({ theme }) => ({
  display: 'flex',
  minHeight: '100%',
  alignItems: 'center',
  paddingTop: theme.spacing(15),
  paddingBottom: theme.spacing(10),
}));

// ----------------------------------------------------------------------

export default function NotFoundView() {
  const { getUserDetails } = useAuth();
  const { role } = getUserDetails();
  return (
    <Page title="404 Page Not Found" sx={{ height: 1 }}>
      <RootStyle>
        <Container component={MotionContainer}>
          <Box sx={{ maxWidth: 480, margin: 'auto', textAlign: 'center' }}>
            <m.div variants={varBounce().in}>
              <Typography variant="h3" paragraph>
                Sorry, page not found!
              </Typography>
            </m.div>
            <Typography sx={{ color: 'text.secondary' }}>
              Sorry, we couldn’t find the page you’re looking for. Perhaps you’ve mistyped the URL?
              Be sure to check your spelling.
            </Typography>

            <m.div variants={varBounce().in}>
              <PageNotFoundIllustration sx={{ height: 260, my: { xs: 5, sm: 10 } }} />
            </m.div>

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
      </RootStyle>
    </Page>
  );
}
