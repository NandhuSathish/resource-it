/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import useAuth from 'src/hooks/use-auth';
import { ResourceView } from 'src/core/resource/view';
// ----------------------------------------------------------------------

export default function ResourcePage() {
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  return (
    <>
      <Helmet>
        <title> Resources | Resource IT</title>
      </Helmet>
      <Container maxWidth="100%">
        <HeaderBreadcrumbs
          heading="Resource List"
          links={[
            ...(currentLoggedUser !== 6 ? [{ name: 'Dashboard', href: '/dashboard' }] : []),
            { name: 'Resource Management' },
            { name: 'Resource List' },
          ]}
        />
        <ResourceView />
      </Container>
    </>
  );
}
