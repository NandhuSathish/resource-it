import { renderHook } from '@testing-library/react-hooks';
import { axiosInstance } from 'src/services/interceptor';
import useHolidayCalendar from '../use-holiday-calendar';
import { vi, expect, describe, it } from 'vitest';

vi.mock('@tanstack/react-query', () => ({
  useMutation: (config) => ({
    mutate: (data) => config.mutationFn(data),
  }),
}));

vi.mock('src/services/interceptor', () => ({
  axiosInstance: {
    delete: vi.fn().mockResolvedValue({ data: {} }),
    get: vi.fn().mockResolvedValue({ data: {} }),
  },
}));

vi.mock('sonner', () => ({
  toast: {
    success: vi.fn(),
  },
}));

vi.mock('src/stores/calendarStore', () => ({
  default: vi.fn(),
}));

describe('useHolidayCalendar', () => {
  it('returns the correct data', () => {
    axiosInstance.delete.mockResolvedValue({ data: {} });
    axiosInstance.get.mockResolvedValue({ data: {} });

    const { result } = renderHook(() => useHolidayCalendar());

    expect(result.current).toHaveProperty('deleteImport');
    expect(result.current).toHaveProperty('exportCalendar');
    expect(result.current).toHaveProperty('getCalendarData');
  });

  it('handles deleteImport mutation', async () => {
    const deleteData = 'test';
    axiosInstance.delete.mockResolvedValue({ data: {} });

    const { result } = renderHook(() => useHolidayCalendar());
    await result.current.deleteImport.mutate(deleteData);
  });
});
