/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import AllocationApprovalView from 'src/components/ui/sections/allocation-approval/view/allocation-approval-view';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';

// ----------------------------------------------------------------------

export default function AllocationApprovalPage() {
  return (
    <>
      <Helmet>
        <title> Allocation Approval | Resource IT</title>
      </Helmet>
      <Container maxWidth="100%">
        <HeaderBreadcrumbs
          heading="Allocation Approvals"
          links={[
            { name: 'Dashboard', href: '/dashboard' },
            { name: 'Resource Management' },
            { name: 'Allocation Approvals' },
          ]}
        />
        <AllocationApprovalView />
      </Container>
    </>
  );
}
