/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Menu, Button, MenuItem, Typography, listClasses } from '@mui/material';

import Iconify from 'src/components/iconify';
import useResourceQueryStore from 'src/components/ui/stores/resourceStore';
// ----------------------------------------------------------------------

const SORT_OPTIONS = [
  { value: '1', label: 'Active' },
  { value: '0', label: 'Inactive' },
];

export default function ResourceState() {
  const [open, setOpen] = useState(null);
  const resourceQuery = useResourceQueryStore((s) => s.resourceQuery);
  const setStatus = useResourceQueryStore((s) => s.setStatus);
  useEffect(() => {
    setStatus(1);
  }, []);

  const handleOpen = (event) => {
    setOpen(event.currentTarget);
  };

  const handleClose = () => {
    setOpen(null);
  };

  const handleSelect = (value) => {
    setStatus(value);
    handleClose();
  };
  const currentLabel =
    SORT_OPTIONS.find((option) => option.value === resourceQuery.status)?.label || 'Active';

  return (
    <div className="ml-2">
      <Button
        sx={{ width: 100 }}
        disableRipple
        color="inherit"
        onClick={handleOpen}
        endIcon={<Iconify icon={open ? 'eva:chevron-up-fill' : 'eva:chevron-down-fill'} />}
      >
        <Typography component="span" variant="subtitle2" sx={{ color: 'text.secondary' }}>
          {currentLabel}
        </Typography>
      </Button>

      <Menu
        open={!!open}
        anchorEl={open}
        onClose={handleClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
        slotProps={{
          paper: {
            sx: {
              [`& .${listClasses.root}`]: {
                p: 0,
              },
            },
          },
        }}
      >
        {SORT_OPTIONS.map((option) => (
          <MenuItem
            key={option.value}
            selected={option.value === resourceQuery.status}
            onClick={() => handleSelect(option.value)}
          >
            {option.label}
          </MenuItem>
        ))}
      </Menu>
    </div>
  );
}
