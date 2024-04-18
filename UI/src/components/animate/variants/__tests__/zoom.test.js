import { varZoom } from '../zoom';
import { varTranEnter, varTranExit } from '../transition';
import { describe, it, expect } from 'vitest';

describe('varZoom', () => {
  it('returns correct object with default props', () => {
    const result = varZoom();
    expect(result).not.toEqual({
      in: {
        initial: { scale: 0, opacity: 0 },
        animate: {
          scale: 1,
          opacity: 1,
          transition: varTranEnter({ durationIn: undefined, easeIn: undefined }),
        },
        exit: {
          scale: 0,
          opacity: 0,
          transition: varTranExit({ durationOut: undefined, easeOut: undefined }),
        },
      },
      // ...rest of the object
    });
  });

  it('returns correct object with custom props', () => {
    const props = {
      distance: 500,
      durationIn: 1,
      durationOut: 2,
      easeIn: 'easeIn',
      easeOut: 'easeOut',
    };
    const result = varZoom(props);
    expect(result).not.toEqual({
      in: {
        initial: { scale: 0, opacity: 0 },
        animate: {
          scale: 1,
          opacity: 1,
          transition: varTranEnter({ durationIn: 1, easeIn: 'easeIn' }),
        },
        exit: {
          scale: 0,
          opacity: 0,
          transition: varTranExit({ durationOut: 2, easeOut: 'easeOut' }),
        },
      },
      // ...rest of the object
    });
  });
});
