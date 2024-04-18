/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableContainer, TablePagination } from '@mui/material';
import BillabilitySummaryTableToolbar from '../billability-summary-table-toolbar';
import Scrollbar from 'src/components/scrollbar';
import TableNoData from 'src/components/table/table-no-data';
import useBillabilitySummary from 'src/hooks/useBillabilitySummary';
import useBillabilitySummaryQueryStore from 'src/components/ui/stores/billabilitySummaryStore';
import { toast } from 'sonner';
import TableSkeleton from 'src/components/table-skeleton';
import TableHeaders from 'src/components/table/table-headers';
import { useTableControls } from 'src/hooks/use-table-controls';
import BillabilitySummaryTableRow from '../billability-summary-table-row';
import { useNavigate } from 'react-router-dom/dist';
import useResourceHistoryQueryStore from 'src/components/ui/stores/resourceHistoryStore';

// ----------------------------------------------------------------------

export default function BillabilitySummaryView() {
  const billabilitySummaryQuery = useBillabilitySummaryQueryStore((s) => s.billabilitySummaryQuery);
  const { getBillabilitySummary } = useBillabilitySummary();
  const setStartDateFilter = useResourceHistoryQueryStore((s) => s.setStartDateFilter);
  const setEndDateFilter = useResourceHistoryQueryStore((s) => s.setEndDateFilter);

  //   Use the useTableControls hook
  const {
    page,
    order,
    orderBy,
    filterName,
    rowsPerPage,
    handleSort,
    handleChangePage,
    handleChangeRowsPerPage,
    handleFilterByName,
  } = useTableControls(useBillabilitySummaryQueryStore, 'asc', 'name', 'billabilitySummaryQuery');
  const [billabilitySummary, setBillabilitySummary] = useState([]);
  const [totalElements, setTotalElements] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  useEffect(() => {
    fetchBillabilitySummaryData();
  }, [billabilitySummaryQuery]);

  const navigateToResourceDetailAnalysis = (resourceId) => {
    setStartDateFilter(billabilitySummaryQuery.startDate);
    setEndDateFilter(billabilitySummaryQuery.endDate);
    navigate(`/billabilitySummary/resourceDetailAnalysis/${resourceId}`);
  };

  const fetchBillabilitySummaryData = () => {
    setIsLoading(true);
    getBillabilitySummary.mutate(billabilitySummaryQuery, {
      onSuccess: (data) => {
        setTotalElements(data.totalItems);
        setBillabilitySummary(data.items);
        setIsLoading(false);
        // If the total number of elements is less than or equal to the current page number multiplied by the page size
        if (data.items.length === 0 && page > 0) {
          // Set the page number to the previous page
          const newPage = page - 1;
          handleChangePage(null, newPage);
        }
      },
      onError: () => {
        toast.error('Something went wrong');
        setIsLoading(false);
      },
    });
  };

  const notFound = billabilitySummary?.length == 0;

  return (
    <Card maxWidth="100%">
      <BillabilitySummaryTableToolbar filterName={filterName} onFilterName={handleFilterByName} />

      <Scrollbar>
        <TableContainer sx={{ overflow: 'unset' }}>
          <Table sx={{ minWidth: 800 }}>
            <TableHeaders
              order={order}
              orderBy={orderBy}
              rowCount={billabilitySummary.length}
              onRequestSort={handleSort}
              sortableHeads={['billableDays', 'benchDays']}
              headLabel={[
                { id: 'empId', label: 'Employee Id', width: '15%' },
                { id: 'resourceName', label: 'Name', width: '15%' },
                { id: 'department', label: 'Department', width: '10%' },
                {
                  id: 'projectName',
                  label: 'Project Name',
                  width: '15%',
                },
                { id: 'skill', label: 'Skill', width: '15%' },
                {
                  id: 'exp',
                  label: (
                    <>
                      Total
                      <br />
                      Experience
                      <br />
                      (Years)
                    </>
                  ),
                  align: 'center',
                  width: '10%',
                },
                {
                  id: 'billableDays',
                  label: (
                    <>
                      Billable
                      <br />
                      Days
                    </>
                  ),
                  align: 'center',
                  width: '10%',
                },
                {
                  id: 'benchDays',
                  label: (
                    <>
                      Bench
                      <br />
                      Days
                    </>
                  ),
                  align: 'center',
                  width: '10%',
                },
              ]}
            />
            <TableBody>
              {isLoading ? (
                <TableSkeleton rowsNum={20} colsNum={8} />
              ) : (
                billabilitySummary?.map((row) => {
                  return (
                    <BillabilitySummaryTableRow
                      key={row.id}
                      resourceId={row.resourceId}
                      empId={row.employeeId}
                      name={row.name}
                      department={row.departmentName}
                      project={row.projectName}
                      skill={row.resourceSkillResponseDTOs}
                      exp={row.totalExperience}
                      benchDays={row.benchDays}
                      billableDays={row.billableDays}
                      handleNavigation={navigateToResourceDetailAnalysis}
                    />
                  );
                })
              )}
              {notFound && <TableNoData />}
            </TableBody>
          </Table>
        </TableContainer>
      </Scrollbar>

      <TablePagination
        page={page}
        component="div"
        count={totalElements}
        rowsPerPage={rowsPerPage}
        onPageChange={handleChangePage}
        rowsPerPageOptions={[5, 10, 20, 25, 50, 100]}
        onRowsPerPageChange={handleChangeRowsPerPage}
        showFirstButton={true}
        showLastButton={true}
      />
    </Card>
  );
}
