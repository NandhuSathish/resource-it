import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';
import qs from 'qs';

const useResourceAllocation = () => {
  const requestResource = useMutation({
    mutationFn: async (request) => {
      return axiosInstance
        .post(`/allocation/request/resource`, [...request])
        .then((res) => res.data);
    },
  });

  const getResourceAllocationRequests = useMutation({
    mutationFn: ({ queryData, isRequestList }) => {
      return axiosInstance
        .get(`/allocation/request/resource?isRequestList=${isRequestList}`, {
          params: queryData,
          paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat' }),
        })
        .then((res) => res.data);
    },
  });

  const approveResourceRequest = useMutation({
    mutationFn: (resourceId) => {
      return axiosInstance
        .put(`/allocation/request/resource/approve/${resourceId}`)
        .then((res) => res.data);
    },
  });

  const rejectResourceRequest = useMutation({
    mutationFn: ({ id, reason }) => {
      return axiosInstance
        .put(`/allocation/request/resource/reject/${id}`, { message: reason })
        .then((res) => res.data);
    },
  });

  const deleteResourceRequest = useMutation({
    mutationFn: (id) => {
      return axiosInstance.delete(`/allocation/request/resource/${id}`).then((res) => res.data);
    },
  });

  return {
    requestResource,
    getResourceAllocationRequests,
    approveResourceRequest,
    rejectResourceRequest,
    deleteResourceRequest,
  };
};

export default useResourceAllocation;
