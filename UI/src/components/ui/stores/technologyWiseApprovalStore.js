import { create } from 'zustand';

const defaultTechnologyWiseApprovalQuery = {
  pageNumber: 0,
  pageSize: 20,
  projectIds: null,
  departmentIds: null,
  managerIds: null,
  approvalStatus: [],
};

const defaultTechnologyWiseApprovalFilters = {
  projectFilters: [],
  departmentFilters: [],
  managerFilters: [],
  approvalStatusFilters: [],
};

const useTechnologyWiseApprovalQueryStore = create((set) => ({
  technologyWiseRequestQuery: {
    ...defaultTechnologyWiseApprovalQuery,
    approvalStatus: ['0'],
  },
  technologyWiseRequestFilters: {
    ...defaultTechnologyWiseApprovalFilters,
    approvalStatusFilters: [{ value: '0', label: 'Pending' }],
  },

  setProjectFilter: (ProjectFilter) =>
    set((store) => ({
      technologyWiseRequestQuery: {
        ...store.technologyWiseRequestQuery,
        projectIds: ProjectFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.technologyWiseRequestQuery.pageSize,
      },
      technologyWiseRequestFilters: {
        ...store.technologyWiseRequestFilters,
        projectFilters: ProjectFilter,
      },
    })),

  setManagerFilter: (managerFilter) =>
    set((store) => ({
      technologyWiseRequestQuery: {
        ...store.technologyWiseRequestQuery,
        managerIds: managerFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.technologyWiseRequestQuery.pageSize,
      },
      technologyWiseRequestFilters: {
        ...store.technologyWiseRequestFilters,
        managerFilters: managerFilter,
      },
    })),

  setDepartmentFilter: (departmentFilter) =>
    set((store) => ({
      technologyWiseRequestQuery: {
        ...store.technologyWiseRequestQuery,
        departmentIds: departmentFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.technologyWiseRequestQuery.pageSize,
      },
      technologyWiseRequestFilters: {
        ...store.technologyWiseRequestFilters,
        departmentFilters: departmentFilter,
      },
    })),

  setApprovalStatusFilters: (approvalStatusFilter) =>
    set((store) => ({
      technologyWiseRequestQuery: {
        ...store.technologyWiseRequestQuery,
        approvalStatus: approvalStatusFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.technologyWiseRequestQuery.pageSize,
      },
      technologyWiseRequestFilters: {
        ...store.technologyWiseRequestFilters,
        approvalStatusFilters: approvalStatusFilter,
      },
    })),

  setPageNumber: (page) =>
    set((store) => ({
      technologyWiseRequestQuery: {
        ...store.technologyWiseRequestQuery,
        pageNumber: page,
        searchText: undefined,
      },
    })),

  setPageSize: (pageSize) =>
    set((store) => ({
      technologyWiseRequestQuery: { ...store.technologyWiseRequestQuery, pageNumber: 0, pageSize },
    })),

  setClearAllFilters: () =>
    set(() => ({
      technologyWiseRequestQuery: {
        ...defaultTechnologyWiseApprovalQuery,
      },
      technologyWiseRequestFilters: {
        ...defaultTechnologyWiseApprovalFilters,
      },
    })),
}));

export default useTechnologyWiseApprovalQueryStore;
