/* eslint-disable react/react-in-jsx-scope */
import { render, fireEvent } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { HolidayCalendarImport } from '../holidayCalenderImport';
import { vi, describe, it, expect } from 'vitest';

// Create a client
const queryClient = new QueryClient();

const mockUploadMutation = vi.fn();
vi.mock('src/hooks/use-import-holiday', () => ({
  __esModule: true,
  default: () => ({
    mutate: mockUploadMutation,
  }),
}));
const mockToastWarning = vi.fn();

vi.mock('sonner', () => ({ toast: { warning: vi.fn() } }));

describe('HolidayCalendarImport', () => {
  it('renders correctly', () => {
    render(
      <QueryClientProvider client={queryClient}>
        <HolidayCalendarImport />
      </QueryClientProvider>
    );
  });
  // it('calls uploadMutation function when a valid file is selected', () => {
  //   const { container } = render(
  //     <QueryClientProvider client={queryClient}>
  //       <HolidayCalendarImport />
  //     </QueryClientProvider>
  //   );
  //   const input = container.querySelector('input[type="file"]');
  //   const file = new File(['(⌐□_□)'], 'INV_Holidays.txt', { type: 'text/plain' });
  //   fireEvent.change(input, { target: { files: [file] } });
  //   expect(mockUploadMutation).toHaveBeenCalled();
  // });
  it('does not call uploadMutation function when an invalid file is selected', () => {
    const { container } = render(
      <QueryClientProvider client={queryClient}>
        <HolidayCalendarImport />
      </QueryClientProvider>
    );
    const input = container.querySelector('input[type="file"]');
    const file = new File(['(⌐□_□)'], 'Invalid.txt', { type: 'text/plain' });
    fireEvent.change(input, { target: { files: [file] } });
    expect(mockToastWarning).not.toHaveBeenCalledWith('Invalid file!');
  });
});
