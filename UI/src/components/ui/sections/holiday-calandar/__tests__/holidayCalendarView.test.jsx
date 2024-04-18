/* eslint-disable react/react-in-jsx-scope */
// FILEPATH: /home/nandhusathish/Documents/hotFix/resource-it/UI/src/sections/holiday-calandar/__tests__/holidayCalendarView.test.jsx
import { render } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import HolidayCalendarView from '../view/holidayCalendarView';
import { describe, it, vi } from 'vitest';

vi.mock('../holidayCalenderImport', () => {
  const HolidayCalendarImport = () => <div>HolidayCalendarImport</div>;
  return { default: HolidayCalendarImport };
});

vi.mock('../holidayCalendarExport', () => {
  const HolidayCalendarExport = () => <div>HolidayCalendarExport</div>;
  return { default: HolidayCalendarExport };
});

const queryClient = new QueryClient();

describe('HolidayCalendarView', () => {
  it('renders without crashing', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <HolidayCalendarView />
      </QueryClientProvider>
    );
  });
});
