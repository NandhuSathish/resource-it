/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import { ProjectRequestView } from 'src/components/ui/sections/project-requests/view';
import useAuth from 'src/hooks/use-auth';
// ----------------------------------------------------------------------

export default function ProjectPage() {
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  const isApprovalView = currentLoggedUser === 2;
  return (
    <>
      <Helmet>
        <title> {isApprovalView ? 'Project Approvals' : 'Project Requests'} | Resource IT</title>
      </Helmet>
      <Container maxWidth="100%">
        <HeaderBreadcrumbs
          heading={isApprovalView ? 'Project Approvals' : 'Project Requests'}
          links={[
            { name: 'Dashboard', href: '/dashboard' },
            { name: 'Project Management' },
            { name: isApprovalView ? 'Project Approvals' : 'Project Requests' },
          ]}
        />
        <ProjectRequestView />
      </Container>
    </>
  );
}
