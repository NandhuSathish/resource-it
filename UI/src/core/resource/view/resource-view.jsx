/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableContainer, TablePagination } from '@mui/material';

import Scrollbar from 'src/components/scrollbar';
import ResourceTableToolbar from '../resource-table-toolbar';
import ResourceTableRow from '../resource-table-row';
import TableNoData from 'src/components/table/table-no-data';
import useResources from 'src/hooks/useResources';
import useResourceQueryStore from 'src/components/ui/stores/resourceStore';
import { toast } from 'sonner';
import TableSkeleton from 'src/components/table-skeleton';
import TableHeaders from 'src/components/table/table-headers';
import { useTableControls } from 'src/hooks/use-table-controls';
// ----------------------------------------------------------------------

export default function ResourceView() {
  const resourceQuery = useResourceQueryStore((s) => s.resourceQuery);
  const { getResources } = useResources();

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
  } = useTableControls(useResourceQueryStore, 'asc', 'name', 'resourceQuery');
  const [resourceList, setResourceList] = useState([]);
  const [totalElements, setTotalElements] = useState(0);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  useEffect(() => {
    fetchResources();
  }, [resourceQuery]);

  const fetchResources = () => {
    setIsLoading(true);
    getResources.mutate(resourceQuery, {
      onSuccess: (data) => {
        setTotalElements(data.totalItems);
        setResourceList(data.items);
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

  const notFound = resourceList?.length == 0;

  return (
    <Card maxWidth="100%">
      <ResourceTableToolbar filterName={filterName} onFilterName={handleFilterByName} />
      <Scrollbar>
        <TableContainer sx={{ overflow: 'unset' }}>
          <Table sx={{ minWidth: 800 }}>
            <TableHeaders
              order={order}
              orderBy={orderBy}
              rowCount={resourceList.length}
              onRequestSort={handleSort}
              sortableHeads={['empId', 'resourceName']}
              headLabel={[
                { id: 'empId', label: 'Employee Id', width: '12%' },
                { id: 'resourceName', label: 'Name', width: '10%' },
                { id: 'department', label: 'Department', width: '10%' },
                {
                  id: 'projectName',
                  label:'Project Name',
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
                  width: '9%',
                },
                {
                  id: 'bench',
                  label: (
                    <>
                      Aging
                      <br />
                      (Days)
                    </>
                  ),
                  align: 'center',
                  width: '8%',
                },
                {
                  id: 'status',
                  label: (
                    <>
                      Allocation
                      <br />
                      Status
                    </>
                  ),
                  align: 'center',
                  width: '9%',
                },
                { id: 'actions', width: '10%' },
              ]}
            />
            <TableBody>
              {isLoading ? (
                <TableSkeleton rowsNum={20} colsNum={9} />
              ) : (
                resourceList?.map((row) => {
                  return (
                    <ResourceTableRow
                      key={row.id}
                      id={row.id}
                      empId={row.employeeId}
                      name={row.name}
                      department={row.departmentName}
                      project={row.projectName}
                      band={row.band}
                      skill={row.resourceSkillResponseDTOs}
                      exp={row.totalExperience}
                      bench={row.aging}
                      status={row.allocationStatus}
                      company={row.company}
                      avatarUrl={row.avatarUrl}
                      role={row.roleName}
                      state={row.status}
                      isVerified={row.isVerified}
                      handleFetch={fetchResources}
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
