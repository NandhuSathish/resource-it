/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import { ProjectView } from 'src/core/project/view';
// ----------------------------------------------------------------------

export default function ProjectPage() {
  return (
    <>
      <Helmet>
        <title> Projects | Resource IT</title>
      </Helmet>
      <Container maxWidth="100%">
        <HeaderBreadcrumbs
          heading="Project List"
          links={[
            { name: 'Dashboard', href: '/dashboard' },
            { name: 'Project Management' },
            { name: 'Project List' },
          ]}
        />
        <ProjectView />
      </Container>
    </>
  );
}
