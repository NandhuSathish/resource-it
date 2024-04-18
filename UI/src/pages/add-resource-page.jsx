/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import useAuth from 'src/hooks/use-auth';
import AddResourceView from 'src/core/resource/view/add-resource-view';
// ----------------------------------------------------------------------

export default function AddResources() {
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  return (
    <>
      <Helmet>
        <title> Add Resource | Resource IT</title>
      </Helmet>
      <Container maxWidth="false">
        <HeaderBreadcrumbs
          heading={'Add Resource'}
          links={[
            ...(currentLoggedUser !== 6 ? [{ name: 'Dashboard', href: '/dashboard' }] : []),
            { name: 'Resource Management', href: '' },
            { name: 'Add Resource', href: '' },
          ]}
        />
        <AddResourceView />
      </Container>
    </>
  );
}
