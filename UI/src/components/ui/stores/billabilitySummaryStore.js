import dayjs from 'dayjs';
import { create } from 'zustand';

const defaultBillabilitySummaryQuery = {
  pageNumber: 0,
  pageSize: 20,
  status: '1', // 1 = Active, 0 = Inactive,
  skillAndExperiences: [], // filters based on skill and experience
  departmentIds: [], // filters based on department
  projectIds: null, // filters based on project
  allocationType: [], // filters based on allocation type like billable,internal, bench etc
  lowerExperience: '0', //experience range slider lower bond
  highExperience: '30', //experience range slider upper bond
  name: null, //name serach
  employeeId: null,
  startDate: dayjs().startOf('month').format('YYYY-MM-DD'),
  endDate: dayjs().endOf('month').format('YYYY-MM-DD'),
  sortOrder: false,
  sortKey: null,
};

const defaultBillabilitySummaryFilters = {
  departmentFilters: [],
  billabilitySummaryFilters: [],
  ExperienceFilters: [0, 30],
  skillAndExperienceFilters: [{ skill: '', proficiency: [], experience: [0, 30] }],
  allocationStatusFilters: [],
  StatusFilters: [],
  startDateFilters: dayjs().startOf('month').format('YYYY-MM-DD'),
  endDateFilters: dayjs().endOf('month').format('YYYY-MM-DD'),
};

const useBillabilitySummaryQueryStore = create((set) => ({
  billabilitySummaryQuery: {
    ...defaultBillabilitySummaryQuery,
  },

  billabilitySummaryFilters: {
    ...defaultBillabilitySummaryFilters,
  },

  setSearchText: (searchText) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        name: searchText,
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
    })),

  setStartDateFilter: (startDateFilter) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        startDate: startDateFilter ? dayjs(startDateFilter).format('YYYY-MM-DD') : null,
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
      billabilitySummaryFilters: {
        ...store.billabilitySummaryFilters,
        startDateFilters: startDateFilter,
      },
    })),

  setEndDateFilter: (endDateFilter) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        endDate: endDateFilter ? dayjs(endDateFilter).format('YYYY-MM-DD') : null,
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
      billabilitySummaryFilters: {
        ...store.billabilitySummaryFilters,
        endDateFilters: endDateFilter,
      },
    })),

  setDepartmentFilter: (departmentFilter) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        departmentIds: departmentFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
      billabilitySummaryFilters: {
        ...store.billabilitySummaryFilters,
        departmentFilters: departmentFilter,
      },
    })),

  setExperienceFilter: (experienceFilter) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        lowerExperience: experienceFilter[0],
        highExperience: experienceFilter[1],
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
      billabilitySummaryFilters: {
        ...store.billabilitySummaryFilters,
        ExperienceFilters: experienceFilter,
      },
    })),

  setSkillExpereinceFilter: (skillExperienceFilter) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        skillAndExperiences:
          skillExperienceFilter[0].skill === ''
            ? []
            : skillExperienceFilter.map((filter) => ({
                skillId: String(filter.skill),
                skillMinValue: String(filter.experience[0]),
                skillMaxValue: String(filter.experience[1]),
                proficiency: filter.proficiency,
              })),
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
      billabilitySummaryFilters: {
        ...store.billabilitySummaryFilters,
        skillAndExperienceFilters: skillExperienceFilter,
      },
    })),

  setProjectFilter: (projectFilter) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        projectIds: projectFilter.length ? projectFilter.map((filter) => filter.value) : null,
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
      billabilitySummaryFilters: {
        ...store.billabilitySummaryFilters,
        billabilitySummaryFilters: projectFilter,
      },
    })),

  setPageNumber: (page) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        pageNumber: page,
        searchText: undefined,
      },
    })),

  setPageSize: (pageSize) =>
    set((store) => ({
      billabilitySummaryQuery: { ...store.billabilitySummaryQuery, pageNumber: 0, pageSize },
    })),

  setStatus: (status) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        status,
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
    })),

  setSortOrder: (sortOrder) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        sortOrder,
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
    })),

  setSortKey: (sortKey) =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...store.billabilitySummaryQuery,
        sortKey,
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
    })),

  setClearAllFilters: () =>
    set((store) => ({
      billabilitySummaryQuery: {
        ...defaultBillabilitySummaryQuery,
        name: store.billabilitySummaryQuery.name,
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
      billabilitySummaryFilters: {
        ...defaultBillabilitySummaryFilters,
        pageNumber: 0,
        pageSize: store.billabilitySummaryQuery.pageSize,
      },
    })),
}));

export default useBillabilitySummaryQueryStore;
