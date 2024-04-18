import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';
import qs from 'qs';
import useDownload from './use-download';
import useResourceHistoryQueryStore from 'src/components/ui/stores/resourceHistoryStore';
import dayjs from 'dayjs';

const useResourceDetailAnalysis = () => {
  const projectQuery = useResourceHistoryQueryStore((s) => s.projectQuery);
  const getResorceDetailById = useMutation({
    mutationFn: (data) => {
      return axiosInstance
        .get(`statistics/resource-analysis/${data.resourceId}`, {
          params: data.query,
          paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat' }),
        })
        .then((res) => res.data);
    },
  });

  const formattedStartDate = dayjs(projectQuery.startDate).format('MMM DD YYYY');
  const formattedEndDate = dayjs(projectQuery.endDate).format('MMM DD YYYY');

  const filename =
    localStorage.getItem('resourceAnalysisFilename') +
    '_' +
    formattedStartDate +
    '_to_' +
    formattedEndDate +
    '.xlsx';
  const exportResourceDetail = useDownload(
    () => `/statistics/allocation/details/download`,
    'post',
    filename
  );

  return { getResorceDetailById, exportResourceDetail };
};

export default useResourceDetailAnalysis;
