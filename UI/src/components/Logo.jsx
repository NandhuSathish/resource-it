/* eslint-disable react/react-in-jsx-scope */
import PropTypes from 'prop-types';
import { Link as RouterLink } from 'react-router-dom';
// @mui
import { Box } from '@mui/material';

// ----------------------------------------------------------------------

Logo.propTypes = {
  disabledLink: PropTypes.bool,
  sx: PropTypes.object,
  size: PropTypes.oneOf(['small', 'large']),
};

export default function Logo({ disabledLink = false, sx, size = 'small' }) {
  const logo = (
    <Box
      sx={{
        width: size === 'small' ? 40 : 160,
        height: 40,
        ...sx,
      }}
    >
      <img src={`/assets/logo${size === 'small' ? '' : 'LG'}.png`} alt="logo" />
    </Box>
  );

  if (disabledLink) {
    return <>{logo}</>;
  }

  return <RouterLink to="/">{logo}</RouterLink>;
}
