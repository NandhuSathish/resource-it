import React, { useEffect, useState } from 'react';
import ProjectDetails from '../project-detaills';
import { useParams } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import ProjectAllocationEditTabs from '../project-allocation-edit-tabs';
import useProjects from 'src/hooks/use-projects';

function ProjectDetailView() {
  let { id } = useParams();
  const location = useLocation();
  const { project, allocationData } = location.state;

  const [projectData, setProjectData] = useState(null);
  const { getProjectById } = useProjects();

  const getProject = () => {
    console.log(project)
    getProjectById.mutate(id, {
      onSuccess: (data) => {
        setProjectData(data);
      },
    });
  };

    useEffect(() => {
      getProject();
    }, []);


if (projectData==null)
{
  return null
}
else {

  return (
    <>
      <ProjectDetails projectDetails={projectData} />
      <ProjectAllocationEditTabs
        projectDetails={projectData}
        handleUpdateProject={getProject}
        allocationData={allocationData}
      />
    </>
  );
}
}

export default ProjectDetailView;
