/* eslint-disable react/react-in-jsx-scope */
import { Container, IconButton } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import ResourceDetailAnalysisView from 'src/components/ui/sections/resource-detail-analysis/resource-detail-analysis-view';
import { useNavigate } from 'react-router-dom/dist';
import Iconify from 'src/components/iconify';
// ----------------------------------------------------------------------

export default function ResourceDetailAnalysisPage() {
  const navigate = useNavigate();
  return (
    <>
      <Helmet>
        <title> Resource Detail Analysis | Resource IT</title>
      </Helmet>
      <Container maxWidth="100%" >
        <div className='flex'>
          <div className="flex items-start align-top mr-2">
            <IconButton
              onClick={() => {
                navigate(-1);
              }}
            >
              <Iconify width={20} height={20} icon={'eva:arrow-ios-back-fill'} />
            </IconButton>
          </div>
          <HeaderBreadcrumbs
            heading="Resource Detail Analysis"
            links={[
              { name: 'Dashboard', href: '/dashboard' },
              { name: 'Billability Summary' },
              { name: 'Resource Detail Analysis' },
            ]}
          />
        </div>
        <ResourceDetailAnalysisView />
      </Container>
    </>
  );
}
