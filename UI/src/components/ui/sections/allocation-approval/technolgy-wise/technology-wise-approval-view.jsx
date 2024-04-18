/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableContainer, TablePagination } from '@mui/material';
import { toast } from 'sonner';
import Scrollbar from 'src/components/scrollbar';
import TableNoData from 'src/components/table/table-no-data';
import { useNavigate } from 'react-router-dom';
import useAuth from 'src/hooks/use-auth';
import TableHeaders from 'src/components/table/table-headers';
import TableToolbar from 'src/components/table/table-toolbar';
import useTechnologyWiseAllocation from 'src/hooks/use-technology-wise-allocation';
import TechnologyWiseApprovalFilters from './technology-wise-approval-filter';
import TechnologyWiseApprovalTableRow from './technology-wise-approval-table-row';
import DialogBox from 'src/components/dialog';
import useAllocationApprovalResourceQueryStore from 'src/components/ui/stores/allocationApprovalResourceStore';
import useTechnologyWiseApprovalQueryStore from 'src/components/ui/stores/technologyWiseApprovalStore';
import RequestReject from 'src/components/ui/request-reject';
import TableSkeleton from 'src/components/table-skeleton';
import { errorCodeMap } from 'src/utils/error-codes';
// ----------------------------------------------------------------------

export default function TechnologyWiseApprovalView() {
  const navigate = useNavigate();
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  const technologyWiseRequestQuery = useTechnologyWiseApprovalQueryStore(
    (s) => s.technologyWiseRequestQuery
  );
  const [isLoading, setIsLoading] = useState(false);
  const setPageNumber = useTechnologyWiseApprovalQueryStore((s) => s.setPageNumber);
  const setPageSize = useTechnologyWiseApprovalQueryStore((s) => s.setPageSize);
  const setSortOrder = useTechnologyWiseApprovalQueryStore((s) => s.setSortOrder);
  const setSortKey = useTechnologyWiseApprovalQueryStore((s) => s.setSortKey);
  //setting the request detail in the store
  const setRequestId = useAllocationApprovalResourceQueryStore((s) => s.setRequestId);

  const {
    getTechnologyWiseAllocationRequests,
    approveTechnologyRequestByHod,
    rejectTechnologyRequest,
  } = useTechnologyWiseAllocation();
  const [page, setPage] = useState(technologyWiseRequestQuery.pageNumber);
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
  const [rejectedRowId, setRejectedRowId] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);

  useEffect(() => {
    fetchRequest();
  }, [technologyWiseRequestQuery]);

  const fetchRequest = () => {
    setIsLoading(true);
    getTechnologyWiseAllocationRequests.mutate(
      { queryData: technologyWiseRequestQuery, isRequestList: false },
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
  const handleDialogOpen = () => {
    setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setDialogOpen(false);
  };
  //approve technology request by hod
  const approveRequestByHod = (id) => {
    setIsLoading(true);
    approveTechnologyRequestByHod.mutate(id, {
      onSuccess: () => {
        toast.success('Resource request approved successfully');
        fetchRequest();
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
        fetchRequest();
      },
    });
  };

  const handleApprove = (data) => {
    //check if the user is hod or not
    if (currentLoggedUser === 2) {
      approveRequestByHod(data.id);
    } else {
      setRequestId(data.id);
      // if its hr navigate to hr approval page
      navigate('/resourceManagement/allocationApprovals/technologyWiseAllocateAndApprove');
    }
  };

  const handleReject = (id) => {
    setRejectedRowId(id);
    handleDialogOpen();
  };

  const RejectWithReason = (reason) => {
    handleDialogClose();
    rejectTechnologyRequest.mutate(
      { id: rejectedRowId, reason: reason },
      {
        onSuccess: () => {
          toast.success(' Request rejected successfully');
          setRejectedRowId(null);

          fetchRequest();
        },
        onError: (error) => {
          toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
          fetchRequest();
        },
      }
    );
  };

  const dialogContent = {
    content: (
      <div>
        <RequestReject onSubmit={RejectWithReason} />
      </div>
    ),
    name: 'Reject With Reason',
    width: '560px',
    showActions: false,
  };

  const notFound = technologyWiseRequestList?.length == 0;

  return (
    <div>
      <Card>
        <TableToolbar>
          <TechnologyWiseApprovalFilters />
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
                    width: '10%',
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
                    width: '13%',
                  },
                  {
                    id: 'requestedBy',
                    label: (
                      <>
                        Requested
                        <br />
                        By
                      </>
                    ),
                    width: '6%',
                    align: 'center',
                  },
                  { id: 'department', label: 'Department', width: '6%' },
                  { id: 'skill', label: 'Skill', width: '13%' },
                  {
                    id: 'experience',
                    label: (
                      <>
                        Total
                        <br />
                        Experience
                      </>
                    ),
                    width: '6%',
                    align: 'center',
                  },
                  { id: 'count', label: 'Count', width: '4%' },
                  {
                    id: 'allocationPeriod',
                    label: (
                      <>
                        Allocation
                        <br />
                        Period
                      </>
                    ),
                    width: '8%',
                    align: 'center',
                  },
                  { id: 'status', label: 'Status', width: '6%', align: 'center' },
                  { id: '', width: '6%' },
                ]}
              />
              <TableBody>
                {isLoading ? (
                  <TableSkeleton rowsNum={20} colsNum={9} />
                ) : (
                  technologyWiseRequestList?.map((row) => {
                    return (
                      <TechnologyWiseApprovalTableRow
                        key={row?.id}
                        id={row?.id}
                        projectId={row?.projectId}
                        projectCode={row?.projectCode}
                        projectName={row?.projectName}
                        managerName={row?.managerName}
                        department={row?.departmentName}
                        skill={row?.skillExperienceResponseDTOs}
                        experience={row?.experience}
                        count={row?.count}
                        allocationType={row.allocationType}
                        startDate={row?.startDate}
                        endDate={row?.endDate}
                        approvalStatus={row?.approvalFlow}
                        onApprove={handleApprove}
                        onReject={handleReject}
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
      {dialogOpen && (
        <DialogBox
          content={dialogContent}
          open={dialogOpen}
          handleClose={handleDialogClose}
          showActions={false}
        />
      )}
    </div>
  );
}
