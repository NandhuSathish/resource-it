/* eslint-disable react/display-name */
/* eslint-disable react/react-in-jsx-scope */
import { Suspense, lazy } from 'react';
import { Navigate, useRoutes, useLocation } from 'react-router-dom';
// layouts

// components
import LoadingScreen from 'src/components/LoadingScreen';
import useAuth from 'src/hooks/use-auth';
import ProtectedRoutes from './components/protected-routes';
import DashboardLayout from 'src/layouts/dashboard';
import LogoOnlyLayout from 'src/layouts/LogoOnlyLayout';
import ResourceDetailAnalysisPage from 'src/pages/resource-detail-analysis-page';

// ----------------------------------------------------------------------

const Loadable = (Component) => {
  return function LoadableComponentWithLocation(props) {
    const { pathname } = useLocation();

    return (
      <Suspense fallback={<LoadingScreen isDashboard={pathname.includes('/dashboard')} />}>
        <Component {...props} />
      </Suspense>
    );
  };
};

export default function Router() {
  const { isAuthenticated } = useAuth();
  const isLoggedIn = isAuthenticated();
  return useRoutes([
    // Check if the user is logged in. If they are, redirect them to the resourceManagement page.
    // If they aren't, show them the LoginPage.
    {
      path: '',
      element: isLoggedIn ? <Navigate to="/dashboard" replace /> : <LoginPage />,
      index: true,
    },

    {
      path: '/login',
      element: isLoggedIn ? <Navigate to="/dashboard" replace /> : <LoginPage />,
      index: true,
    },
    {
      path: 'dashboard',
      element: isLoggedIn ? <DashboardLayout /> : <Navigate to="/login" replace />,
      children: [
        {
          element: (
            <ProtectedRoutes allowedRoles={[1, 2, 3, 4, 5]}>
              <DashboardPage />
            </ProtectedRoutes>
          ),
          index: true,
        },
      ],
    },
    // resourceManagement
    {
      path: 'resourceManagement',
      element: isLoggedIn ? <DashboardLayout /> : <Navigate to="/login" replace />,
      children: [
        { element: <Navigate to="/resourceManagement/resourceList" replace />, index: true },
        {
          path: 'resourceList',
          element: (
            <ProtectedRoutes allowedRoles={[1, 2, 3, 4, 6, 5]}>
              <ResourceListPage />
            </ProtectedRoutes>
          ),
        },
        {
          path: 'addResource',
          element: (
            <ProtectedRoutes allowedRoles={[1, 2, 3, 4, 6, 5]}>
              <AddResourcePage />
            </ProtectedRoutes>
          ),
        },

        {
          path: 'resourceList/editResource',
          element: (
            <ProtectedRoutes allowedRoles={[1, 2, 3, 4, 6, 5]}>
              <EditResourcesPage />
            </ProtectedRoutes>
          ),
        },
        {
          path: 'resourceAllocations',
          element: (
            <ProtectedRoutes allowedRoles={[2, 3, 4, 5]}>
              <ResourceAllocationPage />
            </ProtectedRoutes>
          ),
        },
        {
          path: 'allocationRequests',
          element: (
            <ProtectedRoutes allowedRoles={[2, 3, 4, 5]}>
              <AllocationRequestPage />
            </ProtectedRoutes>
          ),
        },
        {
          path: 'allocationApprovals',
          element: (
            <ProtectedRoutes allowedRoles={[2, 3]}>
              <AllocationApprovalPage />
            </ProtectedRoutes>
          ),
        },
        {
          path: 'allocationApprovals/technologyWiseAllocateAndApprove',
          element: (
            <ProtectedRoutes allowedRoles={[3]}>
              <TechnologyWiseApprovalResourceListPage />
            </ProtectedRoutes>
          ),
        },
      ],
    },
    // projectManagement
    {
      path: 'projectManagement',
      element: isLoggedIn ? <DashboardLayout /> : <Navigate to="/login" replace />,
      children: [
        { element: <Navigate to="/projectManagement/projectList" replace />, index: true },
        {
          path: 'projectList',
          element: (
            <ProtectedRoutes allowedRoles={[1, 2, 3, 4, 6, 5]}>
              <ProjectListPage />
            </ProtectedRoutes>
          ),
        },
        {
          path: 'projectList/projectDetails/:id',
          element: (
            <ProtectedRoutes allowedRoles={[1, 2, 3, 4, 5, 6]}>
              <ProjectDetailsPage />
            </ProtectedRoutes>
          ),
        },
        {
          path: 'projectRequests',
          element: (
            <ProtectedRoutes allowedRoles={[2, 3, 4, 5]}>
              <ProjectRequestPage />
            </ProtectedRoutes>
          ),
        },
        {
          path: 'addProject',
          element: (
            <ProtectedRoutes allowedRoles={[1, 2, 3, 4, 6, 5]}>
              <AddProjectPage />
            </ProtectedRoutes>
          ),
        },
        {
          path: 'projectList/editProject',
          element: (
            <ProtectedRoutes allowedRoles={[1, 2, 3, 4, 6, 5]}>
              <EditProjectPage />
            </ProtectedRoutes>
          ),
        },
      ],
    },
    // BillabilitySummary

    {
      path: 'billabilitySummary',
      element: isLoggedIn ? <DashboardLayout /> : <Navigate to="/login" replace />,
      children: [
        {
          element: (
            <ProtectedRoutes allowedRoles={[2, 3, 4, 5]}>
              <BillabilitySummaryPage />
            </ProtectedRoutes>
          ),
          index: true,
        },
        {
          path: 'resourceDetailAnalysis/:id',
          element: (
            <ProtectedRoutes allowedRoles={[2, 3, 4, 5]}>
              <ResourceDetailAnalysisPage />
            </ProtectedRoutes>
          ),
        },
      ],
    },

    {
      path: '*',
      element: <LogoOnlyLayout />,
      children: [
        { path: '404', element: <Page404 /> },
        { path: '403', element: <Page403 /> },
        { path: '*', element: <Navigate to="/404" replace /> },
      ],
    },
    { path: '*', element: <Navigate to="/404" replace /> },
  ]);
}

