import React from 'react';
import { Toolbar, Box } from '@mui/material';

import ProjectRequestFilters from './project-request-filter';
// ----------------------------------------------------------------------

export default function ProjectRequestTableToolbar() {
  return (
    <Toolbar
      sx={{
        height: 96,
        display: 'flex',
        justifyContent: 'end',
      }}
    >
      <Box
        sx={{
          height: 96,
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <ProjectRequestFilters />
      </Box>
    </Toolbar>
  );
}
