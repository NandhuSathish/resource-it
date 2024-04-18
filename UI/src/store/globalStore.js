import { create } from 'zustand';

const defaultGlobalQuery = {
  allocationSelectedTab: '1',
  allocationApprovalSelectedTab: '1',
  allocationInProjectTab: '1',
};

const useGlobalStore = create((set) => ({
  globalQuery: {
    ...defaultGlobalQuery,
  },

  setAllocationSelectedTab: (tab) =>
    set((store) => ({
      globalQuery: {
        ...store.globalQuery,
        allocationSelectedTab: tab.toString(),
      },
    })),

  setAllocationApprovalSelectedTab: (tab) =>
    set((store) => ({
      globalQuery: {
        ...store.globalQuery,
        allocationApprovalSelectedTab: tab.toString(),
      },
    })),

  setAllocationInProjectTab: (tab) =>
    set((store) => ({
      globalQuery: {
        ...store.globalQuery,
        allocationInProjectTab: tab.toString(),
      },
    })),
}));

export default useGlobalStore;
