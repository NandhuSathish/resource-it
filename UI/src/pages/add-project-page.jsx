/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import AddProjectView from 'src/core/project/add-project-view';
// ----------------------------------------------------------------------

export default function AddProjectPage() {
  return (
    <>
      <Helmet>
        <title> Add Projects | Resource IT</title>
      </Helmet>
      <Container sx={{ marginLeft: '0%' }} maxWidth="false">
        <HeaderBreadcrumbs
          heading={'Add Project'}
          links={[
            { name: 'Dashboard', href: '/dashboard' },
            { name: 'Project Management', href: '' },
            { name: 'Add Project', href: '' },
          ]}
        />
        <AddProjectView />
      </Container>
    </>
  );
}
