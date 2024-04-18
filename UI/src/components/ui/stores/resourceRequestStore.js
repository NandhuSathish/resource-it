import { create } from 'zustand';

const defaultResourceRequestQuery = {
  pageNumber: 0,
  pageSize: 20,
  searchKey: null,
  projectIds: null,
  departmentIds: null,
  approvalStatus: null,
};

const defaultResourceRequestFilters = {
  projectFilters: [],
  departmentFilters: [],
  approvalStatusFilters: [],
};

const useResourceRequestQueryStore = create((set) => ({
  resourceRequestQuery: {
    ...defaultResourceRequestQuery,
    approvalStatus: ['0'],
  },
  resourceRequestFilters: {
    ...defaultResourceRequestFilters,
    approvalStatusFilters: [{ value: '0', label: 'Pending' }],
  },

  setSearchText: (searchText) =>
    set((store) => ({
      resourceRequestQuery: {
        ...store.resourceRequestQuery,
        searchKey: searchText,
        pageNumber: 0,
        pageSize: store.resourceRequestQuery.pageSize,
      },
    })),

  setProjectFilter: (ProjectFilter) =>
    set((store) => ({
      resourceRequestQuery: {
        ...store.resourceRequestQuery,
        projectIds: ProjectFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.resourceRequestQuery.pageSize,
      },
      resourceRequestFilters: {
        ...store.resourceRequestFilters,
        projectFilters: ProjectFilter,
      },
    })),

  setDepartmentFilter: (departmentFilter) =>
    set((store) => ({
      resourceRequestQuery: {
        ...store.resourceRequestQuery,
        departmentIds: departmentFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.resourceRequestQuery.pageSize,
      },
      resourceRequestFilters: {
        ...store.resourceRequestFilters,
        departmentFilters: departmentFilter,
      },
    })),

  setApprovalStatusFilters: (approvalStatusFilter) =>
    set((store) => ({
      resourceRequestQuery: {
        ...store.resourceRequestQuery,
        approvalStatus: approvalStatusFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.resourceRequestQuery.pageSize,
      },
      resourceRequestFilters: {
        ...store.resourceRequestFilters,
        approvalStatusFilters: approvalStatusFilter,
      },
    })),

  setPageNumber: (page) =>
    set((store) => ({
      resourceRequestQuery: {
        ...store.resourceRequestQuery,
        pageNumber: page,
        searchText: undefined,
      },
    })),

  setPageSize: (pageSize) =>
    set((store) => ({
      resourceRequestQuery: { ...store.resourceRequestQuery, pageNumber: 0, pageSize },
    })),

  setClearAllFilters: () =>
    set(() => ({
      resourceRequestQuery: {
        ...defaultResourceRequestQuery,
      },
      resourceRequestFilters: {
        ...defaultResourceRequestFilters,
      },
    })),
}));

export default useResourceRequestQueryStore;
