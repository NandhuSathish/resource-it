/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';
import { useInView } from 'react-intersection-observer';
import { useAnimation } from 'framer-motion';
import MotionInView from '../MotionInView';
import { vi, describe, it, expect } from 'vitest';

vi.mock('react-intersection-observer');
vi.mock('framer-motion');

describe('MotionInView', () => {
  it('starts initial animation if variants are provided', () => {
    const controls = { start: vi.fn() };
    useAnimation.mockReturnValue(controls);
    useInView.mockReturnValue([{}, false]);

    const variants = { hidden: {}, visible: {} };
    render(<MotionInView variants={variants}>Test</MotionInView>);

    expect(controls.start).toHaveBeenCalledWith('hidden');
  });

  it('starts visible animation if in view', () => {
    const controls = { start: vi.fn() };
    useAnimation.mockReturnValue(controls);
    useInView.mockReturnValue([{}, true]);

    const variants = { hidden: {}, visible: {} };
    render(<MotionInView variants={variants}>Test</MotionInView>);

    expect(controls.start).toHaveBeenCalledWith('visible');
  });

  it('starts hidden animation if not in view', () => {
    const controls = { start: vi.fn() };
    useAnimation.mockReturnValue(controls);
    useInView.mockReturnValue([{}, false]);

    const variants = { hidden: {}, visible: {} };
    render(<MotionInView variants={variants}>Test</MotionInView>);

    expect(controls.start).toHaveBeenCalledWith('hidden');
  });
});
