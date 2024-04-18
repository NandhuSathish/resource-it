/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import { AllocationRequestView } from 'src/components/ui/sections/allocation-requests/view';
// ----------------------------------------------------------------------

export default function AllocationRequestPage() {
  return (
    <>
      <Helmet>
        <title> Allocation Requests | Resource IT</title>
      </Helmet>
      <Container maxWidth="100%">
        <HeaderBreadcrumbs
          heading="Allocation Requests"
          links={[
            { name: 'Dashboard', href: '/dashboard' },
            { name: 'Resource Management' },
            { name: 'Allocation Requests' },
          ]}
        />
        <AllocationRequestView />
      </Container>
    </>
  );
}
