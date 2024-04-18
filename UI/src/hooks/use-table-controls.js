import { useEffect, useState } from 'react';

export const useTableControls = (queryStore, initialOrder, initialOrderBy, queryName) => {
  const query = queryStore((s) => s[queryName]);
  const setPageNumber = queryStore((s) => s.setPageNumber);
  const setPageSize = queryStore((s) => s.setPageSize);
  const setSortOrder = queryStore((s) => s.setSortOrder);
  const setSortKey = queryStore((s) => s.setSortKey);
  const setSearchText = queryStore((s) => s.setSearchText);
  const [page, setPage] = useState(query.pageNumber);
  const [order, setOrder] = useState(initialOrder);
  const [orderBy, setOrderBy] = useState(initialOrderBy);
  const [filterName, setFilterName] = useState(query.name);
  const [rowsPerPage, setRowsPerPage] = useState(query.pageSize);

  useEffect(() => {
    setPage(query.pageNumber);
  }, [query.pageNumber]);

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

  const handleFilterByName = (event) => {
    setPage(0);
    setFilterName(event.target.value);
    setSearchText(event.target.value.trim());
  };

  return {
    page,
    order,
    orderBy,
    filterName,
    rowsPerPage,
    handleSort,
    handleChangePage,
    handleChangeRowsPerPage,
    handleFilterByName,
  };
};
