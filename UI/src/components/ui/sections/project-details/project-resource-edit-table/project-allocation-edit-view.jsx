/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableContainer, TablePagination } from '@mui/material';
import Scrollbar from 'src/components/scrollbar';
import TableNoData from 'src/components/table/table-no-data';
import TableHeaders from 'src/components/table/table-headers';
import useProjects from 'src/hooks/use-projects';
import ProjectAllocationRequestTableRow from './project-allocation-edit-table-row';
import useProjectAllocationEditQueryStore from 'src/components/ui/stores/projectAllocationEditStore';

// ---------------------------------------------------------------------------------------------------

export default function ProjectAllocationEditView(allocationData) {
  const { getProjectAllocations } = useProjects();
  const projectAllocationEditQuery = useProjectAllocationEditQueryStore(
    (s) => s.projectAllocationEditQuery
  );
  const setPageNumber = useProjectAllocationEditQueryStore((s) => s.setPageNumber);
  const setPageSize = useProjectAllocationEditQueryStore((s) => s.setPageSize);
  const [page, setPage] = useState(projectAllocationEditQuery.page);

  const [projectAllocationList, setProjectRequestList] = useState([]);
  const [rowsPerPage, setRowsPerPage] = useState(projectAllocationEditQuery.size);
  const [totalElements, setTotalElements] = useState(0);

  useEffect(() => {
    setPage(projectAllocationEditQuery.page);
  }, [projectAllocationEditQuery.page]);

  useEffect(() => {
     fetchRequest();
  }, [projectAllocationEditQuery]);
  const content = {
    query: projectAllocationEditQuery,
    id: allocationData.allocationData,
  };

  const fetchRequest = () => {
    getProjectAllocations.mutate(content, {
      onSuccess: (projectData) => {
        setTotalElements(projectData.totalItems);
        setProjectRequestList(projectData.items);
      },
    });
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

  const notFound = projectAllocationList?.length == 0;

  return (
    <div>
      <Card>
        <Scrollbar>
          <TableContainer sx={{ overflow: 'unset' }}>
            <Table>
              <TableHeaders
                rowCount={projectAllocationList.length}
                sortableHeads={[]}
                isSelectable={false}
                headLabel={[
                  { id: 'employeeId', label: 'Employee Id' },
                  { id: 'resourceName', label: 'Resource Name' },
                  { id: 'departmentName', label: 'Department' },
                  { id: 'requestedBy', label: 'Requested By' },
                  { id: 'startDate', label: 'Start Date' },
                  { id: 'endDate', label: 'End Date' },
                  allocationData.flag == 1 && {
                    id: 'remainingWorkingDays',
                    label: 'Remaining Working Days',
                    align: 'center',
                  },
                  { id: '' },
                ]}
              />
              <TableBody>
                {projectAllocationList?.map((row) => {
                  return (
                    <ProjectAllocationRequestTableRow
                      key={row.id}
                      id={row.id}
                      resourceId={row.resourceId}
                      projectId={row.projectId}
                      employeeId={row.employeeId}
                      requestedBy={row.requestedBy.name}
                      allocationId={row.allocationId}
                      startDate={row.startDate}
                      endDate={row.endDate}
                      remainingWorkingDays={row.remainingWorkingDays}
                      estimatedDays={row.manDay}
                      resourceName={row.resourceName}
                      departmentName={row.departmentName}
                      band={row.band}
                      isEdited={row.isEdited}
                      isRemoved={row.isRemoved}
                      role={row.roleId}
                      projectStartDate={allocationData.projectDetails.startDate}
                      projectEndDate={allocationData.projectDetails.endDate}
                      projectManagerId={allocationData.projectDetails.manager.id}
                      projectType={allocationData.projectDetails.projectType}
                      handleFetch={fetchRequest}
                      handleDetailsUpdate={allocationData.fetchProjectDetails}
                      tabMode={allocationData.flag}
                    />
                  );
                })}
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
          rowsPerPageOptions={[5, 10, 20, 25, 50]}
          onRowsPerPageChange={handleChangeRowsPerPage}
          showFirstButton={true}
          showLastButton={true}
        />
      </Card>
    </div>
  );
}
