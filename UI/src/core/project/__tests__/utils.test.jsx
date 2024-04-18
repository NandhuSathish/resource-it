import { emptyRows, canDelete, getStatusColor } from '../utils.js';
import { it, describe, expect } from 'vitest';


describe('emptyRows', () => {
  it('returns correct empty rows', () => {
    expect(emptyRows(1, 10, 15)).toBe(5);
    expect(emptyRows(0, 10, 15)).toBe(0);
  });
});

describe('canDelete', () => {
  it('returns correct deletion permissions', () => {
    expect(canDelete('HOD', 'HR')).toBe(true);
    expect(canDelete('HR', 'HOD')).toBe(true);
    expect(canDelete('Employee', 'HOD')).toBe(false);
    expect(canDelete('Employee', 'HR')).toBe(false);
    expect(canDelete('Employee', 'Employee')).toBe(true);
  });
});

describe('getStatusColor', () => {
  it('returns correct status color', () => {
    expect(getStatusColor(0)).toBe('warning');
    expect(getStatusColor(1)).toBe('primary');
    expect(getStatusColor(2)).toBe('success');
  });
});