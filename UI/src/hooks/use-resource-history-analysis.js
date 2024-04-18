import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';
import qs from 'qs';

const useResourceHistoryAnalysis = () => {

  const getResorceHistoryById = useMutation({
    mutationFn: (content) => {
      return axiosInstance
        .get(`statistics/resource-analysis/allocations/${content.resourceId}`, {
          params: content.query,
          paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat' }),
        })
        .then((res) => res.data);
    },
  });

  return { getResorceHistoryById  };
};

export default useResourceHistoryAnalysis;
