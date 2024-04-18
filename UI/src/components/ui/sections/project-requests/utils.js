

export const getStatusColor = (status) => {
  status = Number(status);
  let color;
  switch (status) {
    case 0:
      color = 'warning';
      break;
    case 1:
      color = 'success';
      break;
    default:
      color = 'error';
  }
  return color;
};
