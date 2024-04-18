import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from '../services/interceptor';
import { toast } from 'sonner';
import { useNavigate } from 'react-router-dom';

const useUpdateResources = () => {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data) => {
      return axiosInstance.post('/resource/', { ...data }).then((res) => res.data);
    },
    onSuccess: (data) => {
      console.log(data, 'data');
      if (data) {
        navigate('/');
        toast.success('Resource updated successfully');
      } else if (data.errorCode == '1054') {
        toast.warning('provided email already exist');
      }
    },
    onError: (error) => {
      if (error) {
        toast.error('Failed to add resource');
      }
    },
  });
};

export default useUpdateResources;
