import { create } from 'zustand';

const defaultProjectAllocationEditQuery = {
  page: 0,
  size: 20,
  isExpired: false,
};

const useProjectAllocationEditQueryStore = create((set) => ({
  projectAllocationEditQuery: {
    ...defaultProjectAllocationEditQuery,
  },

  setPageNumber: (number) =>
    set((store) => ({
      projectAllocationEditQuery: {
        ...store.projectAllocationEditQuery,
        page: number,
      },
    })),

  setIsExpired: (value) =>
    set((store) => ({
      projectAllocationEditQuery: {
        ...store.projectAllocationEditQuery,
        isExpired: value,
      },
    })),

  setPageSize: (size) =>
    set((store) => ({
      projectAllocationEditQuery: { ...store.projectAllocationEditQuery, page: 0, size },
    })),
}));

export default useProjectAllocationEditQueryStore;
