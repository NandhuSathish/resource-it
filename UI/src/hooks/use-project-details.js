import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';

const useProjectRequests = () => {
  const getRequestById = useMutation({
    mutationFn: (data) => {
      return axiosInstance.get(`/allocation/project/${data}`).then((res) => res.data);
    },
    onSuccess: (data) => {
      console.log(data, 'data');
    },
    onError: () => {},
  });

  return { getRequestById };
};

export default useProjectRequests;
