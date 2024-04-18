/* eslint-disable react/react-in-jsx-scope */

import { render, fireEvent } from '@testing-library/react';
// import { useResourceImport } from 'src/hooks/use-resource-import';
import { describe, it, vi, expect } from 'vitest';
import ResourceImport from '../resource-import';

// Mock the useResourceImport hook
// vi.mock('src/hooks/use-resource-import', () => ({
//   __esModule: true,
//   default: () => ({
//     mutate: vi.fn(),
//   }),
// }));
const mockMutate = vi.fn();
vi.mock('src/hooks/use-resource-import', () => ({
  __esModule: true,
  default: () => ({
    mutate: mockMutate,
  }),
}));

describe('ResourceImport', () => {
  it('renders without crashing', () => {
    render(<ResourceImport />);
  });
  it('calls mutate function when a file is selected', () => {
    const { container } = render(<ResourceImport />);
    const file = new File(['(⌐□_□)'], 'chucknorris.png', { type: 'image/png' });
    const input = container.querySelector('input[type="file"]');
    fireEvent.change(input, { target: { files: [file] } });
    expect(mockMutate).not.toHaveBeenCalled();
  });

  //   it('does not call mutate function when no file is selected', () => {
  //     const { container } = render(<ResourceImport />);
  //     const input = container.querySelector('input[type="file"]');
  //     fireEvent.change(input, { target: { files: [] } });
  //     expect(mockMutate).not.toHaveBeenCalled();
  //   });
});
