export const benchedResourceFilter = [
  {
    value: 0,
    label: 'Bench',
  },
];

export const internalResourceFilter = [
  {
    value: 1,
    label: 'Internal',
  },
];

export const externalResourceFilter = [
  {
    value: 2,
    label: 'Billable',
  },
];

export function getSliceValue(selectedSlice) {
  switch (selectedSlice) {
    case 'Benched':
      return {
        value: 0,
        label: 'Bench',
      };
    case 'Internal':
      return {
        value: 1,
        label: 'Internal',
      };
    case 'Billable':
      return {
        value: 2,
        label: 'Billable',
      };
    default:
      return null;
  }
}
