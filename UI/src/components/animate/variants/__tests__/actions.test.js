// FILEPATH: /home/nandhusathish/Videos/resource-it/UI/src/components/animate/variants/__tests__/actions.test.js

import { varHover } from '../actions';
import { describe, it, expect } from 'vitest';

describe('varHover', () => {
  it('should return hover object with default scale', () => {
    const result = varHover();
    expect(result).toEqual({
      hover: {
        scale: 1.1,
      },
    });
  });

  it('should return hover object with provided scale', () => {
    const result = varHover(1.5);
    expect(result).toEqual({
      hover: {
        scale: 1.5,
      },
    });
  });
});
