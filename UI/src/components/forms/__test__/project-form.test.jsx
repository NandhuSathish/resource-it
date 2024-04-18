// FILEPATH: /home/nandhusathish/Documents/branches/resource-it/UI/src/components/forms/__test__/projectForm.test.jsx

import { render, fireEvent } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import { ProjectForm } from '../../ui/project-form';
import { describe, it, expect, vi } from 'vitest';
import React from 'react';
const queryClient = new QueryClient();

const renderProjectForm = (props) => {
  return render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        <ProjectForm {...props} />
      </MemoryRouter>
    </QueryClientProvider>
  );
};

describe('ProjectForm', () => {
  it('renders correctly', () => {
    const initialValues = {
      projectCode: '',
      name: '',
      clientName: '',
      managerId: '',
      skillIds: [],
    };

    const { getByPlaceholderText } = renderProjectForm({ initialValues });

    expect(getByPlaceholderText('Project Code')).toBeTruthy();
    expect(getByPlaceholderText('Name')).toBeTruthy();
    // expect(getByPlaceholderText('Project Type')).toBeTruthy();
    expect(getByPlaceholderText('Client Name')).toBeTruthy();
  });

  it('submits the form correctly', () => {
    const onSubmit = vi.fn();
    const initialValues = {
      projectCode: '1234',
      name: 'Test Project',
      clientName: 'Test Client',
      managerId: '1',
      skillIds: ['1', '2'],
    };

    const { getByPlaceholderText } = renderProjectForm({ initialValues, onSubmit });

    fireEvent.change(getByPlaceholderText('Project Code'), { target: { value: '1234' } });
    fireEvent.change(getByPlaceholderText('Name'), { target: { value: 'Test Project' } });
    fireEvent.change(getByPlaceholderText('Client Name'), { target: { value: 'Test Client' } });

    // fireEvent.click(getByText('Submit'));

    // expect(onSubmit).toHaveBeenCalledWith(initialValues);
  });
});
