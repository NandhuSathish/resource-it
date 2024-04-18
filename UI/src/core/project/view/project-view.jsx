/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableContainer, TablePagination } from '@mui/material';
import { toast } from 'sonner';
import Scrollbar from 'src/components/scrollbar';
import TableNoData from 'src/components/table/table-no-data';
import useProjects from 'src/hooks/use-projects';
import useProjectQueryStore from 'src/components/ui/stores/projectStore';
import ProjectTableToolbar from '../project-table-toolbar';
import ProjectTableRow from '../project-table-row';
import TableHeaders from 'src/components/table/table-headers';
import TableSkeleton from 'src/components/table-skeleton';
import { useTableControls } from 'src/hooks/use-table-controls';
// ----------------------------------------------------------------------

export default function ProjectView() {
  const { getProjects } = useProjects();
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
  } = useTableControls(useProjectQueryStore, 'asc', 'name', 'projectQuery');
  const projectQuery = useProjectQueryStore((s) => s.projectQuery);
  const reloadQuery = useProjectQueryStore((s) => s.projectReload.reloadStatus);
  const [isLoading, setIsLoading] = useState(false);
  const [projectList, setProjectList] = useState([]);
  const [totalElements, setTotalElements] = useState(0);

  //when the page number changes, the page should be scrolled to the top
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  useEffect(() => {
    fetchProjects();
  }, [projectQuery, reloadQuery]);

  const fetchProjects = () => {
    setIsLoading(true);
    getProjects.mutate(projectQuery, {
      onSuccess: (data) => {
        setTotalElements(data.totalItems);
        setProjectList(data.items);
        setIsLoading(false);
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

  const notFound = projectList?.length == 0;

  return (
    <Card maxWidth="100%">
      <ProjectTableToolbar filterName={filterName} onFilterName={handleFilterByName} />
      <Scrollbar>
        <TableContainer sx={{ overflow: 'unset' }}>
          <Table sx={{ minWidth: 800 }}>
            <TableHeaders
              sortableHeads={['projectCode', 'projectName', 'startDate', 'endDate', 'teamSize']}
              order={order}
              orderBy={orderBy}
              rowCount={projectList.length}
              onRequestSort={handleSort}
              headLabel={[
                { id: 'projectCode', label: 'Project Code', width: '15%' },
                { id: 'projectName', label: 'Project Name', width: '17%' },
                { id: 'projectType', label: 'Project Type', width: '8%' },
                { id: 'projectManager', label: 'Project Manager', width: '10%' },
                { id: 'startDate', label: 'Start Date', width: '10%' },
                { id: 'endDate', label: 'End Date', width: '10%' },
                { id: 'teamSize', label: 'Team Size', align: 'center', width: '7%' },
                { id: 'status', label: 'Project Status', align: 'center', width: '10%' },
                { id: '', width: '13%' },
              ]}
            />
            <TableBody>
              {isLoading ? (
                <TableSkeleton rowsNum={20} colsNum={9} />
              ) : (
                projectList?.map((row) => {
                  return (
                    <ProjectTableRow
                      key={row.projectId}
                      id={row.projectId}
                      projectCode={row.projectCode}
                      projectName={row.name}
                      projectType={row.projectType}
                      projectManager={row?.manager?.name}
                      projectManagerId={row?.manager?.id}
                      startDate={row.startDate}
                      endDate={row.endDate}
                      teamSize={row.teamSize}
                      projectStatus={row.projectState}
                      isEdited={row.edited}
                      fetchProjects={fetchProjects}
                    />
                  );
                })
              )}
              {notFound && <TableNoData query={filterName} />}
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
