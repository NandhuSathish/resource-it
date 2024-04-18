/* eslint-disable react/react-in-jsx-scope */
// components

import SvgColor from 'src/components/svg-color';

// ----------------------------------------------------------------------

const getIcon = (name) => (
  <SvgColor src={`/assets/icons/navbar/${name}.svg`} sx={{ width: 1, height: 1 }} />
);

const ICONS = {
  dashboard: getIcon('ic_dashboard'),
  resource: getIcon('ic_user'),
  project: getIcon('ic_kanban'),
  analytics: getIcon('ic_analytics'),
};

const sidebarConfig = [
  // GENERAL
  // ----------------------------------------------------------------------
  {
    // subheader: 'general',
    items: [
      {
        title: 'Dashboard',
        current: location.pathname === '/',
        path: '/dashboard',
        icon: ICONS.dashboard,
        roles: [1, 2, 3, 4, 5],
      },
    ],
  },

  // MANAGEMENT
  // ----------------------------------------------------------------------
  {
    subheader: 'management',
    roles: [1, 2, 3, 4, 5, 6, 7],
    items: [
      {
        title: 'Resource Management',
        path: '/resourceManagement',
        current: location.pathname === '/resourceManagement',
        icon: ICONS.resource,
        roles: [1, 2, 3, 4, 5, 6],
        children: [
          {
            title: 'Resource List',
            path: '/resourceManagement/resourceList',
            roles: [1, 2, 3, 4, 5, 6],
          },
          { title: 'Add Resource', path: '/resourceManagement/addResource', roles: [1, 2, 3, 6] },
          {
            title: 'Resource Allocation',
            path: '/resourceManagement/resourceAllocations',
            roles: [2, 3, 5, 4],
          },
          {
            title: 'Allocation Requests',
            path: '/resourceManagement/allocationRequests',
            roles: [2, 3, 4, 5],
          },
          {
            title: 'Allocation Approvals',
            path: '/resourceManagement/allocationApprovals',
            roles: [2, 3],
          },
        ],
      },
      {
        title: 'Project Management',
        path: '/projectManagement',
        current: location.pathname === '/projectManagement',
        icon: ICONS.project,
        roles: [2, 3, 4, 5],
        children: [
          { title: 'Project List', path: '/projectManagement/projectList', roles: [2, 3, 4, 5] },
          { title: 'Add Project', path: '/projectManagement/addProject', roles: [3] },
          {
            title: 'Project Requests',
            path: '/projectManagement/projectRequests',
            roles: [2, 3, 4, 5],
          },
        ],
      },
    ],
  },
  // ANALYTICS
  {
    subheader: 'analytics',
    roles: [2, 3, 4, 5],
    items: [
      {
        title: 'Billability Summary',
        current: location.pathname === '/billabilitySummary',
        path: '/billabilitySummary',
        icon: ICONS.analytics,
        roles: [2, 3, 4, 5],
      },
    ],
  },
];

export default sidebarConfig;
