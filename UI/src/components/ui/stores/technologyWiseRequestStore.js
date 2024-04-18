import { create } from 'zustand';

const defaultTechnologyWiseRequestQuery = {
  pageNumber: 0,
  pageSize: 20,
  projectIds: null,
  departmentIds: null,
  approvalStatus: [],
};

const defaultTechnologyWiseRequestFilters = {
  projectFilters: [],
  departmentFilters: [],
  approvalStatusFilters: [],
};

const useTechnologyWiseRequestQueryStore = create((set) => ({
  technologyWiseRequestQuery: {
    ...defaultTechnologyWiseRequestQuery,
    approvalStatus: ['0'],
  },
  technologyWiseRequestFilters: {
    ...defaultTechnologyWiseRequestFilters,
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
        ...defaultTechnologyWiseRequestQuery,
      },
      technologyWiseRequestFilters: {
        ...defaultTechnologyWiseRequestFilters,
      },
    })),
}));

export default useTechnologyWiseRequestQueryStore;
