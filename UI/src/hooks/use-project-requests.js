import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';
import useDownload from './use-download';
import qs from 'qs';

const useProjectRequests = () => {
  const getProjectRequests = useMutation({
    mutationFn: ({ queryData, isRequestList }) => {
      return axiosInstance
        .get(`/project/project-request?isRequestList=${isRequestList}`, {
          params: queryData,
          paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat' }),
        })
        .then((res) => res.data);
    },
  });
  const getRequestById = useMutation({
    mutationFn: (data) => {
      return axiosInstance.get(`/project/project-request/${data}`).then((res) => res.data);
    },
    onSuccess: (data) => {
      console.log(data, 'data');
    },
    onError: () => {},
  });

  const deleteProjectRequest = useMutation({
    mutationFn: (id) => {
      return axiosInstance.delete(`/project/project-request/${id}`).then((res) => res.data);
    },
  });

  const approveEditedProject = useMutation({
    mutationFn: async (request) => {
      return axiosInstance
        .post(`/project/approve/${request.projectRequestId}?approvalStatus=${request.status}`)
        .then((res) => res.data);
    },
    onSuccess: (data) => {
      console.log(data, 'data');
    },
    onError: () => {},
  });

  const projectRequestReject = useMutation({
    mutationFn: async (request) => {
      return axiosInstance
        .put(`/project/reject/${request.projectRequestId}/${request.status}`)
        .then((res) => res.data);
    },
    onSuccess: (data) => {
      console.log(data, 'data');
    },
    onError: () => {},
  });

  const exportProjects = useDownload('/project/download', 'post', 'resources.xlsx');

  return {
    getProjectRequests,
    exportProjects,
    getRequestById,
    approveEditedProject,
    projectRequestReject,
    deleteProjectRequest,
  };
};

export default useProjectRequests;
