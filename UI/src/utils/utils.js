import dayjs from 'dayjs';
import { differenceInDays, isAfter } from 'date-fns';

import {
  allocationStatus,
  projectApprovalStatus,
  projectStatus,
  projectType,
  resourceRequestApprovalStatus,
  technologyRequestApprovalStatus,
} from './constants';

export function mapToOptions(data, valueField, labelField, appendField) {
  return data.map((item) => ({
    value: item[valueField],
    label: appendField ? `${item[appendField]} (${item[labelField]})` : item[labelField],
  }));
}

export function mapManagerToOptions(data, valueField, labelField, currentUserId) {
  let options = [];
  data.forEach((item) => {
    const option = {
      value: item[valueField],
      label: item[valueField] == currentUserId ? `${item[labelField]} (You)` : item[labelField],
    };
    if (item[valueField] == currentUserId) {
      options.unshift(option);
    } else {
      options.push(option);
    }
  });
  return options;
}

export function toTitleCase(str) {
  return str.replace(/\w\S*/g, function (txt) {
    return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
  });
}

export function getPercentage(value, total) {
  if (!value || !total) {
    return 0;
  }
  return (value / total) * 100;
}
export function mapToSrtingOptions(data, valueField, labelField) {
  return data?.map((item) => ({
    value: item[valueField].toString(),
    label: item[labelField],
  }));
}
export function getSkillIds(skills) {
  return skills.map((skill) => skill.id.toString());
}

export function convertDate(dateString) {
  const parts = typeof dateString === 'string' ? dateString.split('-') : ['01', '01', '1970'];

  const date = new Date(parts[2], parts[1] - 1, parts[0]);
  return dayjs(date);
}

export const stringifyNumbers = (resourceData) => {
  const stringifiedObject = JSON.parse(
    JSON.stringify(resourceData, (key, value) =>
      typeof value === 'number' ? value.toString() : value
    )
  );

  return stringifiedObject;
};

export function convertMonthsToYears(months) {
  if (months < 0) {
    // Handle negative input if needed
    return '0';
  }

  // Calculate the years and remaining months
  const years = Math.floor(months / 12);
  const remainingMonths = months % 12;

  // Return the result as a string in the format "x.y"
  return `${years}.${remainingMonths}`;
}

const levels = {
  0: 'Beginner',
  1: 'Intermediate',
  2: 'Expert',
};

export function getProficency(proficiency) {
  const proficiencyMap = {
    0: 'Beginner',
    1: 'Intermediate',
    2: 'Expert',
  };
  return proficiencyMap[proficiency];
}

export function getProjectType(projectType) {
  const projectTypeMap = {
    0: 'Innovature Billing',
    1: 'Customer Billing',
    2: 'Support',
    3: 'Bench',
  };
  return projectTypeMap[projectType];
}

export function getResourceAllocationType(allocationType) {
  const allocationTypeMap = {
    0: 'Bench',
    1: 'Internal',
    2: 'Billable',
  };

  return allocationTypeMap[allocationType];
}

export function getRole(role) {
  const roleMap = {
    1: 'Admin',
    2: 'HOD',
    3: 'HR',
    4: 'DIVISION HEAD',
    5: 'PROJECT MANAGER',
    6: 'RESOURCE MANAGER',
    7: 'RESOURCE',
  };

  return roleMap[role];
}
export function getStatus(status) {
  const statusMap = {
    0: 'Not Started',
    1: 'In Progress',
    2: 'Completed',
  };

  return statusMap[status];
}

export function getCompletionPercentage(startDate, endDate) {
  // If startDate or endDate is not a string or is undefined or empty, return 0
  if (typeof startDate !== 'string' || !startDate || typeof endDate !== 'string' || !endDate) {
    return 0;
  }
  // Convert dates from 'dd-mm-yyyy' to 'yyyy-mm-dd'
  const [startDay, startMonth, startYear] = startDate.split('-');
  const [endDay, endMonth, endYear] = endDate.split('-');
  const start = new Date(`${startYear}-${startMonth}-${startDay}`);
  const end = new Date(`${endYear}-${endMonth}-${endDay}`);

  const now = new Date();

  const totalDuration = differenceInDays(end, start);

  if (isAfter(now, end)) {
    return 100; // Project has already ended
  } else if (isAfter(start, now)) {
    return 0; // Project hasn't started yet
  } else {
    const elapsedDuration = differenceInDays(now, start);
    return (elapsedDuration / totalDuration) * 100;
  }
}

export function getProjectDuration(startDate, endDate) {
  // Convert dates from 'dd-mm-yyyy' to 'yyyy-mm-dd'
  const [startDay, startMonth, startYear] = startDate.split('-');
  const [endDay, endMonth, endYear] = endDate.split('-');
  const start = new Date(`${startYear}-${startMonth}-${startDay}`);
  const end = new Date(`${endYear}-${endMonth}-${endDay}`);

  const now = new Date();

  const totalDuration = differenceInDays(end, start);
  const elapsedDuration = differenceInDays(now, start);
  const remainingDuration = differenceInDays(end, now) + 1;
  return {
    totalDays: totalDuration,
    completedDays: Math.max(0, elapsedDuration),
    remainingDays: Math.max(0, remainingDuration),
  };
}

export function mapLevelIdToLevel(levelId) {
  return levels[levelId];
}
//return status corresponding to id
export function mapAllocationStatusIdToStatus(id) {
  const status = allocationStatus.find((status) => status.id == id);
  return status ? status.name : null;
}

export function mapProjectStatusIdToStatus(id) {
  const status = projectStatus.find((status) => status.id == id);
  return status ? status.name : null;
}

export function mapProjectRequestStatusIdToStatus(id) {
  const status = projectApprovalStatus.find((status) => status.id == id);
  return status ? status.name : null;
}
export function mapResourceRequestStatusIdToStatus(id) {
  const status = resourceRequestApprovalStatus.find((status) => status.id == id);
  return status ? status.name : null;
}
export function mapTechnologyRequestStatusIdToStatus(id) {
  const status = technologyRequestApprovalStatus.find((status) => status.id == id);
  return status ? status.name : null;
}

export function mapProjectTypeIdToType(id) {
  const type = projectType.find((type) => type.id == id);
  return type ? type.name : null;
}

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

export function formatDateString(dateString) {
  const date = dayjs(dateString);
  return date.format('DD-MM-YYYY');
}

export function delay(fn, ms) {
  setTimeout(fn, ms);
}

export function formatDate(date, format) {
  console.log(format, 'format');
  const parsedDate = dayjs(date, 'MM-DD-YYYY');
  return parsedDate.isValid() ? parsedDate.format('DD-MM-YYYY') : null;
}

export const allocationType = [
  { id: '0', name: 'Support' },
  { id: '1', name: 'Full-time' },
];
export function mapAllocationTypeId(id) {
  const status = allocationType.find((status) => status.id == id);
  return status ? status.name : null;
}

export function filterSelectedSkills(options, selectedValues) {
  // Extract the skill values from the selectedValues array
  const selectedSkillValues = selectedValues.map((item) => item.skill);
  // Filter the options array to exclude those options whose value property is found in the selectedSkillValues array
  const filteredOptions = options.filter((option) => !selectedSkillValues.includes(option.value));
  return filteredOptions;
}

export function monthToYear(month) {
  let year = Math.floor(month / 12);
  let months = month % 12;

  return `${year}.${months}`;
}
