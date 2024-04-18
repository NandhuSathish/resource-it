/* eslint-disable react/react-in-jsx-scope */
import PropTypes from 'prop-types';
import { Button, Drawer, Stack, Typography, IconButton, Divider } from '@mui/material';
import { Icon as Iconify } from '@iconify/react';

export function FilterDrawer({
  openFilter,
  handleOpenFilter,
  handleCloseFilter,
  title,
  icon,
  children,
}) {
  return (
    <>
      <Button
        data-testid="filters-button"
        disableRipple
        color="inherit"
        endIcon={<Iconify icon={icon} />}
        onClick={handleOpenFilter}
      >
        <Typography variant="subtitle2">{title}&nbsp;</Typography>
      </Button>

      <Drawer
        anchor="right"
        open={openFilter}
        onClose={handleCloseFilter}
        PaperProps={{
          sx: { width: 360, border: 'none', overflow: 'auto' },
        }}
      >
        <Stack
          direction="row"
          alignItems="center"
          justifyContent="space-between"
          sx={{ px: 1, py: 2 }}
        >
          <Typography variant="h5" sx={{ ml: 1 }}>
            {title}
          </Typography>
          <IconButton onClick={handleCloseFilter}>
            <Iconify icon="eva:close-fill" />
          </IconButton>
        </Stack>
        <Divider />
        {children} {/* Render the children here */}
      </Drawer>
    </>
  );
}
FilterDrawer.propTypes = {
  openFilter: PropTypes.bool.isRequired,
  handleOpenFilter: PropTypes.func.isRequired,
  handleCloseFilter: PropTypes.func.isRequired,
  title: PropTypes.string.isRequired,
  icon: PropTypes.string.isRequired,
  children: PropTypes.node, // node includes any renderable React child, including strings and numbers
};

FilterDrawer.defaultProps = {
  children: null, // default value if children is not provided
};
