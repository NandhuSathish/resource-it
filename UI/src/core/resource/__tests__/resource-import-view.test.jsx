/* eslint-disable react/react-in-jsx-scope */
import { render, screen } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';
import { ResourceImportView } from '../view/resource-import-view';
import '@testing-library/jest-dom';

// Mock the ResourceImport component
vi.mock('../resource-import', () => {
  return {
    default: function DummyResourceImport() {
      return <div data-testid="resource-import"></div>;
    },
  };
});

describe('ResourceImportView', () => {
  it('renders without crashing', () => {
    render(<ResourceImportView />);
    expect(
      screen.getByText(/Upload a valid Excel file name starting with "Employees"/i)
    ).toBeInTheDocument();
  });
});
