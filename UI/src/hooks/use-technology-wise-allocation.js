import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';

const useTechnologyWiseAllocation = () => {
  const requestTechnologyWise = useMutation({
    mutationFn: (request) => {
      return axiosInstance
        .post(`/allocation/request/skill-wise`, { ...request })
        .then((res) => res.data);
    },
  });

  const getTechnologyWiseAllocationRequests = useMutation({
    mutationFn: ({ queryData, isRequestList }) => {
      return axiosInstance
        .post(`/allocation/request/skill-wise/list?isRequestList=${isRequestList}`, {
          ...queryData,
        })
        .then((res) => res.data);
    },
    onSuccess: (data) => {
      console.log(data, 'data');
    },
    onError: () => {},
  });

  const getTechnologyWiseAllocationRequestById = useMutation({
    mutationFn: (id) => {
      return axiosInstance.get(`/allocation/request/skill-wise/${id}`).then((res) => res.data);
    },
  });

  const approveTechnologyRequestByHod = useMutation({
    mutationFn: (requestId) => {
      return axiosInstance
        .put(`/allocation/request/skill-wise/approve/${requestId}`)
        .then((res) => res.data);
    },
  });

  const approveTechnologyRequestByHR = useMutation({
    mutationFn: ({ id, data }) => {
      return axiosInstance
        .post(`/allocation/request/skill-wise/approve/${id}`, [...data])
        .then((res) => res.data);
    },
  });

  const rejectTechnologyRequest = useMutation({
    mutationFn: ({ id, reason }) => {
      return axiosInstance
        .put(`/allocation/request/skill-wise/reject/${id}`, { message: reason })
        .then((res) => res.data);
    },
  });

  const deleteTechnologyRequest = useMutation({
    mutationFn: (id) => {
      return axiosInstance.delete(`/allocation/request/skill-wise/${id}`).then((res) => res.data);
    },
  });

  return {
    requestTechnologyWise,
    getTechnologyWiseAllocationRequests,
    getTechnologyWiseAllocationRequestById,
    approveTechnologyRequestByHod,
    approveTechnologyRequestByHR,
    rejectTechnologyRequest,
    deleteTechnologyRequest,
  };
};

export default useTechnologyWiseAllocation;
