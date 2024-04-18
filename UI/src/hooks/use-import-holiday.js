import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from '../services/interceptor';
import { toast } from 'sonner';
import { errorCodeMap } from 'src/utils/error-codes';

const useHolidayUpload = () => {
  return useMutation({
    mutationFn: async (HolidayFile) => {
      const formData = new FormData();
      formData.append('file', HolidayFile);

      console.log(formData);

      return axiosInstance
        .post('/holiday/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then((res) => res.data);
    },
    onSuccess: (data) => {
      if (data) {
        toast.success('Import Successfull');
      }
    },
    onError: (error) => {
      if (error) {
         toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
      }
    },
  });
};

export default useHolidayUpload;