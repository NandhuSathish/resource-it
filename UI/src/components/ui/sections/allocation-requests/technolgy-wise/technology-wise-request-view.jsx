/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableContainer, TablePagination } from '@mui/material';
import { toast } from 'sonner';
import Scrollbar from 'src/components/scrollbar';
import TableNoData from 'src/components/table/table-no-data';
import useTechnologyWiseRequestQueryStore from 'src/components/ui/stores/technologyWiseRequestStore';
import TableHeaders from 'src/components/table/table-headers';
import TechnologyWiseRequestTableRow from './technology-wise-request-table-row';
import TableToolbar from 'src/components/table/table-toolbar';
import TechnologyWiseRequestFilters from './technology-wise-request-filter';
import useTechnologyWiseAllocation from 'src/hooks/use-technology-wise-allocation';
import TableSkeleton from 'src/components/table-skeleton';
// ----------------------------------------------------------------------

export default function TechnologyWiseRequestView() {
  const technologyWiseRequestQuery = useTechnologyWiseRequestQueryStore(
    (s) => s.technologyWiseRequestQuery
  );

  const { getTechnologyWiseAllocationRequests, deleteTechnologyRequest } =
    useTechnologyWiseAllocation();
  const [page, setPage] = useState(technologyWiseRequestQuery.pageNumber);
  const [isLoading, setIsLoading] = useState(false);
  //   page number need to be in sync with the Store.
  useEffect(() => {
    setPage(technologyWiseRequestQuery.pageNumber);
  }, [technologyWiseRequestQuery.pageNumber]);
  //when the page number changes, the page should be scrolled to the top
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  const [technologyWiseRequestList, setTechnologyWiseRequestList] = useState([]);
  const [order, setOrder] = useState('asc');
  const [orderBy, setOrderBy] = useState('name');
  const [rowsPerPage, setRowsPerPage] = useState(technologyWiseRequestQuery.pageSize);
  const [totalElements, setTotalElements] = useState(0);

  const setPageNumber = useTechnologyWiseRequestQueryStore((s) => s.setPageNumber);
  const setPageSize = useTechnologyWiseRequestQueryStore((s) => s.setPageSize);
  const setSortOrder = useTechnologyWiseRequestQueryStore((s) => s.setSortOrder);
  const setSortKey = useTechnologyWiseRequestQueryStore((s) => s.setSortKey);

  useEffect(() => {
    fetchRequest();
  }, [technologyWiseRequestQuery]);

  const fetchRequest = () => {
    setIsLoading(true);
    getTechnologyWiseAllocationRequests.mutate(
      { queryData: technologyWiseRequestQuery, isRequestList: true },
      {
        onSuccess: (data) => {
          setTotalElements(data.totalItems);
          setTechnologyWiseRequestList(data.items);
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
  const handleDelete = (id) => {
    deleteTechnologyRequest.mutate(id, {
      onSuccess: () => {
        toast.success('Request deleted successfully');
        fetchRequest();
      },
      onError: () => {
        toast.error('Deletion is restricted as the request is currently under process.');
      },
    });
  };

  const notFound = technologyWiseRequestList?.length == 0;

  return (
    <div>
      <Card>
        <TableToolbar>
          <TechnologyWiseRequestFilters />
        </TableToolbar>
        <Scrollbar>
          <TableContainer sx={{ overflow: 'unset' }}>
            <Table sx={{ minWidth: 800 }}>
              <TableHeaders
                order={order}
                orderBy={orderBy}
                rowCount={technologyWiseRequestList.length}
                onRequestSort={handleSort}
                sortableHeads={[]}
                isSelectable={false}
                headLabel={[
                  {
                    id: 'projectCode',
                    label: (
                      <>
                        Project
                        <br />
                        Code
                      </>
                    ),
                    width: '15%',
                  },
                  {
                    id: 'projectName',
                    label: (
                      <>
                        Project
                        <br />
                        Name
                      </>
                    ),
                    width: '15%',
                  },
                  { id: 'department', label: 'Department', width: '7%' },
                  { id: 'skill', label: 'Skill', width: '15%' },
                  {
                    id: 'experience',
                    label: (
                      <>
                        Total
                        <br />
                        Experience
                      </>
                    ),
                    align: 'center',
                    width: '7%',
                  },
                  { id: 'count', label: 'Count', align: 'center', width: '6%' },
                  {
                    id: 'allocationPeriod',
                    label: (
                      <>
                        Allocation
                        <br />
                        Period
                      </>
                    ),
                    align: 'center',
                    width: '15%',
                  },
                  { id: 'status', label: 'Status', align: 'center', width: '8%' },
                  { id: '', width: '7%' },
                ]}
              />
              <TableBody>
                {isLoading ? (
                  <TableSkeleton rowsNum={20} colsNum={9} />
                ) : (
                  technologyWiseRequestList?.map((row) => {
                    return (
                      <TechnologyWiseRequestTableRow
                        key={row?.id}
                        id={row?.id}
                        projectCode={row?.projectCode}
                        projectName={row?.projectName}
                        department={row?.departmentName}
                        skill={row?.skillExperienceResponseDTOs}
                        experience={row?.experience}
                        count={row?.count}
                        startDate={row?.startDate}
                        endDate={row?.endDate}
                        approvalStatus={row?.approvalFlow}
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
