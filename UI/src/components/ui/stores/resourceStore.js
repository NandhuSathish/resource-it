import { create } from 'zustand';

const defaultResourceQuery = {
  pageNumber: 0,
  pageSize: 20,
  status: '1', // 1 = Active, 0 = Inactive,
  allocationStatus: [], // filters based on allocation status
  skillAndExperiences: [], // filters based on skill and experience
  departmentIds: [], // filters based on department
  projectIds: null, // filters based on project
  allocationType: [], // filters based on allocation type like billable,internal, bench etc
  lowerExperience: '0', //experience range slider lower bond
  highExperience: '30', //experience range slider upper bond
  name: null, //name serach
  employeeId: null,
  sortOrder: false,
  sortKey: null,
};

const defaultResourceFilters = {
  departmentFilters: [],
  projectFilters: [],
  ExperienceFilters: [0, 30],
  skillAndExperienceFilters: [{ skill: '', proficiency:[], experience: [0, 30] }],
  allocationStatusFilters: [],
  StatusFilters: [],
};

const useResourceQueryStore = create((set) => ({
  resourceQuery: {
    ...defaultResourceQuery,
  },

  resourceFilters: {
    ...defaultResourceFilters,
  },

  setSearchText: (searchText) =>
    set((store) => ({
      resourceQuery: {
        ...store.resourceQuery,
        name: searchText,
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
    })),

  setDepartmentFilter: (departmentFilter) =>
    set((store) => ({
      resourceQuery: {
        ...store.resourceQuery,
        departmentIds: departmentFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
      resourceFilters: {
        ...store.resourceFilters,
        departmentFilters: departmentFilter,
      },
    })),

  setExperienceFilter: (experienceFilter) =>
    set((store) => ({
      resourceQuery: {
        ...store.resourceQuery,
        lowerExperience: experienceFilter[0],
        highExperience: experienceFilter[1],
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
      resourceFilters: {
        ...store.resourceFilters,
        ExperienceFilters: experienceFilter,
      },
    })),

  setAllocationStatusFilter: (allocationStatusFilter) =>
    set((store) => ({
      resourceQuery: {
        ...store.resourceQuery,
        allocationStatus: allocationStatusFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
      resourceFilters: {
        ...store.resourceFilters,
        allocationStatusFilters: allocationStatusFilter,
      },
    })),

  setSkillExpereinceFilter: (skillExperienceFilter) =>
    set((store) => ({
      resourceQuery: {
        ...store.resourceQuery,
        skillAndExperiences:
          skillExperienceFilter[0].skill === ''
            ? []
            : skillExperienceFilter.map((filter) => ({
                skillId: String(filter.skill),
                skillMinValue: String(filter.experience[0]),
                skillMaxValue: String(filter.experience[1]),
                proficiency: filter.proficiency
                
              })),
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
      resourceFilters: {
        ...store.resourceFilters,
        skillAndExperienceFilters: skillExperienceFilter,
      },
    })),

  setProjectFilter: (projectFilter) =>
    set((store) => ({
      resourceQuery: {
        ...store.resourceQuery,
        projectIds: projectFilter.length ? projectFilter.map((filter) => filter.value) : null,
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
      resourceFilters: {
        ...store.resourceFilters,
        projectFilters: projectFilter,
      },
    })),

  setPageNumber: (page) =>
    set((store) => ({
      resourceQuery: { ...store.resourceQuery, pageNumber: page, searchText: undefined },
    })),

  setPageSize: (pageSize) =>
    set((store) => ({
      resourceQuery: { ...store.resourceQuery, pageNumber: 0, pageSize },
    })),

  setStatus: (status) =>
    set((store) => ({
      resourceQuery: {
        ...store.resourceQuery,
        status,
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
    })),

  setSortOrder: (sortOrder) =>
    set((store) => ({
      resourceQuery: {
        ...store.resourceQuery,
        sortOrder,
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
    })),

  setSortKey: (sortKey) =>
    set((store) => ({
      resourceQuery: {
        ...store.resourceQuery,
        sortKey,
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
    })),

  setClearAllFilters: () =>
    set((store) => ({
      resourceQuery: {
        ...defaultResourceQuery,
        name: store.resourceQuery.name,
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
      resourceFilters: {
        ...defaultResourceFilters,
        pageNumber: 0,
        pageSize: store.resourceQuery.pageSize,
      },
    })),
}));

export default useResourceQueryStore;
