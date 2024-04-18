/* eslint-disable react/react-in-jsx-scope */
import { Container, IconButton } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import HeaderBreadcrumbs from 'src/components/HeaderBreadcrumbs';
import ProjectDetailView from 'src/components/ui/sections/project-details/view/project-details-view';
import { useNavigate } from 'react-router-dom/dist';
import Iconify from 'src/components/iconify';

// ----------------------------------------------------------------------

export default function ProjectDetailsPage() {
  const navigate = useNavigate();
  return (
    <>
      <Helmet>
        <title> Projects | Resource IT</title>
      </Helmet>
      <Container sx={{ marginLeft: '0%' }} maxWidth="100%">
        <div className="flex">
          <div className='flex items-start align-top mr-2'> 
            <IconButton
              onClick={() => {
                navigate(-1);
              }}
            >
              <Iconify width={20} height={20} icon={'eva:arrow-ios-back-fill'} />
            </IconButton>
          </div>
          <HeaderBreadcrumbs
            heading={'Project Summary'}
            links={[
              { name: 'Dashboard', href: '/dashboard' },
              { name: 'Project Management', href: '' },
              { name: 'Project Summary', href: '' },
            ]}
          />
        </div>
        <ProjectDetailView />
      </Container>
    </>
  );
}
