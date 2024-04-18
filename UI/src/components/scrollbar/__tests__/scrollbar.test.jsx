/* eslint-disable react/react-in-jsx-scope */
import { describe, it } from 'vitest';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom';
import Scrollbar from '../scrollbar';
import { MemoryRouter } from 'react-router-dom';

// Define a test suite named 'Header'.
describe('Scrollbar', () => {
  // test to confirm the scrollbar is rendering out.
  it('Renders Scrollbar ', () => {
    render(
      <MemoryRouter>
        <Scrollbar />
      </MemoryRouter>
    );
  });
});
