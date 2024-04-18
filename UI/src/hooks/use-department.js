import { useQuery } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';

const useDepartment = () => {
  return useQuery({
    queryKey: ['departments'],
    queryFn: () => axiosInstance.get('/department', {}).then((res) => res.data),
    staleTime: 1000 * 60 * 60 * 0.5, // 30 mins
  });
};

export default useDepartment;
