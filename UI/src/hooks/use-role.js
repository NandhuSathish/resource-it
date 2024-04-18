import { useQuery } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';

const useRole = () => {
  return useQuery({
    queryKey: ['roles'],
    queryFn: () => axiosInstance.get('/role', {}).then((res) => res.data),
    staleTime: 1000 * 60 * 60 * 0.5, // 30 mins
  });
};

export default useRole;
