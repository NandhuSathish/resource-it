export const experienceOptions = [
  { value: 'none', label: 'Any' },
  ...Array.from({ length: 20 }, (_, i) => ({
    value: String(i + 1),
    label: String(i + 1),
  })),
];
export const experienceOptionsForRequest = Array.from({ length: 20 }, (_, i) => ({
  value: String(i + 1),
  label: String(i + 1),
}));

export const allocationStatus = [
  { id: 2, name: 'Billable' },
  { id: 1, name: 'Internal' },
  { id: 0, name: 'Bench' },
];
export const allocationType = [
  { id: 0, name: 'Support' },
  { id: 1, name: 'Full-time' },
];

export const bands = [
  { id: 'A', name: 'A' },
  { id: 'B', name: 'B' },
  { id: 'C', name: 'C' },
];

export const expertise = [
  { id: '0', name: 'Beginner' },
  { id: '1', name: 'Intermediate' },
  { id: '2', name: 'Expert' },
];

export const projectType = [
  { id: '1', name: 'Customer Billing' },
  { id: '0', name: 'Innovature Billing' },
  { id: '2', name: 'Support' },
];

export const projectFormType = [
  { id: '1', name: 'Customer Billing' },
  { id: '0', name: 'Innovature Billing' },
];
export const projectStatus = [
  { id: '1', name: 'In Progress' },
  { id: '0', name: 'Not Started' },
  { id: '2', name: 'Completed' },
];
export const projectApprovalStatus = [
  { id: '0', name: 'Pending' },
  { id: '1', name: 'Approved' },
  { id: '2', name: 'Rejected' },
];

export const resourceRequestApprovalStatusFilter = [
  { id: '0', name: 'Pending' },
  { id: '1', name: 'Approved' },
  { id: '2', name: 'Rejected' },
];

export const resourceRequestApprovalStatus = [
  { id: '0', name: 'Pending' },
  { id: '1', name: 'Pending' },
  { id: '2', name: 'Pending' },
  { id: '3', name: 'Approved' },
  { id: '4', name: 'Rejected' },
];

export const technologyRequestApprovalStatus = [
  { id: '0', name: 'Pending' },
  { id: '3', name: 'Pending' },
  { id: '1', name: 'Approved' },
  { id: '2', name: 'Rejected' },
];

export const resourceImportString = `* Upload a valid Excel file name starting with "Employees"
 * Excel file should contain fields:
   (Employee Id, Resource Name, Department, Role, Joining Date, Email Id,
   Experience Years, Experience Months)
 * Joining Date format is "DD-MM-YYYY"
 * Possible values in department:
   (Software, Testing, Networking, HR, Admin, Sales, Finance)
 * Possible values in role:
   (HOD, HR, DIVISION HEAD, PROJECT MANAGER, RESOURCE MANAGER, RESOURCE)
`;

export const resourceSkillImportString = `* Upload a valid Excel file name starting with "Resource_Skill"
* Excel file should contain fields:
   (Email Id, Skill, Proficiency,	Experience)
* Possible values in Proficiency:
   (Beginner, Intermediate, Expert)
`;


export const CalenderValidationString = `**Upload a valid CSV file name Starting with "INV_Holidays", CSV should contain fields (Date, Day, Discription) and date format is "DD-MM-YYYY".`;
