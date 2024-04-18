import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';

const useDownload = (url, method, fileName) => {
  return useMutation({
    mutationFn: (data) => {
      const finalUrl = typeof url === 'function' ? url(data) : url;
      return axiosInstance[method](finalUrl, { ...data }, { responseType: 'blob' }).then((res) => {
        const file = new Blob([res.data], { type: 'application/octet-stream' });
        const downloadLink = document.createElement('a');
        downloadLink.href = window.URL.createObjectURL(file);
        const finalFileName = typeof fileName === 'function' ? fileName(data) : fileName;
        downloadLink.setAttribute('download', finalFileName);
        document.body.appendChild(downloadLink);
        downloadLink.click();
      });
    },
  });
};

export default useDownload;
