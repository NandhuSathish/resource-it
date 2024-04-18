/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Card, Table, TableBody, TableContainer, TablePagination } from '@mui/material';
import { toast } from 'sonner';
import Scrollbar from 'src/components/scrollbar';
import TableNoData from 'src/components/table/table-no-data';
import PropTypes from 'prop-types';

import useResourceHistoryQueryStore from 'src/components/ui/stores/resourceHistoryStore';

import TableHeaders from 'src/components/table/table-headers';
import TableSkeleton from 'src/components/table-skeleton';
import { useTableControls } from 'src/hooks/use-table-controls';
import ResourceHistoryTableRow from '../../resource-history-table-row';
import ResourceHistoryTableToolbar from '../../resource-history-table-toolbar';
import useResourceHistoryAnalysis from 'src/hooks/use-resource-history-analysis';
// ----------------------------------------------------------------------

export default function ResourceHistoryTableView({ resourceId }) {
  const { getResorceHistoryById } = useResourceHistoryAnalysis();

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
  } = useTableControls(useResourceHistoryQueryStore, 'asc', 'name', 'projectQuery');
  const projectQuery = useResourceHistoryQueryStore((s) => s.projectQuery);
  const [isLoading, setIsLoading] = useState(false);
  const [projectList, setProjectList] = useState([]);
  const [totalElements, setTotalElements] = useState(0);

  //when the page number changes, the page should be scrolled to the top
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page]);

  useEffect(() => {
    fetchProjects();
  }, [projectQuery]);

  const filterForTable = {
    resourceId: resourceId,
    query: projectQuery,
  };

  const fetchProjects = () => {
    setIsLoading(true);
    getResorceHistoryById.mutate(filterForTable, {
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
      <ResourceHistoryTableToolbar
        resourceId={resourceId}
        filterName={filterName}
        onFilterName={handleFilterByName}
      />
      <Scrollbar>
        <TableContainer sx={{ overflow: 'unset' }}>
          <Table sx={{ minWidth: 800 }}>
            <TableHeaders
              sortableHeads={['startDate', 'endDate']}
              order={order}
              orderBy={orderBy}
              rowCount={projectList.length}
              onRequestSort={handleSort}
              headLabel={[
                { id: 'projectCode', label: 'Project Code' },
                { id: 'projectName', label: 'Project Name' },
                { id: 'projectType', label: 'Project Type' },
                { id: 'allocationPeriod', label: 'Allocation Period', align: 'center' },
              ]}
            />
            <TableBody>
              {isLoading ? (
                <TableSkeleton rowsNum={20} colsNum={9} />
              ) : (
                projectList?.map((row) => {
                  return (
                    <ResourceHistoryTableRow
                      key={row.projectId}
                      id={row.projectId}
                      projectCode={row.projectCode}
                      projectName={row.projectName}
                      projectType={row.projectType}
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
ResourceHistoryTableView.propTypes = {
  resourceId: PropTypes.any,
};
