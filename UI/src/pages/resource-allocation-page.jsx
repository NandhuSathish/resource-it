/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import { ResourceAllocationView } from 'src/components/ui/sections/resource-allocation/view';
// ----------------------------------------------------------------------

export default function ResourcePage() {
  return (
    <>
      <Helmet>
        <title> Resource Allocation | Resource IT</title>
      </Helmet>
      <Container maxWidth="100%">
        <HeaderBreadcrumbs
          heading="Resource Allocation"
          links={[
            { name: 'Dashboard', href: '/dashboard' },
            { name: 'Resource Management' },
            { name: 'Resource Allocation' },
          ]}
        />
        <ResourceAllocationView />
      </Container>
    </>
  );
}
