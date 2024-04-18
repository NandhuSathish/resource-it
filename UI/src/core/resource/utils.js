export function emptyRows(page, rowsPerPage, arrayLength) {
  return page ? Math.max(0, (1 + page) * rowsPerPage - arrayLength) : 0;
}
export function canDelete(currentUserRole, targetUserRole, state) {
  if (state !== 'ACTIVE') {
    return false;
  }
  if (currentUserRole.role === 1) {
    return true;
  } else if (
    (currentUserRole.role === 2 || currentUserRole.role === 3 || currentUserRole.role === 6) &&
    !(targetUserRole === 'HOD' || targetUserRole === 'RESOURCE MANAGER' || targetUserRole === 'HR')
  ) {
    return true;
  } else if (
    currentUserRole.role !== 1 &&
    (targetUserRole === 'HOD' || targetUserRole === 'HR')
  ) 
  {
    return false;
  }
   else if (
    currentUserRole.role == 2 || currentUserRole.role == 3 &&
    (targetUserRole === 'RESOURCE MANAGER')
  ) 
  {
    return true;
  }
  
  
  else if (
    currentUserRole.role === 4 ||
    currentUserRole.role === 5 ||
    currentUserRole.role === 7
  ) {
    return false;
  } else {
    return true;
  }
}


export function canEdit(currentUserRole, state) {
  if (state !== 'ACTIVE') {
    return false;
  } else if (
    currentUserRole.role === 1 ||
    currentUserRole.role === 2 ||
    currentUserRole.role === 3 ||
    currentUserRole.role === 6
  ) {
    return true;
  } else {
    return false;
  }
}