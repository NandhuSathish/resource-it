/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';

import Card from '@mui/material/Card';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import TablePagination from '@mui/material/TablePagination';
import { users } from 'src/_mock/user';
import Scrollbar from 'src/components/scrollbar';
import ResourceAllocationTableToolbar from './resource-allocation-table-toolbar';
import ResourceAllocationTableRow from './resource-allocation-table-row';
import TableNoData from 'src/components/table/table-no-data';
import { Grid, Typography } from '@mui/material';
import useResources from 'src/hooks/useResources';
import { toast } from 'sonner';
import useResourceAllocationQueryStore from 'src/components/ui/stores/resourceAllocationStore';
import { transformRequest } from '../utils';
import useResourceAllocation from 'src/hooks/use-resource-allocation';
import { errorCodeMap } from 'src/utils/error-codes';
import TableSkeleton from 'src/components/table-skeleton';
import { useConfirm } from 'material-ui-confirm';
import useConflictCheck from 'src/hooks/use-conflict-check';
import ConflictTable from 'src/components/conflict-table';
import { confirmationButtonProps, dialogProps } from 'src/utils/confirmationDialogProps';
import TableHeaders from 'src/components/table/table-headers';
import ResourceSelectedList from './resource-selected-list';
import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';
dayjs.extend(customParseFormat);
// ----------------------------------------------------------------------

export default function ResourceWiseAllocationView() {
  const { getResourcesForAllocation } = useResources();
  const { requestResource } = useResourceAllocation();
  const resourceAllocationQuery = useResourceAllocationQueryStore((s) => s.resourceAllocationQuery);
  const allocationRequestQuery = useResourceAllocationQueryStore((s) => s.allocationRequestQuery);
  const setClearAllOnRequest = useResourceAllocationQueryStore((s) => s.setClearAllOnRequest);
  const [resourceList, setResourceList] = useState([]);
  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState(allocationRequestQuery.resources);
  const [rowsPerPage, setRowsPerPage] = useState(resourceAllocationQuery.pageSize);
  const [totalElements, setTotalElements] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const setPageNumber = useResourceAllocationQueryStore((s) => s.setPageNumber);
  const setPageSize = useResourceAllocationQueryStore((s) => s.setPageSize);
  const setResources = useResourceAllocationQueryStore((s) => s.setResources);

  useEffect(() => {
    fetchResources();
  }, [resourceAllocationQuery]);
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  useEffect(() => {
    setPage(resourceAllocationQuery.pageNumber);
  }, [resourceAllocationQuery.pageNumber]);

  const fetchResources = () => {
    setIsLoading(true);
    getResourcesForAllocation.mutate(resourceAllocationQuery, {
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

  const { bulkConflictCheck } = useConflictCheck();
  const confirm = useConfirm();

  const handleApprove = () => {
    const data = {
      projectId: allocationRequestQuery.projectId,
      resourceId: allocationRequestQuery.resources.map((resource) => resource.id),
      allocationId: null,
      allocationStartDate: dayjs(allocationRequestQuery.startDate).format('YYYY-MM-DD'),
      allocationEndDate: dayjs(allocationRequestQuery.endDate).format('YYYY-MM-DD'),
      allocationType: allocationRequestQuery.allocationType,
    };
    //check for conflicts
    bulkConflictCheck.mutate(data, {
      onSuccess: (data) => {
        if (data.length > 0) {
          handleConflict(data);
        } else {
          handleRequest();
        }
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
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

  const handleDeselect = (resourceToDeselect) => {
    const newSelected = selected.filter((resource) => resource.id !== resourceToDeselect.id);
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

  const handleRequest = () => {
    const transformedRequest = transformRequest(allocationRequestQuery);
    requestResource.mutate(transformedRequest, {
      onSuccess: () => {
        setClearAllOnRequest();
        setSelected([]);
        toast.success('Resource Requested Successfully');
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'An unexpected error occurred');
      },
    });
  };

  const handleProjectChange = () => {
    setSelected([]);
  };

  return (
    <div>
      <Grid container spacing={2}>
        <Grid item xs={12} md={8}>
          <Card sx={{ p: 3 }}>
            <ResourceAllocationTableToolbar
              handleRequest={handleApprove}
              handleProjectChange={handleProjectChange}
            />

            <Scrollbar>
              <TableContainer sx={{ overflow: 'unset' }}>
                <Table sx={{ minWidth: 550 }}>
                  <TableHeaders
                    isSelectable={true}
                    rowCount={users.length}
                    numSelected={selected.length}
                    headLabel={[
                      { id: 'name', label: 'Name', width: '20%' },
                      { id: 'department', label: 'Department', width: '15%' },
                      { id: 'skill', label: 'Skill', width: '40%' },
                      {
                        id: 'experience',
                        label: 'Total Experience (Years)',
                        align: 'center',
                        width: '10%',
                      },
                    ]}
                  />
                  <TableBody>
                    {isLoading ? (
                      <TableSkeleton rowsNum={5} colsNum={5} />
                    ) : (
                      resourceList?.map((row) => (
                        <ResourceAllocationTableRow
                          key={row.id}
                          employeeId={row.code}
                          name={row.name}
                          department={row.departmentName}
                          skill={row?.resourceSkillResponseDTOs}
                          experience={row?.experience}
                          selected={selected.some((item) => item.id === row.id)}
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
          <ResourceSelectedList onDeselect={handleDeselect} onApprove={handleApprove} />
        </Grid>
      </Grid>
    </div>
  );
}
