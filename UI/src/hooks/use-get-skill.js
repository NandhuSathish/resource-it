import { useQuery } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';

const useSkill = () => {
  return useQuery({
    queryKey: ['skills'],
    queryFn: () => axiosInstance.get('/skill', {}).then((res) => res.data),
    staleTime: 1000 * 60 * 60 * 0.5, // 30 mins
  });
};

export default useSkill;
