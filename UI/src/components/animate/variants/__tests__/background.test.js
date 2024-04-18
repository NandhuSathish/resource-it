import { varBgPan, varBgColor, varBgKenburns } from '../background';
import { describe, it, expect } from 'vitest';

describe('varBgPan', () => {
  it('returns the correct object with default props', () => {
    const result = varBgPan({});
    expect(result).toEqual({
      top: {
        animate: {
          backgroundImage: [
            'linear-gradient(0deg, #ee7752,#e73c7e,#23a6d5,#23d5ab)',
            'linear-gradient(0deg, #ee7752,#e73c7e,#23a6d5,#23d5ab)',
          ],
          backgroundPosition: ['center 99%', 'center 1%'],
          backgroundSize: ['100% 600%', '100% 600%'],
          transition: { duration: 5, ease: 'linear' },
        },
      },
      right: {
        animate: {
          backgroundPosition: ['1% center', '99% center'],
          backgroundImage: [
            'linear-gradient(270deg, #ee7752,#e73c7e,#23a6d5,#23d5ab)',
            'linear-gradient(270deg, #ee7752,#e73c7e,#23a6d5,#23d5ab)',
          ],
          backgroundSize: ['600% 100%', '600% 100%'],
          transition: { duration: 5, ease: 'linear' },
        },
      },
      bottom: {
        animate: {
          backgroundImage: [
            'linear-gradient(0deg, #ee7752,#e73c7e,#23a6d5,#23d5ab)',
            'linear-gradient(0deg, #ee7752,#e73c7e,#23a6d5,#23d5ab)',
          ],
          backgroundPosition: ['center 1%', 'center 99%'],
          backgroundSize: ['100% 600%', '100% 600%'],
          transition: { duration: 5, ease: 'linear' },
        },
      },
      left: {
        animate: {
          backgroundPosition: ['99% center', '1% center'],
          backgroundImage: [
            'linear-gradient(270deg, #ee7752,#e73c7e,#23a6d5,#23d5ab)',
            'linear-gradient(270deg, #ee7752,#e73c7e,#23a6d5,#23d5ab)',
          ],
          backgroundSize: ['600% 100%', '600% 100%'],
          transition: { duration: 5, ease: 'linear' },
        },
      },
    });
  });

  it('returns the correct object with custom props', () => {
    const props = {
      colors: ['#123456', '#789abc', '#def012', '#345678'],
      duration: 10,
      ease: 'ease-in-out',
    };
    const result = varBgPan(props);
    expect(result).toEqual({
      top: {
        animate: {
          backgroundImage: [
            'linear-gradient(0deg, #123456,#789abc,#def012,#345678)',
            'linear-gradient(0deg, #123456,#789abc,#def012,#345678)',
          ],
          backgroundPosition: ['center 99%', 'center 1%'],
          backgroundSize: ['100% 600%', '100% 600%'],
          transition: { duration: 10, ease: 'ease-in-out' },
        },
      },
      right: {
        animate: {
          backgroundPosition: ['1% center', '99% center'],
          backgroundImage: [
            'linear-gradient(270deg, #123456,#789abc,#def012,#345678)',
            'linear-gradient(270deg, #123456,#789abc,#def012,#345678)',
          ],
          backgroundSize: ['600% 100%', '600% 100%'],
          transition: { duration: 10, ease: 'ease-in-out' },
        },
      },
      bottom: {
        animate: {
          backgroundImage: [
            'linear-gradient(0deg, #123456,#789abc,#def012,#345678)',
            'linear-gradient(0deg, #123456,#789abc,#def012,#345678)',
          ],
          backgroundPosition: ['center 1%', 'center 99%'],
          backgroundSize: ['100% 600%', '100% 600%'],
          transition: { duration: 10, ease: 'ease-in-out' },
        },
      },
      left: {
        animate: {
          backgroundPosition: ['99% center', '1% center'],
          backgroundImage: [
            'linear-gradient(270deg, #123456,#789abc,#def012,#345678)',
            'linear-gradient(270deg, #123456,#789abc,#def012,#345678)',
          ],
          backgroundSize: ['600% 100%', '600% 100%'],
          transition: { duration: 10, ease: 'ease-in-out' },
        },
      },
    });
  });
});
describe('varBgColor', () => {
  it('returns the correct object with default props', () => {
    const result = varBgColor({});
    expect(result).toEqual({
      animate: {
        background: ['#19dcea', '#b22cff'],
        transition: { duration: 5, ease: 'linear' },
      },
    });
  });

  it('returns the correct object with custom props', () => {
    const props = {
      colors: ['#123456', '#789abc'],
      duration: 10,
      ease: 'ease-in-out',
    };
    const result = varBgColor(props);
    expect(result).toEqual({
      animate: {
        background: ['#123456', '#789abc'],
        transition: { duration: 10, ease: 'ease-in-out' },
      },
    });
  });
});

describe('varBgKenburns', () => {
  it('returns the correct object with default props', () => {
    const result = varBgKenburns({});
    expect(result).toEqual({
      top: {
        animate: {
          scale: [1, 1.25],
          y: [0, -15],
          transformOrigin: ['50% 16%', 'top'],
          transition: { duration: 5, ease: 'easeOut' },
        },
      },
      right: {
        animate: {
          scale: [1, 1.25],
          x: [0, 20],
          y: [0, -15],
          transformOrigin: ['84% 50%', 'right'],
          transition: { duration: 5, ease: 'easeOut' },
        },
      },
      bottom: {
        animate: {
          scale: [1, 1.25],
          y: [0, 15],
          transformOrigin: ['50% 84%', 'bottom'],
          transition: { duration: 5, ease: 'easeOut' },
        },
      },
      left: {
        animate: {
          scale: [1, 1.25],
          x: [0, -20],
          y: [0, 15],
          transformOrigin: ['16% 50%', 'left'],
          transition: { duration: 5, ease: 'easeOut' },
        },
      },
    });
  });

  it('returns the correct object with custom props', () => {
    const props = {
      duration: 10,
      ease: 'ease-in-out',
    };
    const result = varBgKenburns(props);
    expect(result).toEqual({
      top: {
        animate: {
          scale: [1, 1.25],
          y: [0, -15],
          transformOrigin: ['50% 16%', 'top'],
          transition: { duration: 10, ease: 'ease-in-out' },
        },
      },
      right: {
        animate: {
          scale: [1, 1.25],
          x: [0, 20],
          y: [0, -15],
          transformOrigin: ['84% 50%', 'right'],
          transition: { duration: 10, ease: 'ease-in-out' },
        },
      },
      bottom: {
        animate: {
          scale: [1, 1.25],
          y: [0, 15],
          transformOrigin: ['50% 84%', 'bottom'],
          transition: { duration: 10, ease: 'ease-in-out' },
        },
      },
      left: {
        animate: {
          scale: [1, 1.25],
          x: [0, -20],
          y: [0, 15],
          transformOrigin: ['16% 50%', 'left'],
          transition: { duration: 10, ease: 'ease-in-out' },
        },
      },
    });
  });
});
