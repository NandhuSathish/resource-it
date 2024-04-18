/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import { TechnologyWiseApprovalResourceListView } from 'src/components/ui/sections/allocation-approval/technology-wise-approval-resource-list/view';

// ----------------------------------------------------------------------

export default function TechnologyWiseApprovalResourceListPage() {
  return (
    <>
      <Helmet>
        <title> Technology Wise Approval Resource List| Resource IT</title>
      </Helmet>
      <Container maxWidth="100%">
        <TechnologyWiseApprovalResourceListView />
      </Container>
    </>
  );
}
