/* eslint-disable react/react-in-jsx-scope */
import Box from '@mui/material/Box';
import Tab from '@mui/material/Tab';
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import { useState } from 'react';
import { Typography } from '@mui/material';
import ResourceWiseRequestView from '../resource-wise/resource-wise-request-view';
import TechnologyWiseRequestView from '../technolgy-wise/technology-wise-request-view';
import useAuth from 'src/hooks/use-auth';

const AllocationRequestView = () => {
  const [value, setValue] = useState('1');
  const handleChange = (event, newValue) => {
    setValue(newValue);
  };
  const { getUserDetails } = useAuth();
  const { role } = getUserDetails();
  return (
    <Box sx={{ width: '100%' }}>
      <TabContext value={value}>
        <Box sx={{}}>
          <TabList onChange={handleChange} aria-label="lab API tabs example">
            <Tab
              label={
                <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                  Resource-wise Requests
                </Typography>
              }
              value="1"
            />
            {role !== 3 && (
              <Tab
                label={
                  <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                    Technology-wise Requests
                  </Typography>
                }
                value="2"
              />
            )}
          </TabList>
        </Box>
        <TabPanel value="1" sx={{ paddingTop: 2, paddingLeft: 0 }}>
          <ResourceWiseRequestView />
        </TabPanel>
        {role !== 3 && (
          <TabPanel value="2" sx={{ paddingTop: 2, paddingLeft: 0 }}>
            <TechnologyWiseRequestView />
          </TabPanel>
        )}
      </TabContext>
    </Box>
  );
};

export default AllocationRequestView;
