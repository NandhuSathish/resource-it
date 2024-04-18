/* eslint-disable react/react-in-jsx-scope */
import { useCallback, useEffect, useState } from 'react';
import { Card, Table, TableBody, TableContainer, TablePagination, Typography } from '@mui/material';
import { toast } from 'sonner';
import Scrollbar from 'src/components/scrollbar';
import TableNoData from 'src/components/table/table-no-data';
import useResourceApprovalQueryStore from 'src/components/ui/stores/resourceApprovalStore';
import TableToolbar from 'src/components/table/table-toolbar';
import TableHeaders from 'src/components/table/table-headers';
import useResourceAllocation from 'src/hooks/use-resource-allocation';
import DialogBox from 'src/components/dialog';
import RequestReject from 'src/components/ui/request-reject';
import ResourceWiseApprovalTableRow from './resource-wise-approval-table-row';
import ResourceWiseApprovalFilters from './resource-wise-approval-filter';
import { useConfirm } from 'material-ui-confirm';
import useConflictCheck from 'src/hooks/use-conflict-check';
import { errorCodeMap } from 'src/utils/error-codes';
import ConflictTable from 'src/components/conflict-table';
import { confirmationButtonProps, dialogProps } from 'src/utils/confirmationDialogProps';
import dayjs from 'dayjs';
import TableSkeleton from 'src/components/table-skeleton';
import customParseFormat from 'dayjs/plugin/customParseFormat';
dayjs.extend(customParseFormat);
// ----------------------------------------------------------------------

