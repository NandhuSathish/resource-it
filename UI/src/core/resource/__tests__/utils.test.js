import { it, describe, expect } from 'vitest';
import { emptyRows, canDelete, canEdit } from '../utils';
// Import the functions and properties to be tested

// Define a test suite named 'Resource Utils'.
describe('emptyRows', () => {
  it('returns 0 if page is not defined', () => {
    const result = emptyRows(undefined, 10, 20);
    expect(result).toBe(0);
  });

  it('returns the correct number of empty rows if page is defined', () => {
    const result = emptyRows(1, 10, 15);
    expect(result).toBe(5);
  });
});

describe('canDelete', () => {
  it('returns false if currentUserRole.role is 4', () => {
    const result = canDelete({ role: 4 }, 'Employee', 'ACTIVE');
    expect(result).toBe(false);
  });

  it('returns false if currentUserRole.role is 5', () => {
    const result = canDelete({ role: 5 }, 'Employee', 'ACTIVE');
    expect(result).toBe(false);
  });

  it('returns false if currentUserRole.role is 7', () => {
    const result = canDelete({ role: 7 }, 'Employee', 'ACTIVE');
    expect(result).toBe(false);
  });

  it('returns true if none of the conditions are met', () => {
    const result = canDelete({ role: 8 }, 'Employee', 'ACTIVE');
    expect(result).toBe(true);
  });
});

describe('canEdit', () => {
  it('returns false if state is not ACTIVE', () => {
    const result = canEdit({ role: 1 }, 'INACTIVE');
    expect(result).toBe(false);
  });

  it('returns true if currentUserRole.role is 1', () => {
    const result = canEdit({ role: 1 }, 'ACTIVE');
    expect(result).toBe(true);
  });

  it('returns true if currentUserRole.role is 2', () => {
    const result = canEdit({ role: 2 }, 'ACTIVE');
    expect(result).toBe(true);
  });

  it('returns true if currentUserRole.role is 3', () => {
    const result = canEdit({ role: 3 }, 'ACTIVE');
    expect(result).toBe(true);
  });

  it('returns true if currentUserRole.role is 6', () => {
    const result = canEdit({ role: 6 }, 'ACTIVE');
    expect(result).toBe(true);
  });

  it('returns false if currentUserRole.role is not 1, 2, 3, or 6', () => {
    const result = canEdit({ role: 4 }, 'ACTIVE');
    expect(result).toBe(false);
  });
});
