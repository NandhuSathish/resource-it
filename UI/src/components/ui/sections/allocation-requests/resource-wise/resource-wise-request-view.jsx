/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableContainer, TablePagination } from '@mui/material';
import { toast } from 'sonner';
import Scrollbar from 'src/components/scrollbar';
import TableNoData from 'src/components/table/table-no-data';
import useResourceRequestQueryStore from 'src/components/ui/stores/resourceRequestStore';
import TableToolbar from 'src/components/table/table-toolbar';
import ResourceWiseRequestFilters from './resource-wise-request-filter';
import TableHeaders from 'src/components/table/table-headers';
import ResourceWiseRequestTableRow from './resource-wise-request-table-row';
import useResourceAllocation from 'src/hooks/use-resource-allocation';
import TableSkeleton from 'src/components/table-skeleton';

// ----------------------------------------------------------------------
export default function ResourceWiseRequestView() {
  const resourceRequestQuery = useResourceRequestQueryStore((s) => s.resourceRequestQuery);
  const { getResourceAllocationRequests, deleteResourceRequest } = useResourceAllocation();
  const [page, setPage] = useState(resourceRequestQuery.pageNumber);
  const [isLoading, setIsLoading] = useState(false);
  //   page number need to be in sync with the Store.
  useEffect(() => {
    setPage(resourceRequestQuery.pageNumber);
  }, [resourceRequestQuery.pageNumber]);

  //when the page number changes, the page should be scrolled to the top
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  const [resourceRequestList, setResourceRequestList] = useState([]);
  const [rowsPerPage, setRowsPerPage] = useState(resourceRequestQuery.pageSize);
  const [totalElements, setTotalElements] = useState(0);
  const setPageNumber = useResourceRequestQueryStore((s) => s.setPageNumber);
  const setPageSize = useResourceRequestQueryStore((s) => s.setPageSize);

  useEffect(() => {
    fetchRequest();
  }, [resourceRequestQuery]);

  const fetchRequest = () => {
    setIsLoading(true);
    getResourceAllocationRequests.mutate(
      { queryData: resourceRequestQuery, isRequestList: true },
      {
        onSuccess: (data) => {
          setTotalElements(data.totalItems);
          setResourceRequestList(data.items);
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

  const handleDelete = (id) => {
    deleteResourceRequest.mutate(id, {
      onSuccess: () => {
        toast.success('Request deleted successfully');
        fetchRequest();
      },
      onError: () => {
        toast.error('Deletion is restricted as the request is currently under process');
      },
    });
  };

  const notFound = resourceRequestList?.length == 0;

  return (
    <div>
      <Card>
        <TableToolbar>
          <ResourceWiseRequestFilters sx={{}} />
        </TableToolbar>

        <Scrollbar>
          <TableContainer sx={{ overflow: 'unset' }}>
            <Table sx={{ minWidth: 800 }}>
              <TableHeaders
                rowCount={resourceRequestList.length}
                sortableHeads={[]}
                isSelectable={false}
                headLabel={[
                  { id: 'projectCode', label: 'Project Code', width: '15%' },
                  { id: 'projectName', label: 'Project Name', width: '15%' },
                  { id: 'resourceName', label: 'Resource Name', width: '15%' },
                  { id: 'department', label: 'Department', width: '15%' },
                  {
                    id: 'allocationPeriod',
                    label: 'Allocation Period',
                    align: 'center',
                    width: '20%',
                  },
                  { id: 'status', label: 'Status', align: 'center', width: '10%' },
                  { id: 'actions', width: '10%' },
                ]}
              />
              <TableBody>
                {isLoading ? (
                  <TableSkeleton rowsNum={20} colsNum={7} />
                ) : (
                  resourceRequestList?.map((row) => {
                    return (
                      <ResourceWiseRequestTableRow
                        key={row.id}
                        id={row.id}
                        projectCode={row.projectCode}
                        projectName={row.projectName}
                        resourceName={row.resourceName}
                        empId={row.resourceEmployeeId}
                        departmentName={row.departmentName}
                        startDate={row.startDate}
                        endDate={row.endDate}
                        approvalStatus={row.approvalFlow}
                        onDelete={handleDelete}
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
    </div>
  );
}
