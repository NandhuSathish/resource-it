import { useMutation, useQuery } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';
import useDownload from './use-download';
import qs from 'qs';

const useProjects = () => {
  const getProjects = useMutation({
    mutationFn: (data) => {
      return axiosInstance.post('/project/listing', { ...data }).then((res) => res.data);
    },
  });

  const getProjectAllocations = useMutation({
    mutationFn: (content) => {
      return axiosInstance
        .get(`/allocation/project/${content.id}`, {
          params: content.query,
          paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat' }),
        })
        .then((res) => res.data);
    },
  });

  const getProjectById = useMutation({
    mutationFn: (data) => {
      return axiosInstance.get(`/project/${data}`).then((res) => res.data);
    },
  });

  const approveProject = useMutation({
    mutationFn: async (request) => {
      console.log(request);
      return axiosInstance
        .post(`/project/approval/${request.projectRequestId}`, { ...request })
        .then((res) => res.data);
    },
  });

  const editProject = useMutation({
    mutationFn: async (project) => {
      console.log(project);
      return axiosInstance.put(`/project`, { ...project }).then((res) => res.data);
    },
  });

  const deleteProject = useMutation({
    mutationFn: (id) => {
      return axiosInstance.delete(`/project/${id}`).then((res) => res.data);
    },
  });

  const currentDate = new Date();
  const formattedDate = `${String(currentDate.getDate()).padStart(2, '0')}-${String(
    currentDate.getMonth() + 1
  ).padStart(2, '0')}-${currentDate.getFullYear()}`;
  const filename = `Project_List_${formattedDate}.xlsx`;

  const exportProjects = useDownload('/project/download', 'post', filename);

  const getProjectNameAndId = (data) => {
    return useQuery({
      queryKey: ['projectNameAndId', data],
      queryFn: () =>
        axiosInstance.get(`/project/manager?isCurrentUser=${data}`, {}).then((res) => res.data),
      staleTime: 0,
    });
  };

  const addProject = useMutation({
    mutationFn: async (project) => {
      return axiosInstance.post(`/project`, { ...project }).then((res) => res.data);
    },
  });

  const deleteAllocation = useMutation({
    mutationFn: (data) => {
      return axiosInstance.delete(`/allocation/${data}`).then((res) => res.data);
    },
  });

  const projectAllocationEdit = useMutation({
    mutationFn: async (project) => {
      return axiosInstance
        .put(`/allocation/request/resource`, { ...project })
        .then((res) => res.data);
    },
  });

  return {
    getProjects,
    exportProjects,
    getProjectById,
    editProject,
    deleteProject,
    approveProject,
    projectAllocationEdit,
    addProject,
    getProjectAllocations,
    deleteAllocation,
    getProjectNameAndId,
  };
};

export default useProjects;
