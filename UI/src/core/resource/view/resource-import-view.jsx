import React from 'react';
import { resourceImportString } from 'src/utils/constants';
import { Box, Typography } from '@mui/material';
import ResourceImport from '../resource-import';

export function ResourceImportView() {
  return (
    <div>
      <div className="mb-12">
        <Typography>
          <pre className="font-sans text-base">{resourceImportString}</pre>
        </Typography>
        <Box sx={{ paddingTop: '24px', paddingLeft: '12px' }}>
          <a
            style={{ color: '#1877f2' }}
            target="_blank"
            rel="noopener noreferrer"
            href="https://docs.google.com/spreadsheets/d/1-zirkB_9LzFQpKA-U-K-l_spN6zPe7es56aR2JUkseQ/edit?usp=sharing"
          >
            Click to see an example{' '}
          </a>
        </Box>
      </div>
      <ResourceImport />
    </div>
  );
}

export default ResourceImportView;
