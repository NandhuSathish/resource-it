/* eslint-disable react/react-in-jsx-scope */

import { Badge, Box, Tab, Typography } from '@mui/material';
import { TabList, TabPanel, TabContext } from '@mui/lab';
import { useState } from 'react';
import useGlobalStore from 'src/store/globalStore';
import ResourceWiseApprovalView from '../resource-wise/resource-wise-approval-view';
import TechnologyWiseApprovalView from '../technolgy-wise/technology-wise-approval-view';

const AllocationApprovalView = () => {
  const globalQuery = useGlobalStore((s) => s.globalQuery);
  const setAllocationApprovalSelectedTab = useGlobalStore(
    (s) => s.setAllocationApprovalSelectedTab
  );
  const [value, setValue] = useState(globalQuery.allocationApprovalSelectedTab);
  const handleChange = (event, newValue) => {
    setValue(newValue);
    setAllocationApprovalSelectedTab(newValue);
  };
  return (
    <Box sx={{ width: '100%' }}>
      <TabContext value={value}>
        <Box sx={{}}>
          <TabList onChange={handleChange} aria-label="lab API tabs example">
            <Tab
              label={
                <Badge badgeContent={0} color="primary">
                  <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                    Resource-wise Approvals
                  </Typography>
                </Badge>
              }
              value="1"
            />
            <Tab
              label={
                <Badge badgeContent={0} color="primary">
                  <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                    Technology-wise Approvals
                  </Typography>
                </Badge>
              }
              value="2"
            />
          </TabList>
        </Box>
        <TabPanel value="1" sx={{ paddingTop: 2, paddingLeft: 0 }}>
          <ResourceWiseApprovalView />
        </TabPanel>
        <TabPanel value="2" sx={{ paddingTop: 2, paddingLeft: 0 }}>
          <TechnologyWiseApprovalView />
        </TabPanel>
      </TabContext>
    </Box>
  );
};

export default AllocationApprovalView;
