export function canAllocate(
  currentUserRole,
  managerId,
  resourceId,
  status,
  projectStartDate,
  projectStatus,
  projectEditStatus
) {
  const today = new Date();
  const [month, day, year] = projectStartDate.split('-');
  const startDate = new Date(year, month - 1, day); // months are 0-indexed in JavaScript Date
  const differenceInTime = today.getTime() - startDate.getTime();
  const differenceInDays = differenceInTime / (1000 * 3600 * 24);
  if (projectStatus != 1) {
    return true;
  }

  if (projectEditStatus == 1) {
    return true;
  } else if (
    (currentUserRole === 2 || currentUserRole === 3 || currentUserRole === 4) &&
    (status === 1 || status === 0)
  ) {
    return false;
  } else if (Number(resourceId) === managerId && (status === 1 || status === 0)) {
    return false;
  } else if ((differenceInDays > 7 && currentUserRole !== 2) || currentUserRole !== 3) {
    return true;
  } else {
    return true;
  }
}


export function canEditFromProject(currentUserRole, resourceId, managerId, isEdit, projectType) {
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