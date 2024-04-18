import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';

const useNotification = () => {
  const getPendingRequestCount = useMutation({
    mutationFn: (chart) => {
      return axiosInstance
        .get(
          `/dashboard/chart?allocationStatus=${chart.benched}&allocationStatus=${chart.internal}&allocationStatus=${chart.billable}&flag=${chart.chartMode}`
        )
        .then((res) => res.data);
    },
  });

  return {
    getPendingRequestCount,
  };
};

export default useNotification;
