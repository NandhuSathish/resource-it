import { Card, Typography } from '@mui/material';
import PropTypes from 'prop-types';
import { getResourceAllocationType, convertMonthsToYears } from 'src/utils/utils';  
import React from 'react';

const ResourceDetailsContents = ({resourceData}) => {
      const cardStyle = {
        width: '100%',
        minHeight: 150,
        padding:'4%',
        paddingTop: '15px',
        paddingLeft: '20px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'start',
      };


  localStorage.setItem('resourceAnalysisFilename', resourceData.name);
  return (
    <div className=" w-full" style={{ whiteSpace: 'nowrap' }}>
      {' '}
      <Card sx={cardStyle}>
        <div className="pr-20 pt-6 pb-6 pl-6">
          {/*Name */}
          <div>
            <Typography variant="h4"> {resourceData.name} </Typography>
          </div>

          {/*emploeeId */}
          <div className="mt-4">
            <Typography>Employee Id: {resourceData.employeeId} </Typography>
          </div>

          {/*Status */}
          <div className="mt-4">
            <Typography> Status : {resourceData.status == 1 ? 'Active' : 'Inactive'} </Typography>
          </div>

          {/*department */}
          <div className="mt-4">
            <Typography>Department: {resourceData.departmentName} </Typography>
          </div>

          {/*role */}
          <div className="mt-4">
            <Typography>Role: {resourceData.role} </Typography>
          </div>

          {/*allocationStatus */}
          <div className="mt-4 ">
            <Typography>
              Allocation Status: {getResourceAllocationType(resourceData.allocationStatus)}{' '}
            </Typography>
          </div>

          {/*joiningDate */}
          <div className="mt-4">
            <Typography>Joining Date: {resourceData.joiningDate} </Typography>
          </div>

          {/*billableDays */}
          <div className="mt-4">
            <Typography>Billable Days: {resourceData.billableDays} </Typography>
          </div>

          {/*benchDays */}
          <div className="mt-4">
            <Typography>Bench Days: {resourceData.benchDays} </Typography>
          </div>

          {/*workSpan */}
          <div className="mt-4">
            <Typography>Work span: {convertMonthsToYears(resourceData.workSpan)} Years </Typography>
          </div>

          {/*TotalExp */}
          <div className="mt-4">
            <Typography>Total Experience: {resourceData.totalExperience} Years </Typography>
          </div>
        </div>
      </Card>
    </div>
  );
}

export default ResourceDetailsContents;

ResourceDetailsContents.propTypes = {
  resourceData: PropTypes.object.isRequired,
};