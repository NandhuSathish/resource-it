export function emptyRows(page, rowsPerPage, arrayLength) {
  return page ? Math.max(0, (1 + page) * rowsPerPage - arrayLength) : 0;
}

export function canDelete(currentUserRole, targetUserRole) {
  if (currentUserRole === 'HOD' || currentUserRole === 'HR') {
    return true;
  } else if (targetUserRole === 'HOD' || targetUserRole === 'HR') {
    return false;
  } else {
    return true;
  }
}

export function canEditProject(currentUserRole, resourceId, managerId, isEdit, projectType) {
  if ((currentUserRole === 2 || currentUserRole === 3)  && isEdit === 0 && projectType != 2) {
    return false;
  }
    else if ((Number(resourceId) === managerId) && isEdit === 0 && projectType != 2) {
    return false;
  }
    else {
    return true;
  }
}

export const getStatusColor = (status) => {
  status = Number(status);
  let color;
  switch (status) {
    case 0:
      color = 'warning';
      break;
    case 1:
      color = 'primary';
      break;
    default:
      color = 'success';
  }
  return color;
};
