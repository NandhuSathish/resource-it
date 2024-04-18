import { varBounce } from '../bounce';
import { describe, it, expect } from 'vitest';

describe('varBounce', () => {
  it('returns the correct object with default props', () => {
    const result = varBounce({});
    // Here you would check the structure of the returned object.
    // This is dependent on your animation library and might require additional setup or mocking.
    // For example:
    expect(result).toHaveProperty('in');
    expect(result).toHaveProperty('inUp');
    expect(result).toHaveProperty('inDown');
    expect(result).toHaveProperty('inLeft');
    expect(result).toHaveProperty('inRight');
    expect(result).toHaveProperty('out');
    expect(result).toHaveProperty('outUp');
    expect(result).toHaveProperty('outDown');
    expect(result).toHaveProperty('outLeft');
    expect(result).toHaveProperty('outRight');
  });
});
