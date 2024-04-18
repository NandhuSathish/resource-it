import { render, } from '@testing-library/react';
import { describe, it, expect} from 'vitest';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { MemoryRouter } from 'react-router-dom';
import { AddResourceForm } from '../addResource';
import React from 'react';
const queryClient = new QueryClient();

const renderAddResourceForm = (props) => {

    const skillData = [
      // Add your skill data here
    ];

  return render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        <AddResourceForm {...props} skillData={skillData} />
      </MemoryRouter>
    </QueryClientProvider>
  );
};


describe('AddResourceForm', () => {
    it('renders correctly', () => {
        const initialValues = {
          employeeId: '',
          name: '',
        };
    
        const { getByPlaceholderText } = renderAddResourceForm({ initialValues });
    
        expect(getByPlaceholderText('Employee Id')).toBeTruthy();
        expect(getByPlaceholderText('Name')).toBeTruthy();
        
      });


});