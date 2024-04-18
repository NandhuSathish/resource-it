/* eslint-disable react/react-in-jsx-scope */
// FILEPATH: /home/nandhusathish/Documents/hotFix/resource-it/UI/src/sections/holiday-calandar/__tests__/holidayCalendarView.test.jsx
import { render } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import SkillDataImport from '../view/skill-data';
import { MemoryRouter } from 'react-router-dom';

import { describe, it, vi } from 'vitest';

vi.mock('../skillDataImport', () => {
  const SkillDataImport = () => <div>Skill Data Import</div>;
  return { default: SkillDataImport };
});

const queryClient = new QueryClient();

describe('SkillDataImport', () => {
  it('renders without crashing', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <SkillDataImport />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });
});
