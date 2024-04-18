import { create } from 'zustand';
import dayjs from 'dayjs';

const defaultProjectQuery = {
  pageNumber: 0,
  pageSize: 20,
  projectName: null, //name serach
  startDate: null,
  endDate: null,
  projectState: null,
  managerId: null,
  projectType: null,
  sortOrder: false,
  sortKey: null,
};

const defaultProjectFilters = {
  projectTypeFilters: [],
  managerFilters: [],
  startDateFilters: '',
  endDateFilters: '',
  projectStatusFilters: [],
};

const useProjectQueryStore = create((set) => ({
  projectQuery: {
    ...defaultProjectQuery,
  },

  projectFilters: {
    ...defaultProjectFilters,
  },

  projectReload: {
    reloadStatus: false,
  },

  setProjectReload: (status) =>
    set(() => ({
      projectReload: {
        reloadStatus: status,
      },
    })),

  setSearchText: (searchText) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        projectName: searchText,
        pageNumber: 0,
        pageSize: store.projectQuery.pageSize,
      },
    })),

  setProjectTypeFilter: (ProjectTypeFilter) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        projectType: ProjectTypeFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.projectQuery.pageSize,
      },
      projectFilters: {
        ...store.projectFilters,
        projectTypeFilters: ProjectTypeFilter,
      },
    })),

  setManagerFilter: (managerFilter) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        managerId: managerFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.projectQuery.pageSize,
      },
      projectFilters: {
        ...store.projectFilters,
        managerFilters: managerFilter,
      },
    })),

  setStartDateFilter: (startDateFilter) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        startDate: startDateFilter ? dayjs(startDateFilter).format('DD-MM-YYYY') : null,
        pageNumber: 0,
        pageSize: store.projectQuery.pageSize,
      },
      projectFilters: {
        ...store.projectFilters,
        startDateFilters: startDateFilter,
      },
    })),

  setEndDateFilter: (endDateFilter) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        endDate: endDateFilter ? dayjs(endDateFilter).format('DD-MM-YYYY') : null,
        pageNumber: 0,
        pageSize: store.projectQuery.pageSize,
      },
      projectFilters: {
        ...store.projectFilters,
        endDateFilters: endDateFilter,
      },
    })),

  setProjectStatusFilter: (projectStatusFilter) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        projectState: projectStatusFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.projectQuery.pageSize,
      },
      projectFilters: {
        ...store.projectFilters,
        projectStatusFilters: projectStatusFilter,
      },
    })),

  setPageNumber: (page) =>
    set((store) => ({
      projectQuery: { ...store.projectQuery, pageNumber: page, searchText: undefined },
    })),

  setPageSize: (pageSize) =>
    set((store) => ({
      projectQuery: { ...store.projectQuery, pageNumber: 0, pageSize },
    })),

  setSortOrder: (sortOrder) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        sortOrder,
        pageNumber: 0,
        pageSize: store.projectQuery.pageSize,
      },
    })),

  setSortKey: (sortKey) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        sortKey,
        pageNumber: 0,
        pageSize: store.projectQuery.pageSize,
      },
    })),

  setClearAllFilters: () =>
    set((store) => ({
      projectQuery: {
        ...defaultProjectQuery,
        projectName: store.projectQuery.projectName,
      },
      projectFilters: {
        ...defaultProjectFilters,
      },
    })),
}));

export default useProjectQueryStore;
