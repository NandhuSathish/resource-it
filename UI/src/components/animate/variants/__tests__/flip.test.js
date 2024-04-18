import { varFlip } from '../flip';
import { describe, it, expect } from 'vitest';

describe('varFlip', () => {
  it('returns the correct object with default props', () => {
    const result = varFlip({});
    expect(result).toHaveProperty('inX');
    expect(result).toHaveProperty('inY');
    expect(result).toHaveProperty('outX');
    expect(result).toHaveProperty('outY');
  });
});
