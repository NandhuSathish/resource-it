import { create } from 'zustand';

const defaultProjectRequestQuery = {
  pageNumber: 0,
  pageSize: 20,
  managerId: null,
  projectType: null,
  approvalStatus: null,
};

const defaultProjectRequestFilters = {
  projectTypeFilters: [],
  managerFilters: [],
  approvalStatusFilters: [],
};

const useProjectRequestQueryStore = create((set) => ({
  projectRequestQuery: {
    ...defaultProjectRequestQuery,
    approvalStatus: ['0'],
  },
  projectRequestFilters: {
    ...defaultProjectRequestFilters,
    approvalStatusFilters: [{ value: '0', label: 'Pending' }],
  },

  setProjectTypeFilter: (ProjectTypeFilter) =>
    set((store) => ({
      projectRequestQuery: {
        ...store.projectRequestQuery,
        projectType: ProjectTypeFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.projectRequestQuery.pageSize,
      },
      projectRequestFilters: {
        ...store.projectRequestFilters,
        projectTypeFilters: ProjectTypeFilter,
      },
    })),

  setManagerFilter: (managerFilter) =>
    set((store) => ({
      projectRequestQuery: {
        ...store.projectRequestQuery,
        managerId: managerFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.projectRequestQuery.pageSize,
      },
      projectRequestFilters: {
        ...store.projectRequestFilters,
        managerFilters: managerFilter,
      },
    })),

  setApprovalStatusFilters: (approvalStatusFilter) =>
    set((store) => ({
      projectRequestQuery: {
        ...store.projectRequestQuery,
        approvalStatus: approvalStatusFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.projectRequestQuery.pageSize,
      },
      projectRequestFilters: {
        ...store.projectRequestFilters,
        approvalStatusFilters: approvalStatusFilter,
      },
    })),

  setPageNumber: (page) =>
    set((store) => ({
      projectRequestQuery: {
        ...store.projectRequestQuery,
        pageNumber: page,
        searchText: undefined,
      },
    })),

  setPageSize: (pageSize) =>
    set((store) => ({
      projectRequestQuery: { ...store.projectRequestQuery, pageNumber: 0, pageSize },
    })),

  setClearAllFilters: () =>
    set(() => ({
      projectRequestQuery: {
        ...defaultProjectRequestQuery,
      },
      projectRequestFilters: {
        ...defaultProjectRequestFilters,
      },
    })),
}));

export default useProjectRequestQueryStore;
