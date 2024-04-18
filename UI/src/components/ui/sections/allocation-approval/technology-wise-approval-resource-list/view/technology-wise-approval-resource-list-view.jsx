/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';

import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import TablePagination from '@mui/material/TablePagination';
import { useNavigate } from 'react-router-dom';
import { users } from 'src/_mock/user';
import Scrollbar from 'src/components/scrollbar';
import TableNoData from 'src/components/table/table-no-data';
import { Grid, IconButton, Typography } from '@mui/material';
import useResources from 'src/hooks/useResources';
import { toast } from 'sonner';
import useAllocationApprovalResourceQueryStore from 'src/components/ui/stores/allocationApprovalResourceStore';
import { errorCodeMap } from 'src/utils/error-codes';
import TechnologyWiseApprovalResourceListTableRow from '../technology-wise-approval-resource-list-table-row';
import Iconify from 'src/components/iconify';
import useTechnologyWiseAllocation from 'src/hooks/use-technology-wise-allocation';
import TechnologyWiseApprovalResourceListTableToolbar from '../technology-wise-approval-resource-list-table-toolbar';
import { transformRequestForApproval } from '../../utils';
import useConflictCheck from 'src/hooks/use-conflict-check';
import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';
dayjs.extend(customParseFormat);
import { useConfirm } from 'material-ui-confirm';
import { confirmationButtonProps, dialogProps } from 'src/utils/confirmationDialogProps';
import ConflictTable from 'src/components/conflict-table';
import TableSkeleton from 'src/components/table-skeleton';
import TableHeaders from 'src/components/table/table-headers';
import TechnologyWiseApprovalResourceSelectedList from '../technology-wise-approval-resource-selected-list';

