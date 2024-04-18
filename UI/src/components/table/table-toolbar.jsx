/* eslint-disable react/react-in-jsx-scope */
import PropTypes from 'prop-types';
import { Toolbar } from '@mui/material';

// ----------------------------------------------------------------------

export default function TableToolbar({ children, styles }) {
  return (
    <Toolbar
      sx={{
        height: 96,
        display: 'flex',
        justifyContent: 'end',
        ...styles,
      }}
    >
      {children}
    </Toolbar>
  );
}
TableToolbar.propTypes = {
  children: PropTypes.node.isRequired,
  styles: PropTypes.object,
};
