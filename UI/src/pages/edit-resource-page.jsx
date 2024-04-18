/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import EditResourceView from 'src/core/resource/view/edit-resource-view';
// ----------------------------------------------------------------------

export default function EditResourcesPage() {
  return (
    <>
      <Helmet>
        <title> Edit Resource | Resource IT</title>
      </Helmet>
      <Container sx={{ marginLeft: '0%' }} maxWidth="false">
        <HeaderBreadcrumbs
          heading={'Edit Resource'}
          links={[
            { name: 'Dashboard', href: '/dashboard' },
            { name: 'Resource Management', href: '' },
            { name: 'Edit Resource', href: '' },
          ]}
        />
        <EditResourceView />
      </Container>
    </>
  );
}
