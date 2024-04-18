import { act } from 'react-dom/test-utils';
import { describe, it, expect } from 'vitest';
import { renderHook } from '@testing-library/react-hooks';
import useResourceQueryStore from '../resourceStore';

describe('useResoureQueryStore', () => {
  it('should update search text', () => {
    const { result } = renderHook(() => useResourceQueryStore());
    act(() => {
      result.current.setSearchText('test');
    });
    expect(result.current.resourceQuery.name).toBe('test');
  });

  it('should update department filter', () => {
    const { result } = renderHook(() => useResourceQueryStore());
    act(() => {
      result.current.setDepartmentFilter([{ value: 'dept1' }, { value: 'dept2' }]);
    });
    expect(result.current.resourceQuery.departmentIds).toEqual(['dept1', 'dept2']);
  });

  it('should update allocation status filter', () => {
    const { result } = renderHook(() => useResourceQueryStore());
    act(() => {
      result.current.setAllocationStatusFilter([{ value: 'allocated' }]);
    });
    expect(result.current.resourceQuery.allocationStatus).toEqual(['allocated']);
  });

  it('should update skill experience filter', () => {
    const { result } = renderHook(() => useResourceQueryStore());
    act(() => {
      result.current.setSkillExpereinceFilter([{ skill: 'skill1', experience: [1, 2] }]);
    });
    expect(result.current.resourceQuery.skillAndExperiences).toEqual([
      { skillId: 'skill1', skillMinValue: '1', skillMaxValue: '2' },
    ]);
  });

  it('should update project filter', () => {
    const { result } = renderHook(() => useResourceQueryStore());
    act(() => {
      result.current.setProjectFilter([{ value: 'project1' }]);
    });
    expect(result.current.resourceQuery.projectIds).toEqual(['project1']);
  });

  it('should update page number', () => {
    const { result } = renderHook(() => useResourceQueryStore());
    act(() => {
      result.current.setPageNumber(2);
    });
    expect(result.current.resourceQuery.pageNumber).toBe(2);
  });

  it('should update page size', () => {
    const { result } = renderHook(() => useResourceQueryStore());
    act(() => {
      result.current.setPageSize(20);
    });
    expect(result.current.resourceQuery.pageSize).toBe(20);
  });

  it('should update status', () => {
    const { result } = renderHook(() => useResourceQueryStore());
    act(() => {
      result.current.setStatus('active');
    });
    expect(result.current.resourceQuery.status).toBe('active');
  });

  it('should update sort order', () => {
    const { result } = renderHook(() => useResourceQueryStore());
    act(() => {
      result.current.setSortOrder('asc');
    });
    expect(result.current.resourceQuery.sortOrder).toBe('asc');
  });

  it('should update sort key', () => {
    const { result } = renderHook(() => useResourceQueryStore());
    act(() => {
      result.current.setSortKey('name');
    });
    expect(result.current.resourceQuery.sortKey).toBe('name');
  });
});
