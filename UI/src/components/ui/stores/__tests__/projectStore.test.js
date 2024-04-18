import { describe, it, expect } from 'vitest';
import useProjectQueryStore from '../projectStore';

describe('useProjectQueryStore', () => {
  it('sets the search text', () => {
    useProjectQueryStore.setState({ projectQuery: { projectName: null } });

    useProjectQueryStore.getState().setSearchText('test');

    expect(useProjectQueryStore.getState().projectQuery.projectName).toBe('test');
  });

  it('sets the page number', () => {
    useProjectQueryStore.getState().setPageNumber(1);
    expect(useProjectQueryStore.getState().projectQuery.pageNumber).toBe(1);
  });

  it('sets the page size', () => {
    useProjectQueryStore.getState().setPageSize(50);
    expect(useProjectQueryStore.getState().projectQuery.pageSize).toBe(50);
  });

  it('sets the sort order', () => {
    useProjectQueryStore.getState().setSortOrder(true);
    expect(useProjectQueryStore.getState().projectQuery.sortOrder).toBe(true);
  });

  it('sets the sort key', () => {
    useProjectQueryStore.getState().setSortKey('test');
    expect(useProjectQueryStore.getState().projectQuery.sortKey).toBe('test');
  });

  it('sets the project type filter', () => {
    // Arrange
    const initialProjectQuery = {
      projectType: null,
      pageNumber: 0,
      pageSize: 20,
    };
    const initialProjectFilters = {
      projectTypeFilters: [],
    };
    useProjectQueryStore.setState({
      projectQuery: initialProjectQuery,
      projectFilters: initialProjectFilters,
    });

    // Act
    const newProjectTypeFilter = [{ value: 'test' }];
    useProjectQueryStore.getState().setProjectTypeFilter(newProjectTypeFilter);

    // Assert
    expect(useProjectQueryStore.getState().projectQuery.projectType).toEqual(['test']);
    expect(useProjectQueryStore.getState().projectFilters.projectTypeFilters).toEqual(
      newProjectTypeFilter
    );
  });

  it('sets the manager filter', () => {
    const initialProjectQuery = {
      managerId: null,
      pageNumber: 0,
      pageSize: 20,
    };
    const initialProjectFilters = {
      managerFilters: [],
    };
    useProjectQueryStore.setState({
      projectQuery: initialProjectQuery,
      projectFilters: initialProjectFilters,
    });

    const newManagerFilter = [{ value: 'test' }];
    useProjectQueryStore.getState().setManagerFilter(newManagerFilter);

    expect(useProjectQueryStore.getState().projectQuery.managerId).toEqual(['test']);
    expect(useProjectQueryStore.getState().projectFilters.managerFilters).toEqual(newManagerFilter);
  });

  it('sets the start date filter', () => {
    const initialProjectQuery = {
      startDate: null,
      pageNumber: 0,
      pageSize: 20,
    };
    const initialProjectFilters = {
      startDateFilters: '',
    };
    useProjectQueryStore.setState({
      projectQuery: initialProjectQuery,
      projectFilters: initialProjectFilters,
    });

    const newStartDateFilter = '2022-01-01';
    useProjectQueryStore.getState().setStartDateFilter(newStartDateFilter);

    expect(useProjectQueryStore.getState().projectQuery.startDate).toBe('01-01-2022');
    expect(useProjectQueryStore.getState().projectFilters.startDateFilters).toBe(
      newStartDateFilter
    );
  });

  it('sets the end date filter', () => {
    const initialProjectQuery = {
      endDate: null,
      pageNumber: 0,
      pageSize: 20,
    };
    const initialProjectFilters = {
      endDateFilters: '',
    };
    useProjectQueryStore.setState({
      projectQuery: initialProjectQuery,
      projectFilters: initialProjectFilters,
    });

    const newEndDateFilter = '2022-12-31';
    useProjectQueryStore.getState().setEndDateFilter(newEndDateFilter);

    expect(useProjectQueryStore.getState().projectQuery.endDate).toBe('31-12-2022');
    expect(useProjectQueryStore.getState().projectFilters.endDateFilters).toBe(newEndDateFilter);
  });

  it('sets the project status filter', () => {
    const initialProjectQuery = {
      projectState: null,
      pageNumber: 0,
      pageSize: 20,
    };
    const initialProjectFilters = {
      projectStatusFilters: [],
    };
    useProjectQueryStore.setState({
      projectQuery: initialProjectQuery,
      projectFilters: initialProjectFilters,
    });

    const newProjectStatusFilter = [{ value: 'test' }];
    useProjectQueryStore.getState().setProjectStatusFilter(newProjectStatusFilter);

    expect(useProjectQueryStore.getState().projectQuery.projectState).toEqual(['test']);
    expect(useProjectQueryStore.getState().projectFilters.projectStatusFilters).toEqual(
      newProjectStatusFilter
    );
  });
});
