import { useQuery } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';
const useHolidayGet = () => {
  return useQuery({
    queryFn: () => axiosInstance.get('/holiday', {}).then((res) => res.data),
  });
};

export default useHolidayGet;
