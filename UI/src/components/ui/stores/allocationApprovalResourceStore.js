import dayjs from 'dayjs';
import { create } from 'zustand';

const defaultResourceQuery = {
  pageNumber: 0,
  pageSize: 20,
  status: '1', // 1 = Active, 0 = Inactive,
  skillsAndExperiences: [], // filters based on skill and experience
  departmentIds: [], // filters based on department
  experienceMinValue: '0', //experience range slider lower bond
  experienceMaxValue: '30', //experience range slider upper bond
  allocationStartDate: null, // start date for the resource allocation
  allocationEndDate: null, // end date for the resource allocation
  sortOrder: false,
  sortKey: null,
};

const defaultResourceFilters = {
  departmentFilters: [],
  ExperienceFilters: [0, 30],
  skillAndExperienceFilters: [{ skill: '', proficiency: [], experience: '' }],
  allocationStatusFilters: [],
};

const defaultRequestDetails = {
  requestId: null,
  projectId: null,
  departmentId: null,
  startDate: null,
  endDate: null,
  experience: null,
  count: null,
};

const defaultAllocationRequestQuery = {
  requestId: null,
  projectId: null,
  startDate: null,
  endDate: null,
  resources: [],
};

const useAllocationApprovalResourceQueryStore = create((set, get) => ({
  resourceQuery: {
    ...defaultResourceQuery,
  },
  resourceFilters: {
    ...defaultResourceFilters,
  },
  requestDetails: {
    ...defaultRequestDetails,
  },
  allocationRequestQuery: {
    ...defaultAllocationRequestQuery,
  },
  initialStateAfterSetRequestDetails: {},
  //set the request id for the request
  setRequestId: (requestId) =>
    set(() => ({
      requestDetails: {
        ...defaultRequestDetails,
        requestId: requestId,
      },
      allocationRequestQuery: {
        ...defaultAllocationRequestQuery,
        requestId: requestId,
      },
    })),

  setRequestDetails: (requestDetails) => {
    const store = get();
    const newState = {
      requestDetails: {
        ...store.requestDetails,
        projectId: requestDetails.projectId,
        departmentId: requestDetails.departmentId,
        startDate: dayjs(requestDetails.startDate, 'DD-MM-YYYY').toDate(),
        endDate: dayjs(requestDetails.endDate, 'DD-MM-YYYY').toDate(),
        experience: requestDetails.experience,
        count: requestDetails.count,
      },
      allocationRequestQuery: {
        ...store.allocationRequestQuery,
        projectId: requestDetails.projectId,
        startDate: requestDetails.startDate,
        endDate: requestDetails.endDate,
      },
      resourceQuery: {
        ...store.resourceQuery,
        experienceMinValue: requestDetails.experience,
        allocationStatus: ['0'],
        departmentIds: [requestDetails.departmentId],
        skillsAndExperiences: requestDetails.skillExperienceResponseDTOs.map((filter) => ({
          skillId: String(filter.skillId),
          skillMinValue: String(filter.skillMinValue),
          skillMaxValue: String(filter.skillMaxValue),
          proficiency: filter.proficiency,
        })),
        allocationStartDate: dayjs(requestDetails.startDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
        allocationEndDate: dayjs(requestDetails.endDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
      },
      resourceFilters: {
        ...store.resourceFilters,
        ExperienceFilters: [requestDetails.experience, 30],
        allocationStatusFilters: [{ value: 0, label: 'Bench' }],
        skillAndExperienceFilters: requestDetails.skillExperienceResponseDTOs.map((filter) => ({
          skill: filter.skillId,
          experience: [filter.skillMinValue, filter.skillMaxValue],
          proficiency: filter.proficiency,
        })),
      },
    };

    set(() => ({
      ...newState,
      initialStateAfterSetRequestDetails: JSON.parse(JSON.stringify(newState)),
    }));
  },

  reset: () => {
    const { initialStateAfterSetRequestDetails } = get();
    set(() => ({
      ...initialStateAfterSetRequestDetails,
      resourceQuery: {
        ...initialStateAfterSetRequestDetails.resourceQuery,
        skillsAndExperiences:
          initialStateAfterSetRequestDetails.resourceQuery.skillsAndExperiences.map((filter) => ({
            ...filter,
          })),
      },
      resourceFilters: {
        ...initialStateAfterSetRequestDetails.resourceFilters,
        skillAndExperienceFilters:
          initialStateAfterSetRequestDetails.resourceFilters.skillAndExperienceFilters.map(
            (filter) => ({
              ...filter,
            })
          ),
      },
    }));
  },
  //set the resources for the request
  setResources: (resources) =>
    set((store) => ({
      allocationRequestQuery: {
        ...store.allocationRequestQuery,
        resources: resources,
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
        experienceMinValue: experienceFilter[0],
        experienceMaxValue: experienceFilter[1],
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
    set(() => ({
      resourceQuery: {
        ...defaultResourceQuery,
      },
      resourceFilters: {
        ...defaultResourceFilters,
      },
    })),
}));

export default useAllocationApprovalResourceQueryStore;