// ----------------------------------------------------------------------
export default function TechnologyWiseApprovalResourceListView() {
  const navigate = useNavigate();
  const { getResourcesForAllocation } = useResources();
  const { getTechnologyWiseAllocationRequestById } = useTechnologyWiseAllocation();
  const { approveTechnologyRequestByHR } = useTechnologyWiseAllocation();
  const resourceQuery = useAllocationApprovalResourceQueryStore((s) => s.resourceQuery);
  const allocationRequestQuery = useAllocationApprovalResourceQueryStore(
    (s) => s.allocationRequestQuery
  );
  const requestDetails = useAllocationApprovalResourceQueryStore((s) => s.requestDetails);
  const [resourceList, setResourceList] = useState([]);
  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState(allocationRequestQuery.resources);
  const [rowsPerPage, setRowsPerPage] = useState(resourceQuery.pageSize);
  const [totalElements, setTotalElements] = useState(0);
  const setPageNumber = useAllocationApprovalResourceQueryStore((s) => s.setPageNumber);
  const setPageSize = useAllocationApprovalResourceQueryStore((s) => s.setPageSize);
  const setResources = useAllocationApprovalResourceQueryStore((s) => s.setResources);
  const setRequestDetails = useAllocationApprovalResourceQueryStore((s) => s.setRequestDetails);
  const { bulkConflictCheck } = useConflictCheck();
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    fetchRequestDetails(requestDetails.requestId);
  }, []);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  useEffect(() => {
    setPage(resourceQuery.pageNumber);
  }, [resourceQuery.pageNumber]);

  useEffect(() => {
    fetchResources();
  }, [resourceQuery]);

  const fetchResources = () => {
    setIsLoading(true);
    getResourcesForAllocation.mutate(resourceQuery, {
      onSuccess: (data) => {
        setTotalElements(data.totalItems);
        setResourceList(data.items);
        setIsLoading(false);
      },
      onError: () => {
        toast.error('Something went wrong');
        setIsLoading(false);
      },
    });
  };

  useEffect(() => {
    setSelected(allocationRequestQuery.resources);
  }, [allocationRequestQuery.resources]);

  //fetch the request details
  const fetchRequestDetails = async (id) => {
    if (id === null) {
      navigate(-1);
      return;
    }
    try {
      const data = await getTechnologyWiseAllocationRequestById.mutateAsync(id);
      setRequestDetails(data);
    } catch (error) {
      toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
    }
  };

  const handleClick = (event, row) => {
    const selectedIndex = selected.findIndex((item) => item.id === row.id);
    let newSelected = [];
    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, row);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selected.slice(1));
    } else if (selectedIndex === selected.length - 1) {
      newSelected = newSelected.concat(selected.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(
        selected.slice(0, selectedIndex),
        selected.slice(selectedIndex + 1)
      );
    }
    setSelected(newSelected);
    setResources(newSelected);
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

  const notFound = resourceList?.length == 0;
  const transformRequestForConflictCheck = (request) => {
    return {
      allocationStartDate: dayjs(request.startDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
      allocationEndDate: dayjs(request.endDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
      projectId: request.projectId,
      resourceId: request.resources.map((resource) => resource.id),
    };
  };

  const confirm = useConfirm();
  const handleDeselect = (resourceToDeselect) => {
    const newSelected = selected.filter((resource) => resource.id !== resourceToDeselect.id);
    setSelected(newSelected);
    setResources(newSelected);
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
      .catch(() => {});
  };

  const handleRequest = () => {
    const transformedRequest = transformRequestForApproval(allocationRequestQuery);
    //check conflict .
    transformRequestForConflictCheck(allocationRequestQuery);
    bulkConflictCheck.mutate(transformRequestForConflictCheck(allocationRequestQuery), {
      onSuccess: (data) => {
        if (data.length > 0) {
          handleConflict(data);
        } else {
          approveTechnologyWiseByRequest(transformedRequest);
        }
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
      },
    });
  };

  const approveTechnologyWiseByRequest = (request) => {
    approveTechnologyRequestByHR.mutate(
      { id: requestDetails.requestId, data: request },
      {
        onSuccess: () => {
          toast.success('Request approved successfully');
          navigate(-1);
        },
        onError: (error) => {
          const errorMessage =
            errorCodeMap[error.response.data.errorCode] || 'An unexpected error occurred';
          toast.error(errorMessage);
        },
      }
    );
  };

  const handleProjectChange = () => {
    setSelected([]);
  };

  return (
    <div>
      <Stack direction="row" alignItems="center" mb={5}>
        <IconButton
          onClick={() => {
            navigate(-1);
          }}
        >
          <Iconify width={20} height={20} icon={'eva:arrow-ios-back-fill'} />
        </IconButton>
        <Typography variant="h4" sx={{ marginLeft: 2 }}>
          Allocate And Approve
        </Typography>
      </Stack>
      <Grid container spacing={2}>
        <Grid item xs={12} md={8}>
          <Card sx={{ p: 3 }}>
            <TechnologyWiseApprovalResourceListTableToolbar
              handleProjectChange={handleProjectChange}
            />

            <Scrollbar>
              <TableContainer sx={{ overflow: 'unset' }}>
                <Table sx={{ minWidth: 550 }}>
                  <TableHeaders
                    rowCount={users.length}
                    numSelected={selected.length}
                    headLabel={[
                      { id: '', label: '' },
                      { id: 'empId', label: 'Employee Id', align: 'center' },
                      { id: 'name', label: 'Name' },
                      { id: 'department', label: 'Department' },
                      { id: 'skill', label: 'Skill' },
                    ]}
                  />
                  <TableBody>
                    {isLoading ? (
                      <TableSkeleton rowsNum={20} colsNum={5} />
                    ) : (
                      resourceList?.map((row) => (
                        <TechnologyWiseApprovalResourceListTableRow
                          key={row.id}
                          employeeId={row.code}
                          name={row.name}
                          department={row.departmentName}
                          skill={row?.resourceSkillResponseDTOs}
                          selected={selected.some((item) => item.id === row.id)}
                          selectedLength={selected.length}
                          count={requestDetails.count}
                          handleClick={(event) => handleClick(event, row)}
                        />
                      ))
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
        </Grid>

        <Grid item xs={12} md={4}>
          <TechnologyWiseApprovalResourceSelectedList
            onDeselect={handleDeselect}
            onApprove={handleRequest}
          />
        </Grid>
      </Grid>
    </div>
  );
}