// Dashboard
export const IndexPage = Loadable(lazy(() => import('src/pages/login')));
export const DashboardPage = Loadable(lazy(() => import('src/pages/app')));
export const ResourceListPage = Loadable(lazy(() => import('src/pages/resource-page')));
export const AddResourcePage = Loadable(lazy(() => import('src/pages/add-resource-page')));
export const EditResourcesPage = Loadable(lazy(() => import('src/pages/edit-resource-page')));
export const ProjectListPage = Loadable(lazy(() => import('src/pages/project-page')));
export const AddProjectPage = Loadable(lazy(() => import('src/pages/add-project-page')));
export const EditProjectPage = Loadable(lazy(() => import('src/pages/edit-project-page')));
export const ProjectRequestPage = Loadable(lazy(() => import('src/pages/project-request-page')));
export const ResourceAllocationPage = Loadable(
  lazy(() => import('src/pages/resource-allocation-page'))
);
export const AllocationRequestPage = Loadable(
  lazy(() => import('src/pages/allocation-request-page'))
);
export const AllocationApprovalPage = Loadable(
  lazy(() => import('src/pages/allocation-approval-page'))
);
export const ProjectDetailsPage = Loadable(lazy(() => import('src/pages/project-details-page')));
export const TechnologyWiseApprovalResourceListPage = Loadable(
  lazy(() => import('src/pages/technology-wise-approval-resource-list-page'))
);
export const BillabilitySummaryPage = Loadable(
  lazy(() => import('src/pages/billability-summary-page'))
);

export const LoginPage = lazy(() => import('src/pages/login'));
export const Page404 = lazy(() => import('src/pages/page-not-found'));
export const Page403 = lazy(() => import('src/pages/no-permission-page'));
