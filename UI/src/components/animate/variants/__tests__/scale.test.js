import { varScale } from '../scale';
import { describe, it, expect } from 'vitest';

describe('varScale', () => {
  it('returns the correct object with default props', () => {
    const result = varScale({});
    expect(result).toHaveProperty('inX');
    expect(result).toHaveProperty('inY');
    expect(result).toHaveProperty('outX');
    expect(result).toHaveProperty('outY');
  });
});
