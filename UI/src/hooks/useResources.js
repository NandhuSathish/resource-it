import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';
import useDownload from './use-download';

const useResources = () => {
  const getResources = useMutation({
    mutationFn: (data) => {
      return axiosInstance.post('/resource/list-resources', { ...data }).then((res) => res.data);
    },
    onSuccess: (data) => {
      console.log(data, 'data');
    },
    onError: () => {},
  });

  const getResourceById = useMutation({
    mutationFn: (data) => {
      return axiosInstance.get(`/resource/` + data).then((res) => res.data);
    },
    onSuccess: (data) => {
      console.log(data, 'data');
    },
    onError: () => {},
  });

  const editResource = useMutation({
    mutationFn: async (resource) => {
      return axiosInstance.put(`/resource/${resource.id}`, { ...resource }).then((res) => res.data);
    },
    onSuccess: (data) => {
      console.log(data, 'data');
    },
    onError: () => {},
  });

  const deleteResource = useMutation({
    mutationFn: (id) => {
      return axiosInstance.delete(`/resource/${id}`).then((res) => res.data);
    },
  });

  const getResourcesForAllocation = useMutation({
    mutationFn: (data) => {
      return axiosInstance
        .post('/allocation/request/resource/list', { ...data })
        .then((res) => res.data);
    },
  });
  const currentDate = new Date();
  const formattedDate = `${String(currentDate.getDate()).padStart(2, '0')}-${String(
    currentDate.getMonth() + 1
  ).padStart(2, '0')}-${currentDate.getFullYear()}`;
  const filename = `Resource_List_${formattedDate}.xlsx`;

  const exportResources = useDownload('/resource/download', 'post', filename);

  return {
    getResources,
    getResourcesForAllocation,
    exportResources,
    getResourceById,
    editResource,
    deleteResource,
  };
};

export default useResources;
