import { Box, Button, Card, CardContent, Grid, Tooltip } from '@mui/material';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import React from 'react';
import PropTypes from 'prop-types';
import { getProjectType } from 'src/utils/utils';
import { useNavigate } from 'react-router-dom';
import useAuth from 'src/hooks/use-auth';
import { canEditFromProject } from './utils';

const ProjectDetailsContent = ({ projectDetails }) => {
    const navigate = useNavigate();
      const { getUserDetails } = useAuth();
      const { role, resourceId } = getUserDetails();
      

  const handleEditNavigation = () => {
            navigate('/projectManagement/projectList/editProject', {
              state: {
                project: projectDetails.projectDetails,
                id: projectDetails.projectDetails.projectId,
              },
            });
  };

  return (
    <Grid container spacing={3}>
      <Grid item xs={12} sm={6} md={6}>
        <Card
          component={Stack}
          spacing={3}
          direction="row"
          sx={{
            px: 3,
            py: 3,
            borderRadius: 2,
          }}
        >
          <CardContent>
            <Box sx={{ display: 'flex' }}>
              <Typography variant="h4">
                {projectDetails.projectDetails.teamSize}
                <Typography variant="body1" component="span">
                  {' Resources'}
                </Typography>
              </Typography>
            </Box>
            <Typography variant="body2" color="text.secondary">
              Team Size
            </Typography>
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12} sm={6} md={6}>
        <Card
          component={Stack}
          spacing={3}
          direction="row"
          sx={{
            px: 3,
            py: 3,
            borderRadius: 2,
          }}
        >
          <CardContent>
            <Box sx={{ display: 'flex' }}>
              <Typography variant="h4">
                {projectDetails.projectDetails.manDay ? projectDetails.projectDetails.manDay : ''}
                <Typography variant="body1" component="span">
                  {projectDetails.projectDetails.manDay ? ' Days' : 'Nil'}
                </Typography>
              </Typography>
            </Box>
            <Typography variant="body2" color="text.secondary">
              Estimated Man Days
            </Typography>
          </CardContent>
        </Card>
      </Grid>
      <Grid item xs={12}>
        <Card
          component={Stack}
          spacing={3}
          direction="Column"
          sx={{
            px: 3,
            py: 5,
            borderRadius: 2,
          }}
        >
          {/* name. */}
          <Box sx={{ display: 'flex', marginTop: '10px', paddingBottom: '4px' }}>
            <div className="flex w-full justify-between">
              <Typography className="break-all" variant="h5">
                {projectDetails.projectDetails.name}
              </Typography>

              <div className="ml-2" style={{ cursor: 'pointer' }}>
                <Tooltip title={'Edit Project'}>
                  <Button
                  
                    disabled={canEditFromProject(
                      role,
                      resourceId,
                      projectDetails.projectDetails.manager.id,
                      projectDetails.projectDetails.edited,
                      projectDetails.projectDetails.projectType
                    )}
                    onClick={handleEditNavigation}
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="24px"
                      height="24px"
                      viewBox="0 0 24 24"
                    >
                      <path
                        fill={
                          canEditFromProject(
                            role,
                            resourceId,
                            projectDetails.projectDetails.manager.id,
                            projectDetails.projectDetails.edited,
                            projectDetails.projectDetails.projectType
                          )
                            ? '#dadada': '#637381'
                            
                        }
                        d="m21.561 5.318l-2.879-2.879A1.495 1.495 0 0 0 17.621 2c-.385 0-.768.146-1.061.439L13 6H4a1 1 0 0 0-1 1v13a1 1 0 0 0 1 1h13a1 1 0 0 0 1-1v-9l3.561-3.561c.293-.293.439-.677.439-1.061s-.146-.767-.439-1.06M11.5 14.672L9.328 12.5l6.293-6.293l2.172 2.172zm-2.561-1.339l1.756 1.728L9 15zM16 19H5V8h6l-3.18 3.18c-.293.293-.478.812-.629 1.289c-.16.5-.191 1.056-.191 1.47V17h3.061c.414 0 1.108-.1 1.571-.29c.464-.19.896-.347 1.188-.64L16 13zm2.5-11.328L16.328 5.5l1.293-1.293l2.171 2.172z"
                      ></path>
                    </svg>
                  </Button>
                </Tooltip>
              </div>
            </div>
          </Box>
          <Typography sx={{ marginLeft: '8px', color: 'gray' }} className="break-all">
            {projectDetails.projectDetails.description
              ? projectDetails.projectDetails.description
              : ''}
          </Typography>
          <Grid lineHeight={3}>
            <Grid item sx={{ display: 'flex', width: '100%' }}>
              {/* project code.  */}
              <Grid item mt={2}>
                <Box
                  sx={{
                    display: 'flex',
                    alignItems: 'start',
                    justifyContent: 'start',
                  }}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    className="feather feather-hash"
                  >
                    <line x1="4" y1="9" x2="20" y2="9"></line>
                    <line x1="4" y1="15" x2="20" y2="15"></line>
                    <line x1="10" y1="3" x2="8" y2="21"></line>
                    <line x1="16" y1="3" x2="14" y2="21"></line>
                  </svg>
                  <Typography className="break-all" sx={{ marginLeft: '16px', width: '100%' }}>
                    {projectDetails.projectDetails.projectCode}
                  </Typography>
                </Box>
              </Grid>
            </Grid>
            {/* project type. */}
            <Grid item mt={2}>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'start' }}>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                  <path
                    fill="currentColor"
                    d="M12 2.75a9.25 9.25 0 1 0 0 18.5a9.25 9.25 0 0 0 0-18.5ZM1.25 12C1.25 6.063 6.063 1.25 12 1.25S22.75 6.063 22.75 12S17.937 22.75 12 22.75S1.25 17.937 1.25 12ZM12 5.25a.75.75 0 0 1 .75.75v.317c1.63.292 3 1.517 3 3.183a.75.75 0 0 1-1.5 0c0-.678-.564-1.397-1.5-1.653v3.47c1.63.292 3 1.517 3 3.183s-1.37 2.891-3 3.183V18a.75.75 0 0 1-1.5 0v-.317c-1.63-.292-3-1.517-3-3.183a.75.75 0 0 1 1.5 0c0 .678.564 1.397 1.5 1.652v-3.469c-1.63-.292-3-1.517-3-3.183s1.37-2.891 3-3.183V6a.75.75 0 0 1 .75-.75Zm-.75 2.597c-.936.256-1.5.975-1.5 1.653s.564 1.397 1.5 1.652V7.848Zm1.5 5v3.306c.936-.256 1.5-.974 1.5-1.653c0-.678-.564-1.397-1.5-1.652Z"
                  />
                </svg>
                <Typography sx={{ marginLeft: '16px' }}>
                  {getProjectType(projectDetails.projectDetails.projectType)}
                </Typography>
              </Box>
            </Grid>

            <Grid item sx={{ marginTop: '24px' }}>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'start' }}>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 32 32">
                  <path
                    fill="currentColor"
                    d="M23.586 21.414L27.166 25l-3.582 3.587L25 30l5-5l-5-5zm-3.172 0L16.834 25l3.582 3.587L19 30l-5-5l5-5zM22 6h2v8h-2zm-4 0h2v8h-2zm-4 8h-2c-1.103 0-2-.897-2-2V8c0-1.103.897-2 2-2h2c1.103 0 2 .897 2 2v4c0 1.103-.897 2-2 2zm-2-6v4h2V8h-2zM6 6h2v8H6z"
                  />
                  <path
                    fill="currentColor"
                    d="M10 28H4c-1.103 0-2-.897-2-2V4c0-1.103.897-2 2-2h22c1.103 0 2 .897 2 2v12h-2V4H4v22h6v2z"
                  />
                </svg>
                <Typography sx={{ marginLeft: '16px' }}>
                  {projectDetails.projectDetails.skill &&
                  projectDetails.projectDetails.skill.length > 0
                    ? projectDetails.projectDetails.skill.map((skill, index) => (
                        <span key={skill.id}>
                          {skill.name}
                          {index < projectDetails.projectDetails.skill.length - 1 ? ', ' : ''}
                        </span>
                      ))
                    : 'Nil'}
                </Typography>
              </Box>
            </Grid>
          </Grid>

          <Grid item pt={2} xs={6} md={6} sm={6}>
            <Box sx={{ display: 'flex' }}>
              <Box>Manager :</Box>
              <Typography sx={{ marginLeft: '8px' }}>
                {projectDetails.projectDetails.manager.name}{' '}
              </Typography>
            </Box>
          </Grid>

          <Grid item pt={2} xs={6} md={6} sm={6}>
            <Box sx={{ display: 'flex' }}>
              <Box className="whitespace-nowrap">Client :</Box>
              <Typography sx={{ marginLeft: '8px' }}>
                {projectDetails.projectDetails.clientName}{' '}
              </Typography>
            </Box>
          </Grid>

          {/* start date. */}
          <Grid item pt={2} xs={6} md={6} sm={6}>
            <Box sx={{ display: 'flex' }}>
              <Box className="whitespace-nowrap">Start Date :</Box>
              <Typography sx={{ marginLeft: '8px' }}>
                {projectDetails.projectDetails.startDate
                  ? projectDetails.projectDetails.startDate
                  : 'Nil'}
              </Typography>
            </Box>
          </Grid>

          {/* end date. */}
          <Grid item pt={2} xs={6} md={6} sm={6}>
            <Box sx={{ display: 'flex' }}>
              <Box className="whitespace-nowrap">End Date :</Box>
              <Typography sx={{ marginLeft: '8px' }}>
                {projectDetails.projectDetails.endDate
                  ? projectDetails.projectDetails.endDate
                  : 'Nil'}
              </Typography>
            </Box>
          </Grid>
        </Card>
      </Grid>
    </Grid>
  );
};

export default ProjectDetailsContent;
ProjectDetailsContent.propTypes = {
  projectDetails: PropTypes.any,
};
