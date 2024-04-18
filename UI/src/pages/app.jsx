/* eslint-disable react/react-in-jsx-scope */
import { Helmet } from 'react-helmet-async';
import DashboardView from 'src/components/ui/sections/dashboard/view/dashboard-view';
// ----------------------------------------------------------------------

export default function AppPage() {
  return (
    <>
      <Helmet>
        <title> Dashboard | Resource IT </title>
      </Helmet>

      <DashboardView />
    </>
  );
}
