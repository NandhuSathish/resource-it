import { useMutation, useQuery } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';

const useDashboard = () => {
  const getDashboardData = useQuery({
    queryKey: ['dashboardCount'],
    queryFn: () => axiosInstance.get(`dashboard/count`).then((res) => res.data),
  });

  const getDashboardPieChartData = useMutation({
    mutationFn: async (chart) => {
      return axiosInstance
        .get(
          `/dashboard/chart?allocationStatus=${chart.benched}&allocationStatus=${chart.internal}&allocationStatus=${chart.billable}&flag=${chart.chartMode}&skillIds=${chart.skillIds}`
        )
        .then((res) => res.data);
    },
  });

  return {
    getDashboardData,
    getDashboardPieChartData,
  };
};

export default useDashboard;
