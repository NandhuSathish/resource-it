/* eslint-disable react/react-in-jsx-scope */
import { Typography, Box, Tab } from '@mui/material';
import { TabContext, TabList, TabPanel } from '@mui/lab';
import { useState } from 'react';
import useGlobalStore from 'src/store/globalStore';
import ResourceWiseAllocationView from '../resource-wise/resource-wise-allocation-view';
import TechnologyWiseView from '../technolgy-wise/technology-wise-allocation-view';
import useAuth from 'src/hooks/use-auth';

const ResourceAllocationView = () => {
  const { getUserDetails } = useAuth();
  const { role } = getUserDetails();
  const globalQuery = useGlobalStore((s) => s.globalQuery);
  const setAllocationSelectedTab = useGlobalStore((s) => s.setAllocationSelectedTab);
  const [value, setValue] = useState(globalQuery.allocationSelectedTab);
  const handleChange = (event, newValue) => {
    setValue(newValue);
    setAllocationSelectedTab(newValue);
  };
  return (
    <Box sx={{ width: '100%' }}>
      <TabContext value={value}>
        <Box>
          <TabList onChange={handleChange} aria-label="lab API tabs example">
            <Tab
              label={
                <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                  Resource-wise Allocation
                </Typography>
              }
              value="1"
            />
            {role !== 3 && (
              <Tab
                label={
                  <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                    Technology-wise Allocation
                  </Typography>
                }
                value="2"
              />
            )}
          </TabList>
        </Box>
        <TabPanel value="1" sx={{ paddingTop: 2, paddingLeft: 0, paddingRight: 0 }}>
          <ResourceWiseAllocationView />
        </TabPanel>
        {role !== 3 && (
          <TabPanel value="2" sx={{ paddingTop: 2, paddingLeft: 0, paddingRight: 0 }}>
            <TechnologyWiseView />
          </TabPanel>
        )}
      </TabContext>
    </Box>
  );
};

export default ResourceAllocationView;
