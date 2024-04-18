/* eslint-disable react/react-in-jsx-scope */
import dayjs from 'dayjs';

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

export function transformRequestForApproval(request) {
  return request.resources.map((resource) => ({
    projectId: request.projectId,
    resourceId: resource.id,
    startDate: dayjs(request.startDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
    endDate: dayjs(request.endDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
    allocationType: request.allocationType,
  }));
}

export function getResourceApprovalStatus(role, approvalStatus) {
  if (role === 2) {
    switch (approvalStatus) {
      case 0:
      case 2:
        return 'Pending';
      case 1:
      case 3:
        return 'Approved';
      case 4:
        return 'Rejected';
      default:
        return 'Unknown status';
    }
  } else if (role === 3) {
    switch (approvalStatus) {
      case 1:
        return 'Pending';
      case 2:
      case 3:
        return 'Approved';
      case 4:
        return 'Rejected';
      default:
        return 'Unknown status';
    }
  } else {
    return 'Unknown role';
  }
}

export function getTechnologyApprovalStatus(role, approvalStatus) {
  if (role === 2) {
    switch (approvalStatus) {
      case 0:
        return 'Pending';
      case 1:
      case 3:
        return 'Approved';
      case 2:
        return 'Rejected';
      default:
        return 'Unknown status';
    }
  } else if (role === 3) {
    switch (approvalStatus) {
      case 3:
        return 'Pending';
      case 1:
        return 'Approved';
      case 2:
        return 'Rejected';
      default:
        return 'Unknown status';
    }
  } else {
    return 'Unknown role';
  }
}
