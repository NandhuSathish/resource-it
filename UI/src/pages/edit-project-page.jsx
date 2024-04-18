/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import EditResourceView from 'src/core/project/edit-project-view';
// ----------------------------------------------------------------------

export default function EditResourcesPage() {
  return (
    <>
      <Helmet>
        <title> Edit Projects | Resource IT</title>
      </Helmet>
      <Container sx={{ marginLeft: '0%' }} maxWidth="false">
        <HeaderBreadcrumbs
          heading={'Edit Project'}
          links={[
            { name: 'Dashboard', href: '/dashboard' },
            { name: 'Project Management', href: '' },
            { name: 'Edit Project', href: '' },
          ]}
        />
        <EditResourceView />
      </Container>
    </>
  );
}
