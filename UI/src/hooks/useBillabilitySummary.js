import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from 'src/services/interceptor';
import useDownload from './use-download';
import useBillabilitySummaryQueryStore from 'src/components/ui/stores/billabilitySummaryStore';
import dayjs from 'dayjs';

const useBillabilitySummary = () => {
  const billabilitySummaryQuery = useBillabilitySummaryQueryStore((s) => s.billabilitySummaryQuery);
  const formattedStartDate = dayjs(billabilitySummaryQuery.startDate).format('MMM DD YYYY');
  const formattedEndDate = dayjs(billabilitySummaryQuery.endDate).format('MMM DD YYYY');
  const getBillabilitySummary = useMutation({
    mutationFn: (data) => {
      return axiosInstance
        .post('/statistics/billability/list', { ...data })
        .then((res) => res.data);
    },
  });

  const filename = `Billability_Summary_Report_${formattedStartDate}_to_${formattedEndDate}.xlsx`;
  const exportBillabilitySummary = useDownload(
    '/statistics/billability/download',
    'post',
    filename
  );

const currentDay = dayjs().format('MMM DD YYYY'); 
  const detailFilename = `Billability_Summary_Detail_Report_${currentDay}.xlsx`;
  const exportBillabilitySummaryDetail = useDownload(
    '/statistics/allocation/download',
    'post',
    detailFilename
  );

  return {
    getBillabilitySummary,
    exportBillabilitySummary,
    exportBillabilitySummaryDetail,
  };
};

export default useBillabilitySummary;
