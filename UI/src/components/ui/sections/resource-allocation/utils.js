import dayjs from 'dayjs';
import useResourceAllocationQueryStore from 'src/components/ui/stores/resourceAllocationStore';

const currentUserRole = localStorage.getItem('role');

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

export function getAllocationStartMinDate(startDate) {
  const projectStartDate = dayjs(startDate);
  const currentDate = dayjs();
  const sevenDaysFromNow = dayjs().add(7, 'day');

  //if the current user is HOD or HR, then the min date is the project start date
  if (currentUserRole === '2' || currentUserRole === '3') {
    return projectStartDate;
  }
  //if the project start date is after the current date, then the min date is the project start date
  if (projectStartDate.isAfter(currentDate) && projectStartDate.isBefore(sevenDaysFromNow)) {
    return projectStartDate;
  } else {
    //if the project start date is before the current date or after the next 7 days, then the min date is the current date
    return currentDate;
  }
}

export function getMaxDate(projectEndDate) {
  const sevenDaysFromNow = dayjs().add(7, 'day');

  let maxDate = null;
  if (currentUserRole === '2' || currentUserRole === '3') {
    maxDate = projectEndDate ? dayjs(projectEndDate) : null;
  } else {
    maxDate = sevenDaysFromNow.isAfter(projectEndDate) ? dayjs(projectEndDate) : sevenDaysFromNow;
  }

  return maxDate;
}

export function disableCheckBox() {
  const resourceAllocationQuery = useResourceAllocationQueryStore((s) => s.allocationRequestQuery);
  return (
    resourceAllocationQuery.projectId !== null &&
    resourceAllocationQuery.startDate !== null &&
    resourceAllocationQuery.endDate !== null &&
    resourceAllocationQuery.allocationType !== null
  );
}

export const transformRequest = (allocationRequestQuery) => {
  return allocationRequestQuery.resources.map((resource) => {
    const startDate = new Date(allocationRequestQuery.startDate);
    const endDate = new Date(allocationRequestQuery.endDate);

    const startDateStr = `${startDate.getFullYear()}-${String(startDate.getMonth() + 1).padStart(
      2,
      '0'
    )}-${String(startDate.getDate()).padStart(2, '0')}`;
    const endDateStr = `${endDate.getFullYear()}-${String(endDate.getMonth() + 1).padStart(
      2,
      '0'
    )}-${String(endDate.getDate()).padStart(2, '0')}`;

    return {
      projectId: allocationRequestQuery.projectId,
      resourceId: resource.id,
      allocationId: null,
      startDate: startDateStr,
      endDate: endDateStr,
    };
  });
};

export const transformTechnologyWiseRequest = (RequestQuery) => {
  const startDate = new Date(RequestQuery.startDate);
  const endDate = new Date(RequestQuery.endDate);

  const startDateStr = `${startDate.getFullYear()}-${String(startDate.getMonth() + 1).padStart(
    2,
    '0'
  )}-${String(startDate.getDate()).padStart(2, '0')}`;
  const endDateStr = `${endDate.getFullYear()}-${String(endDate.getMonth() + 1).padStart(
    2,
    '0'
  )}-${String(endDate.getDate()).padStart(2, '0')}`;
  return {
    projectId: RequestQuery.projectId,
    departmentId: RequestQuery.departmentId,
    count: RequestQuery.count,
    allocationType: RequestQuery.allocationType,
    experience: RequestQuery.experience,
    startDate: startDateStr,
    endDate: endDateStr,
    skillExperienceRequestDTO: RequestQuery.skillExperienceRequestDTO,
  };
};

export function isButtonEnabledInResource(query) {
  return (
    Object.values(query).every((value) => value !== null) &&
    query.resources &&
    query.resources.length > 0
  );
}

export function isButtonEnabled(query) {
  // Check if the startDate is after the endDate
  if (dayjs(query.startDate).isAfter(dayjs(query.endDate))) {
    return false;
  }

  // Check if all values in the query object are not null and arrays are not empty
  if (
    !Object.values(query).every((value) => {
      if (Array.isArray(value)) {
        // If the value is an array, check if it's not empty
        return value.length > 0;
      } else {
        // If the value is not an array, check if it's not null or empty string
        return value !== null && value !== '';
      }
    })
  ) {
    return false;
  }

  // Check if all items in skillExperienceRequestDTO have both a skillId and experience
  if (
    !query.skillExperienceRequestDTO.every((item) => {
      return item.skillId && item.skillMaxValue && item.skillMinValue;
    })
  ) {
    return false;
  }

  // If none of the conditions are met, return true
  return true;
}

export function getNonSelectedOptions(options, selectedValues) {
  const selectedSkillValues = selectedValues.map((item) => Number(item.skillId));
  const filteredOptions = options.filter((option) => !selectedSkillValues.includes(option.value));
  return filteredOptions;
}
