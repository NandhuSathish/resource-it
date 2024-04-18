import dayjs from 'dayjs';
import { create } from 'zustand';

const defaultResourceAllocationQuery = {
  pageNumber: 0,
  pageSize: 5,
  allocationStartDate: null,
  allocationEndDate: null,
  resourceName: null, //name serach
  skillsAndExperiences: [], // filters based on skill and experience
  departmentIds: [], // filters based on department
  experienceMinValue: '0', // filters based on experience
  experienceMaxValue: '30', // filters based on experience
  sortOrder: false,
  sortKey: null,
};

const defaultResourceAllocationFilters = {
  name: null,
  departmentFilters: [],
  skillAndExperienceFilters: [{ skill: '', proficiency: [], experience: [0, 30] }],
  allocationStatusFilters: [],
  ExperienceFilters: [0, 30],
};

const defaultProjectQuery = {
  projectId: null,
  projectStartDate: null,
  projectEndDate: null,
};

const defaultAllocationRequestQuery = {
  projectId: null,
  startDate: null,
  endDate: null,
  resources: [],
};

const useResourceAllocationQueryStore = create((set) => ({
  //store used to handle resource list table in the allocation request page.
  resourceAllocationQuery: {
    ...defaultResourceAllocationQuery,
  },
  //store used to handle resource list table filters in the allocation request page.
  resourceAllocationFilters: {
    ...defaultResourceAllocationFilters,
  },
  //store used for project related date restriction
  projectQuery: {
    ...defaultProjectQuery,
  },
  //store used for allocation request body
  allocationRequestQuery: {
    ...defaultAllocationRequestQuery,
  },

  setProjectId: (projectId) =>
    set((store) => ({
      projectQuery: {
        ...defaultProjectQuery,
        projectId: projectId,
      },
      allocationRequestQuery: {
        ...defaultAllocationRequestQuery,
        projectId: projectId.value,
      },
      resourceAllocationQuery: {
        ...store.resourceAllocationQuery,
        allocationStartDate: null,
        allocationEndDate: null,
        pageNumber: 0,
        pageSize: store.resourceAllocationQuery.pageSize,
      },
    })),

  setProjectStartDate: (projectStartDate) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        projectStartDate: dayjs(projectStartDate, 'DD-MM-YYYY').toDate(),
      },
    })),
  setProjectEndDate: (projectEndDate) =>
    set((store) => ({
      projectQuery: {
        ...store.projectQuery,
        projectEndDate: dayjs(projectEndDate, 'DD-MM-YYYY').toDate(),
      },
    })),

  setAllocationStartDate: (startDate) =>
    set((store) => ({
      allocationRequestQuery: {
        ...store.allocationRequestQuery,
        startDate: dayjs(startDate, 'DD-MM-YYYY').toDate(),
      },
      resourceAllocationQuery: {
        ...store.resourceAllocationQuery,
        allocationStartDate: dayjs(startDate).format('YYYY-MM-DD'),
        pageNumber: 0,
        pageSize: store.resourceAllocationQuery.pageSize,
      },
    })),

  setAllocationEndDate: (endDate) =>
    set((store) => ({
      allocationRequestQuery: {
        ...store.allocationRequestQuery,
        endDate: dayjs(endDate, 'DD-MM-YYYY').toDate(),
      },
      resourceAllocationQuery: {
        ...store.resourceAllocationQuery,
        allocationEndDate: dayjs(endDate).format('YYYY-MM-DD'),
        pageNumber: 0,
        pageSize: store.resourceAllocationQuery.pageSize,
      },
    })),

  setResources: (resource) =>
    set((store) => ({
      allocationRequestQuery: {
        ...store.allocationRequestQuery,
        resources: resource,
      },
    })),

  //filter related setters.
  setResourceName: (name) =>
    set((store) => ({
      resourceAllocationQuery: {
        ...store.resourceAllocationQuery,
        resourceName: name,
        pageNumber: 0,
        pageSize: store.resourceAllocationQuery.pageSize,
      },
    })),

  setDepartmentFilter: (departmentFilter) =>
    set((store) => ({
      resourceAllocationQuery: {
        ...store.resourceAllocationQuery,
        departmentIds: departmentFilter.map((filter) => filter.value),
        pageNumber: 0,
        pageSize: store.resourceAllocationQuery.pageSize,
      },
      resourceAllocationFilters: {
        ...store.resourceAllocationFilters,
        departmentFilters: departmentFilter,
      },
    })),

  setExperienceFilter: (experienceFilter) =>
    set((store) => ({
      resourceAllocationQuery: {
        ...store.resourceAllocationQuery,
        experienceMinValue: experienceFilter[0],
        experienceMaxValue: experienceFilter[1],
        pageNumber: 0,
        pageSize: store.resourceAllocationQuery.pageSize,
      },
      resourceAllocationFilters: {
        ...store.resourceAllocationFilters,
        ExperienceFilters: experienceFilter,
      },
    })),

  setSkillExpereinceFilter: (skillExperienceFilter) =>
    set((store) => ({
      resourceAllocationQuery: {
        ...store.resourceAllocationQuery,
        skillsAndExperiences:
          skillExperienceFilter[0].skill === ''
            ? []
            : skillExperienceFilter.map((filter) => ({
                skillId: String(filter.skill),
                skillMinValue: String(filter.experience[0]),
                skillMaxValue: String(filter.experience[1]),
                proficiency: filter.proficiency,
              })),
        pageNumber: 0,
        pageSize: store.resourceAllocationQuery.pageSize,
      },
      resourceAllocationFilters: {
        ...store.resourceAllocationFilters,
        skillAndExperienceFilters: skillExperienceFilter,
      },
    })),

  setPageNumber: (page) =>
    set((store) => ({
      resourceAllocationQuery: {
        ...store.resourceAllocationQuery,
        pageNumber: page,
        searchText: undefined,
      },
    })),

  setPageSize: (pageSize) =>
    set((store) => ({
      resourceAllocationQuery: { ...store.resourceAllocationQuery, pageNumber: 0, pageSize },
    })),

  setClearAllFilters: () =>
    set((store) => ({
      resourceAllocationQuery: {
        ...defaultResourceAllocationQuery,
        allocationStartDate: store.resourceAllocationQuery.allocationStartDate,
        allocationEndDate: store.resourceAllocationQuery.allocationEndDate,
      },
      resourceAllocationFilters: {
        ...defaultResourceAllocationFilters,
      },
    })),

  setClearAllOnRequest: () =>
    set(() => ({
      allocationRequestQuery: {
        ...defaultAllocationRequestQuery,
      },
      projectQuery: {
        ...defaultProjectQuery,
      },
    })),
}));

export default useResourceAllocationQueryStore;
