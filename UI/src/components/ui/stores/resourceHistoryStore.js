import { create } from 'zustand';
import dayjs from 'dayjs';


const defaultProjectQuery = {
  pageNumber: 0,
  pageSize: 20,
  startDate: dayjs().startOf('month').format('YYYY-MM-DD'),
  endDate:  dayjs().endOf('month').format('YYYY-MM-DD'),
  sortOrder: false,
  sortKey: null,
};

const defaultResourceDetailsQuery = {
  startDate:  dayjs().startOf('month').format('YYYY-MM-DD'),
  endDate: dayjs().endOf('month').format('YYYY-MM-DD')
};




const defaultProjectFilters = {
  startDateFilters: dayjs().startOf('month').format('YYYY-MM-DD'),
  endDateFilters: dayjs().endOf('month').format('YYYY-MM-DD')
};

const useResourceHistoryQueryStore = create((set) => ({
  projectQuery: {
    ...defaultProjectQuery,
  },

  projectFilters: {
    ...defaultProjectFilters,
  },

  resourceDetailsQuery:{
  ...defaultResourceDetailsQuery
  },
  projectReload: {
    reloadStatus: false,
  },


  setStartDateFilter: (startDateFilter) =>
    set((store) => ({
     
      projectQuery: {
        ...store.projectQuery,
        startDate: startDateFilter ? dayjs(startDateFilter).format('YYYY-MM-DD')  : null,
        pageNumber: 0,
        pageSize: store.projectQuery.pageSize,
      },

      resourceDetailsQuery:{

        ...store.resourceDetailsQuery,
        startDate: startDateFilter ? dayjs(startDateFilter).format('YYYY-MM-DD')  : null,

      },
      projectFilters: {
        ...store.projectFilters,
        startDateFilters: startDateFilter,
      },

        projectReload: {
    reloadStatus: true,
  },
    })),

  setEndDateFilter: (endDateFilter) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        endDate: endDateFilter ? dayjs(endDateFilter).format('YYYY-MM-DD')  : null,
        pageNumber: 0,
        pageSize: store.projectQuery.pageSize,
      },
      
      resourceDetailsQuery:{
        ...store.resourceDetailsQuery,
        endDate: endDateFilter ? dayjs(endDateFilter).format('YYYY-MM-DD')  : null,
      },


      projectFilters: {
        ...store.projectFilters,
        endDateFilters: endDateFilter,
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
    set(() => ({
      projectQuery: {
        ...defaultProjectQuery,

      },
      projectFilters: {
        ...defaultProjectFilters,
      },

         resourceDetailsQuery:{
        ...defaultResourceDetailsQuery
      
      },
    })),
}));

export default useResourceHistoryQueryStore;
