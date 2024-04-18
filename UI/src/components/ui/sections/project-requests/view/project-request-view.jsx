/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableContainer, TablePagination } from '@mui/material';
import { toast } from 'sonner';
import Scrollbar from 'src/components/scrollbar';
import TableNoData from 'src/components/table/table-no-data';
import useProjectRequests from 'src/hooks/use-project-requests';
import useProjectRequestQueryStore from 'src/components/ui/stores/projectRequestStore';
import useAuth from 'src/hooks/use-auth';
import ProjectRequestTableToolbar from '../project-request-table-toolbar';
import ProjectRequestTableRow from '../project-request-table-row';
import TableSkeleton from 'src/components/table-skeleton';
import TableHeaders from 'src/components/table/table-headers';
import { errorCodeMap } from 'src/utils/error-codes';

// ----------------------------------------------------------------------

export default function ProjectRequestView() {
  const projectRequestQuery = useProjectRequestQueryStore((s) => s.projectRequestQuery);
  const { getProjectRequests } = useProjectRequests();
  const [page, setPage] = useState(projectRequestQuery.pageNumber);
  //   page number need to be in sync with the Store.
  useEffect(() => {
    setPage(projectRequestQuery.pageNumber);
  }, [projectRequestQuery.pageNumber]);

  //when the page number changes, the page should be scrolled to the top
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  const [projectRequestList, setProjectRequestList] = useState([]);
  const [order, setOrder] = useState('asc');
  const [orderBy, setOrderBy] = useState('name');
  const [isLoading, setIsLoading] = useState(false);
  const [rowsPerPage, setRowsPerPage] = useState(projectRequestQuery.pageSize);
  const [totalElements, setTotalElements] = useState(0);

  const setPageNumber = useProjectRequestQueryStore((s) => s.setPageNumber);
  const setPageSize = useProjectRequestQueryStore((s) => s.setPageSize);
  const setSortOrder = useProjectRequestQueryStore((s) => s.setSortOrder);
  const setSortKey = useProjectRequestQueryStore((s) => s.setSortKey);
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  const { approveEditedProject } = useProjectRequests();
  useEffect(() => {
    fetchRequest();
  }, [projectRequestQuery]);

  const isRequestList = currentLoggedUser !== 2;

  const fetchRequest = () => {
    setIsLoading(true);
    getProjectRequests.mutate(
      { queryData: projectRequestQuery, isRequestList: isRequestList },
      {
        onSuccess: (data) => {
          setTotalElements(data.totalItems);
          setProjectRequestList(data.items);
          setIsLoading(false);
          if (data.totalItems <= page * rowsPerPage) {
            // Set the page number to the previous page
            const newPage = Math.max(0, page - 1);
            if (newPage >= 0 && newPage !== page) {
              setPage(newPage);
              setPageNumber(newPage);
            }
          }
        },
        onError: () => {
          toast.error('Something went wrong');
          setIsLoading(false);
        },
      }
    );
  };

  const handleSetLoading = (state) => {
    setIsLoading(state);
  };

  const handleSort = (event, id) => {
    const isAsc = orderBy === id && order === 'asc';
    if (id !== '') {
      const newOrder = isAsc ? 'desc' : 'asc';
      setOrder(newOrder);
      setOrderBy(id);
      setSortOrder(newOrder !== 'desc');
      setSortKey(id);
    }
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
    setPageNumber(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    const newRowsPerPage = parseInt(event.target.value, 10);
    setPageNumber(0);
    setRowsPerPage(newRowsPerPage);
    setPageSize(newRowsPerPage);
  };

  const approveEditedProjectFunction = (requests) => {
    setIsLoading(true);
    approveEditedProject.mutate(requests, {
      onSuccess: () => {
        fetchRequest();
        toast.success('Project request approved successfully');
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
        fetchRequest();
      },
    });
  };
  const notFound = projectRequestList?.length == 0;

  return (
    <Card maxWidth="100%">
      <ProjectRequestTableToolbar />
      <Scrollbar>
        <TableContainer sx={{ overflow: 'unset' }}>
          <Table sx={{ minWidth: 800 }}>
            <TableHeaders
              order={order}
              orderBy={orderBy}
              rowCount={projectRequestList.length}
              onRequestSort={handleSort}
              sortableHeads={[]}
              headLabel={[
                { id: 'projectCode', label: 'Project Code', width: '15%' },
                { id: 'projectName', label: 'Project Name', width: '15%' },
                { id: 'projectType', label: 'Project Type', width: '10%' },
                { id: 'projectManager', label: 'Project Manager', width: '10%' },
                { id: 'startDate', label: 'Start Date', width: '10%' },
                { id: 'endDate', label: 'End Date', width: '10%' },
                { id: 'estimatedDays', label: 'Estimated Man Days', align: 'center', width: '8%' },
                { id: 'status', label: 'Approval Status', width: '10%' },
                { id: '', width: '12%' },
              ]}
            />
            <TableBody>
              {isLoading ? (
                <TableSkeleton rowsNum={20} colsNum={9} />
              ) : (
                projectRequestList?.map((row) => {
                  return (
                    <ProjectRequestTableRow
                      key={row.projectRequestId}
                      id={row.projectRequestId}
                      project={row?.project}
                      projectCode={row.projectCode}
                      projectName={row.name}
                      projectType={row.projectType}
                      projectManager={row?.manager?.name}
                      estimatedDays={row.manDay}
                      startDate={row.startDate}
                      endDate={row.endDate}
                      approvalStatus={row.approvalStatus}
                      editedFields={row.editedFields}
                      fetch={fetchRequest}
                      setIsLoading={handleSetLoading}
                      approveEditedProjectFunction={approveEditedProjectFunction}
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
