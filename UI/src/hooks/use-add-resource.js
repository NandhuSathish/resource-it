import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from '../services/interceptor';

const useAddResources = () => {
  return useMutation({
    mutationFn: (data) => {
      return axiosInstance.post('/resource/add', { ...data }).then((res) => res.data);
    },
  });
};

export default useAddResources;
