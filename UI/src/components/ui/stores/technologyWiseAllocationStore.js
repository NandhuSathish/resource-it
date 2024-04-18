import dayjs from 'dayjs';
import { create } from 'zustand';

const defaultProjectQuery = {
  projectId: null,
  projectStartDate: null,
  projectEndDate: null,
  departmentId: null,
  skillExperience: [{ skillId: '', proficiency: [], experience: [0, 30] }],
};

const defaultTechnologyWiseAllocationRequestQuery = {
  projectId: null,
  departmentId: null,
  count: null,
  experience: null,
  startDate: null,
  endDate: null,
  skillExperienceRequestDTO: [],
};

const useTechnologyWiseAllocationQueryStore = create((set) => ({
  //store used for project related date restriction
  projectQuery: {
    ...defaultProjectQuery,
  },
  //store used for allocation request body
  technologyWiseAllocationRequestQuery: {
    ...defaultTechnologyWiseAllocationRequestQuery,
  },

  setProjectId: (projectId) =>
    set(() => ({
      projectQuery: {
        ...defaultProjectQuery,
        projectId: projectId,
      },
      technologyWiseAllocationRequestQuery: {
        ...defaultTechnologyWiseAllocationRequestQuery,
        projectId: projectId.value,
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

  setDepartmentId: (departmentId) =>
    set((store) => ({
      technologyWiseAllocationRequestQuery: {
        ...store.technologyWiseAllocationRequestQuery,
        departmentId: departmentId.value,
      },
      projectQuery: {
        ...store.projectQuery,
        departmentId: departmentId,
      },
    })),

  setCount: (count) =>
    set((store) => ({
      technologyWiseAllocationRequestQuery: {
        ...store.technologyWiseAllocationRequestQuery,
        count: count,
      },
    })),

  setExperience: (experience) =>
    set((store) => ({
      technologyWiseAllocationRequestQuery: {
        ...store.technologyWiseAllocationRequestQuery,
        experience: experience,
      },
    })),

  setAllocationStartDate: (startDate) =>
    set((store) => ({
      technologyWiseAllocationRequestQuery: {
        ...store.technologyWiseAllocationRequestQuery,
        startDate: dayjs(startDate, 'DD-MM-YYYY').toDate(),
        // startDate: startDate,
      },
    })),
  setAllocationEndDate: (endDate) =>
    set((store) => ({
      technologyWiseAllocationRequestQuery: {
        ...store.technologyWiseAllocationRequestQuery,
        endDate: dayjs(endDate, 'DD-MM-YYYY').toDate(),
      },
    })),

  setSkillExperience: (skillExperience) =>
    set((store) => ({
      technologyWiseAllocationRequestQuery: {
        ...store.technologyWiseAllocationRequestQuery,
        skillExperienceRequestDTO: skillExperience.map((filter) => ({
          skillId: String(filter.skillId),
          skillMinValue: String(filter.experience[0]),
          skillMaxValue: String(filter.experience[1]),
          proficiency: filter.proficiency,
        })),
      },
      projectQuery: {
        ...store.projectQuery,
        skillExperience: skillExperience,
      },
    })),

  setClearAllOnRequest: () =>
    set(() => ({
      technologyWiseAllocationRequestQuery: {
        ...defaultTechnologyWiseAllocationRequestQuery,
      },
      projectQuery: {
        ...defaultProjectQuery,
      },
    })),
}));

export default useTechnologyWiseAllocationQueryStore;
