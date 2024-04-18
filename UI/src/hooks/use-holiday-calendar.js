/* eslint-disable no-useless-catch */
import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from '../services/interceptor';
import { toast } from 'sonner';
import calendarStore from 'src/components/ui/stores/calendarStore';
import useDownload from './use-download';

const useHolidayCalendar = () => {
  const addToCalendars = calendarStore((state) => state.addToCalendars);
  const deleteImport = useMutation({
    mutationFn: (data) => {
      axiosInstance.delete(`/holiday/${data}`).then((res) => res.data);
    },
    onSuccess: () => {
      toast.success('Delete Successfull');
    },
    onError: () => {
      toast.success('Failed to Delete');
    },
  });

  const exportCalendar = useDownload(
    (data) => `/holiday/download/${data}`,
    'get',
    (data) => `INV_Holidays${data}.csv`
  );

  const getCalendarData = useMutation({
    mutationFn: async () => {
      const response = await axiosInstance.get('/holiday');
      return response.data;
    },
    onSuccess: (data) => {
      addToCalendars(data);
    },
  });

  return { deleteImport, getCalendarData, exportCalendar };
};

export default useHolidayCalendar;
