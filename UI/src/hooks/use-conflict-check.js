import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';
import qs from 'qs';

const useConflictCheck = () => {
  const bulkConflictCheck = useMutation({
    mutationFn: (data) => {
      return axiosInstance
        .get(`/allocation/conflicts/list`, {
          params: data,
          paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat' }),
        })
        .then((res) => res.data);
    },
  });

  return {
    bulkConflictCheck,
  };
};

export default useConflictCheck;
