import { varSlide } from '../slide';
import { describe, it, expect } from 'vitest';

describe('varSlide', () => {
  it('returns the correct object with default props', () => {
    const result = varSlide({});
    expect(result).toHaveProperty('inUp');
    expect(result).toHaveProperty('inDown');
    expect(result).toHaveProperty('inLeft');
    expect(result).toHaveProperty('inRight');
    expect(result).toHaveProperty('outUp');
    expect(result).toHaveProperty('outDown');
    expect(result).toHaveProperty('outLeft');
    expect(result).toHaveProperty('outRight');
  });
});
