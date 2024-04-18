export const visuallyHidden = {
  border: 0,
  margin: -1,
  padding: 0,
  width: '1px',
  height: '1px',
  overflow: 'hidden',
  position: 'absolute',
  whiteSpace: 'nowrap',
  clip: 'rect(0 0 0 0)',
};
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
export const getStatusColor = (status) => {
  status = Number(status);
  let color;
  switch (status) {
    case 0:
      color = 'warning';
      break;
    case 1:
      color = 'warning';
      break;
    case 2:
      color = 'warning';
      break;
    case 3:
      color = 'success';
      break;
    default:
      color = 'error';
  }
  return color;
};

export function getApprovalColor(approvalStatus) {
  switch (approvalStatus) {
    case 'Pending':
      return 'warning';
    case 'Approved':
      return 'success';
    case 'Rejected':
      return 'error';
    default:
      return 'warning';
  }
}

export function isDeleteButtonEnabled(role, status) {
  return (
    (role === 2 && status === 1) || (role === 3 && status === 2) || (role === 5 && status === 0)
  );
}

export function getApprovalStatusOfResourceRequestList(role, approvalStatus) {
  if (role === 2) {
    switch (approvalStatus) {
      case 1:
        return 'Pending';
      case 3:
        return 'Approved';
      case 4:
        return 'Rejected';
      default:
        return 'Unknown status';
    }
  } else if (role === 3) {
    switch (approvalStatus) {
      case 2:
        return 'Pending';
      case 3:
        return 'Approved';
      case 4:
        return 'Rejected';
      default:
        return 'Unknown status';
    }
  } else if (role === 5 || role === 4) {
    switch (approvalStatus) {
      case 0:
      case 1:
        return 'Pending';
      case 3:
        return 'Approved';
      case 4:
        return 'Rejected';
      default:
        return 'Unknown ';
    }
  } else {
    return 'Unknown ';
  }
}

export function getApprovalStatusOfTechnologyWiseRequestList(role, approvalStatus) {
  if (role === 2) {
    switch (approvalStatus) {
      case 3:
        return 'Pending';
      case 1:
        return 'Approved';
      case 2:
        return 'Rejected';
      default:
        return 'Unknown ';
    }
  } else if (role === 3 || role === 5 || role === 4) {
    switch (approvalStatus) {
      case 0:
      case 3:
        return 'Pending';
      case 1:
        return 'Approved';
      case 2:
        return 'Rejected';
      default:
        return 'Unknown ';
    }
  } else {
    return 'Unknown ';
  }
}
