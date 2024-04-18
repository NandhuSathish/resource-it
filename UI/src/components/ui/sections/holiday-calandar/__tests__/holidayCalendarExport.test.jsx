import { render, screen } from '@testing-library/react';
import HolidayCalendarExport from '../holidayCalendarExport';
// import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, vi, expect } from 'vitest';
// import HolidayCalendarExport from '../holidayCalendarExport';
import React from 'react';

vi.mock('src/hooks/use-holiday-calendar', () => ({
  __esModule: true,
  default: () => ({
    deleteImport: { mutate: vi.fn() },
    exportFile: { mutate: vi.fn() },
    getCalendarData: { mutate: vi.fn() },
  }),
}));

describe('HolidayCalendarExport', () => {
  it('renders "No imports found!" when holidayList is empty', () => {
    render(<HolidayCalendarExport holidayList={[]} />);
    expect(screen.getByText('No imports found!')).toBeTruthy();
  });
});
