import { useQuery } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';

const useManager = () => {
  return useQuery({
    queryKey: ['managers'],
    queryFn: () => axiosInstance.get('/resource/managers', {}).then((res) => res.data),
    staleTime: 1000 * 30,
  });
};

export default useManager;