export default function ResourceWiseApprovalView() {
  const resourceRequestQuery = useResourceApprovalQueryStore((s) => s.resourceRequestQuery);
  const { getResourceAllocationRequests, approveResourceRequest, rejectResourceRequest } =
    useResourceAllocation();
  const confirm = useConfirm();
  const [page, setPage] = useState(resourceRequestQuery.pageNumber);
  const [resourceRequestList, setResourceRequestList] = useState([]);
  const [rowsPerPage, setRowsPerPage] = useState(resourceRequestQuery.pageSize);
  const [totalElements, setTotalElements] = useState(0);
  const setPageNumber = useResourceApprovalQueryStore((s) => s.setPageNumber);
  const setPageSize = useResourceApprovalQueryStore((s) => s.setPageSize);
  const [open, setOpen] = useState(false);
  const [rejectedRowId, setRejectedRowId] = useState(null);
  const { bulkConflictCheck } = useConflictCheck();
  const [isLoading, setIsLoading] = useState(false);
  const [conflictData, setConflictData] = useState();

  //   page number need to be in sync with the Store.
  useEffect(() => {
    setPage(resourceRequestQuery.pageNumber);
  }, [resourceRequestQuery.pageNumber]);
  //when the page number changes, the page should be scrolled to the top
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);
  //fetch the data.
  useEffect(() => {
    fetchRequest();
  }, [resourceRequestQuery]);
  // f() that manages the data fetching.
  const fetchRequest = () => {
    setIsLoading(true);
    getResourceAllocationRequests.mutate(
      { queryData: resourceRequestQuery, isRequestList: false },
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

  const handleDialogOpen = useCallback(() => {
    if (!open) {
      setOpen(true);
    }
  }, [open]);

  const handleDialogClose = () => {
    setOpen(false);
  };

  const approveResourceWiseRequest = (id) => {
    setIsLoading(true);
    approveResourceRequest.mutate(id, {
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

  const handleConflict = (data) => {
    confirm({
      title: (
        <Typography variant="h6" style={{ fontSize: '18px' }}>
          Conflicts Found
        </Typography>
      ),
      description: (
        <Scrollbar>
          <ConflictTable data={data} />
        </Scrollbar>
      ),
      confirmationButtonProps: {
        ...confirmationButtonProps,
      },
      dialogProps: {
        ...dialogProps,
      },
      cancellationButtonProps: {
        style: {
          visibility: 'hidden',
        },
      },
    })
      .then(() => {
        return;
      })
      .catch(() => {
        return;
      });
  };

  const handleApprove = (id, projectId, resourceId, allocationId, startDate, endDate) => {
    const formattedStartDate = dayjs(startDate, 'DD-MM-YYYY').format('DD-MM-YYYY');
    const formattedEndDate = dayjs(endDate, 'DD-MM-YYYY').format('DD-MM-YYYY');
    const data = {
      projectId: projectId,
      resourceId: resourceId,
      allocationId: allocationId,
      allocationStartDate: dayjs(formattedStartDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
      allocationEndDate: dayjs(formattedEndDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
    };

    //check for conflicts
    bulkConflictCheck.mutate(data, {
      onSuccess: (data) => {
        if (data.length > 0) {
          handleConflict(data);
        } else {
          approveResourceWiseRequest(id);
        }
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
      },
    });
  };

  const handleReject = (id, projectId, resourceId, allocationId, startDate, endDate) => {
    const formattedStartDate = dayjs(startDate, 'DD-MM-YYYY').format('DD-MM-YYYY');
    const formattedEndDate = dayjs(endDate, 'DD-MM-YYYY').format('DD-MM-YYYY');
    const data = {
      projectId: projectId,
      resourceId: resourceId,
      allocationId: allocationId,
      allocationStartDate: dayjs(formattedStartDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
      allocationEndDate: dayjs(formattedEndDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
    };
    bulkConflictCheck.mutate(data, {
      onSuccess: (data) => {
        if (data.length > 0) {
          let conflictsString =
            'Conflicts found:\n' +
            data
              .map((item) => `${item.projectName} - ${item.startDate} - ${item.endDate}`)
              .join('\n');
          setConflictData(conflictsString);
        } else {
          setConflictData('');
        }
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
      },
      onSettled: () => {
        setRejectedRowId(id);
        handleDialogOpen();
      },
    });
  };

  const RejectWithReason = (reason) => {
    handleDialogClose();
    rejectResourceRequest.mutate(
      { id: rejectedRowId, reason: reason },
      {
        onSuccess: () => {
          toast.success('Resource request rejected successfully');
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
        <RequestReject conflictData={conflictData} onSubmit={RejectWithReason} />
      </div>
    ),
    name: 'Reject With Reason',
    width: '560px',
    showActions: false,
  };

  const notFound = resourceRequestList?.length == 0;

  return (
    <div>
      <Card>
        <TableToolbar>
          <ResourceWiseApprovalFilters sx={{}} />
        </TableToolbar>
        <Scrollbar>
          <TableContainer sx={{ overflow: 'unset' }}>
            <Table sx={{ minWidth: 800 }}>
              <TableHeaders
                rowCount={resourceRequestList.length}
                sortableHeads={[]}
                isSelectable={false}
                headLabel={[
                  { id: 'projectCode', label: 'Project Code', width: '17%' },
                  { id: 'projectName', label: 'Project Name', width: '18%' },
                  {
                    id: 'requestedBy',
                    label: (
                      <>
                        Requested
                        <br />
                        By
                      </>
                    ),
                    width: '8%',
                    align: 'center',
                  },
                  {
                    id: 'resourceName',
                    label: (
                      <>
                        Resource
                        <br />
                        Name
                      </>
                    ),
                    width: '8%',
                    align: 'center',
                  },
                  { id: 'department', label: 'Department', width: '8%' },
                  {
                    id: 'allocationPeriod',
                    label: 'Allocation Period',
                    align: 'center',
                    width: '15%',
                  },
                  { id: 'conflictDays', label: 'Conflict Days', align: 'center', width: '8%' },
                  { id: 'status', label: 'Status', width: '8%', align: 'center' },
                  { id: '', width: '10%' },
                ]}
              />
              <TableBody>
                {isLoading ? (
                  <TableSkeleton rowsNum={20} colsNum={9} />
                ) : (
                  resourceRequestList?.map((row) => {
                    return (
                      <ResourceWiseApprovalTableRow
                        key={row.id}
                        id={row.id}
                        projectCode={row.projectCode}
                        projectName={row.projectName}
                        projectManager={row.requestedByName}
                        resourceName={row.resourceName}
                        empId={row.resourceEmployeeId}
                        departmentName={row.departmentName}
                        startDate={row.startDate}
                        endDate={row.endDate}
                        resourceId={row.resourceId}
                        projectId={row.projectId}
                        conflictDays={row.conflictDays}
                        approvalStatus={row.approvalFlow}
                        allocationId={row.allocationId}
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
      <DialogBox
        content={dialogContent}
        open={open}
        handleClose={handleDialogClose}
        showActions={false}
      />
    </div>
  );
}
