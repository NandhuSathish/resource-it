/* eslint-disable react/react-in-jsx-scope */
import { render, screen } from '@testing-library/react';
import TextAnimate from '../TextAnimate';
import { describe, it, expect } from 'vitest';

describe('TextAnimate', () => {
  it('renders without crashing', () => {
    render(<TextAnimate text="Test" />);
  });

  it('splits the text into individual letters', () => {
    render(<TextAnimate text="Test" />);
    expect(screen.getAllByText(/T|e|s|t/)).toHaveLength(4);
  });

  it('applies the correct variants', () => {
    const variants = { hidden: {}, visible: {} };
    render(<TextAnimate text="Test" variants={variants} />);
    // Here you would check if the correct animation is applied based on the variants.
    // This is dependent on your animation library and might require additional setup or mocking.
  });
});
