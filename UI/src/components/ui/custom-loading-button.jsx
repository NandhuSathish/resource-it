import React from 'react';
import PropTypes from 'prop-types';
import { LoadingButton } from '@mui/lab';
import Iconify from 'src/components/iconify';

const CustomLoadingButton = ({ isLoading, icon, onClick, children, loadingPosition, variant }) => (
  <LoadingButton
    loading={isLoading}
    loadingPosition={loadingPosition}
    sx={{ marginRight: 2 }}
    variant={variant}
    color="inherit"
    startIcon={<Iconify icon={icon} />}
    onClick={onClick}
  >
    {children}
  </LoadingButton>
);

CustomLoadingButton.propTypes = {
  isLoading: PropTypes.bool,
  icon: PropTypes.string,
  onClick: PropTypes.func.isRequired,
  children: PropTypes.node.isRequired,
  loadingPosition: PropTypes.string,
  variant: PropTypes.string,
};

CustomLoadingButton.defaultProps = {
  loadingPosition: 'start',
  variant: 'contained',
};

export default CustomLoadingButton;
