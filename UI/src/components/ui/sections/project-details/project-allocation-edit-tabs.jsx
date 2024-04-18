/* eslint-disable react/prop-types */
/* eslint-disable react/react-in-jsx-scope */
import dayjs from 'dayjs';
import Box from '@mui/material/Box';
import Tab from '@mui/material/Tab';

import TabContext from '@mui/lab/TabContext';
import { Badge, Button, Container, Stack, Typography } from '@mui/material';
import { TabList, TabPanel } from '@mui/lab';
import { useState } from 'react';
import useGlobalStore from 'src/store/globalStore';
import ProjectAllocationEditView from './project-resource-edit-table/project-allocation-edit-view';
import Iconify from 'src/components/iconify/iconify';
import useResourceAllocationQueryStore from '../../stores/resourceAllocationStore';
import { useNavigate } from 'react-router-dom';
import { convertDate } from 'src/utils/utils';
import useProjectAllocationEditQueryStore from '../../stores/projectAllocationEditStore';
import { canAllocate } from './utils';
import useAuth from 'src/hooks/use-auth';
import useTechnologyWiseAllocationQueryStore from '../../stores/technologyWiseAllocationStore';

const ProjectAllocationEditTabs = (props) => {
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser, resourceId: currentResourceId } = getUserDetails();
  const globalQuery = useGlobalStore((s) => s.globalQuery);
  const setSelectedTab = useGlobalStore((s) => s.setAllocationInProjectTab);
  const navigate = useNavigate();

  //resourcewise
  const setProjectId = useResourceAllocationQueryStore((s) => s.setProjectId);
  const setProjectStartDate = useResourceAllocationQueryStore((s) => s.setProjectStartDate);
  const setProjectEndDate = useResourceAllocationQueryStore((s) => s.setProjectEndDate);
  const setAllocationStartDate = useResourceAllocationQueryStore((s) => s.setAllocationStartDate);
  const setAllocationEndDate = useResourceAllocationQueryStore((s) => s.setAllocationEndDate);

  //technologywise
  const setTechWiseProjectId = useTechnologyWiseAllocationQueryStore((s) => s.setProjectId);
  const setTechWiseProjectStartDate = useTechnologyWiseAllocationQueryStore(
    (s) => s.setProjectStartDate
  );
  const setTechWiseProjectEndDate = useTechnologyWiseAllocationQueryStore(
    (s) => s.setProjectEndDate
  );
  const setTechWiseAllocationStartDate = useTechnologyWiseAllocationQueryStore(
    (s) => s.setAllocationStartDate
  );
  const setTechWiseAllocationEndDate = useTechnologyWiseAllocationQueryStore(
    (s) => s.setAllocationEndDate
  );

  const setIsExpired = useProjectAllocationEditQueryStore((s) => s.setIsExpired);

  const today = Date();
  const startDate = dayjs(convertDate(props.projectDetails.startDate));
  const [value, setValue] = useState(globalQuery.allocationInProjectTab);
  const handleChange = (event, newValue) => {
    setValue(newValue);
    setSelectedTab(newValue);
    if (newValue === '1') {
      setIsExpired(false);
    } else if (newValue === '2') {
      setIsExpired(true);
    }
  };

  const AllocateResourceToProject = () => {
    setProjectId({ value: props.projectDetails.projectId, label: '' });

    //resourceWise
    if (startDate.isBefore(today)) {
      setAllocationStartDate(dayjs(today));
    } else {
      setAllocationStartDate(convertDate(props.projectDetails.startDate));
    }
    setAllocationEndDate(convertDate(props.projectDetails.endDate));
    setProjectStartDate(convertDate(props.projectDetails.startDate));
    setProjectEndDate(convertDate(props.projectDetails.endDate));

    //techwise

    setTechWiseProjectId({ value: props.projectDetails.projectId, label: '' });
    if (startDate.isBefore(today)) {
      setTechWiseAllocationStartDate(dayjs(today));
    } else {
      setTechWiseAllocationStartDate(convertDate(props.projectDetails.startDate));
    }
    setTechWiseAllocationEndDate(convertDate(props.projectDetails.endDate));
    setTechWiseProjectStartDate(convertDate(props.projectDetails.startDate));
    setTechWiseProjectEndDate(convertDate(props.projectDetails.endDate));

    navigate('/resourceManagement/resourceAllocations');
  };

  return (
    <Container maxWidth="100%" sx={{ marginTop: '20px' }}>
      <Stack direction="col" alignItems="center" gap={3} justifyContent="space-between" mb={5}>
        <Typography variant="h4">Project Allocations</Typography>
        <Button
          variant="contained"
          onClick={AllocateResourceToProject}
          color="inherit"
          startIcon={<Iconify icon="eva:plus-fill" />}
          className="px-6"
          disabled={
            props.projectDetails.projectType == 2
              ? false
              : canAllocate(
                  currentLoggedUser,
                  props.projectDetails.manager.id,
                  currentResourceId,
                  props.projectDetails.projectState,
                  props.projectDetails.startDate,
                  props.projectDetails.status,
                  props.projectDetails.edited
                )
          }
        >
          Allocate
        </Button>
      </Stack>

      <Box sx={{ width: '100%' }}>
        <TabContext value={value}>
          <Box sx={{}}>
            <TabList onChange={handleChange} aria-label="lab API tabs example">
              <Tab
                label={
                  <Badge badgeContent={0} color="primary">
                    <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                      Active Allocations
                    </Typography>
                  </Badge>
                }
                value="1"
              />
              <Tab
                label={
                  <Badge badgeContent={0} color="primary">
                    <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                      History
                    </Typography>
                  </Badge>
                }
                value="2"
              />
            </TabList>
          </Box>
          <TabPanel value="1" sx={{ paddingTop: 2, paddingLeft: 0 }}>
            {/* active.  */}
            <ProjectAllocationEditView
              projectDetails={props.projectDetails}
              fetchProjectDetails={props.handleUpdateProject}
              allocationData={props.allocationData}
              flag={1}
            />
          </TabPanel>
          <TabPanel value="2" sx={{ paddingTop: 2, paddingLeft: 0 }}>
            {/* inactive.  */}
            <ProjectAllocationEditView
              projectDetails={props.projectDetails}
              fetchProjectDetails={props.handleUpdateProject}
              allocationData={props.allocationData}
              flag={0}
            />
          </TabPanel>
        </TabContext>
      </Box>
    </Container>
  );
};

export default ProjectAllocationEditTabs;
