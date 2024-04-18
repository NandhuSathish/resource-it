/* eslint-disable react/react-in-jsx-scope */
import { Container } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import { BillabilitySummaryView } from 'src/components/ui/sections/billability-summary/view';
// ----------------------------------------------------------------------

export default function BillabilitySummaryPage() {
  return (
    <>
      <Helmet>
        <title> Billability Summary | Resource IT</title>
      </Helmet>
      <Container maxWidth="100%">
        <HeaderBreadcrumbs
          heading="Billability Summary"
          links={[{ name: 'Dashboard', href: '/dashboard' }, { name: 'Billability Summary' }]}
        />
        <BillabilitySummaryView />
      </Container>
    </>
  );
}
