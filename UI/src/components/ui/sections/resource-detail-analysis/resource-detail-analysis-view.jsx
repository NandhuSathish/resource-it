import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import ResourceDetailsContents from './resource-details-contents';
import ResourceDetailsGraph from './resource-details-graph';
import { Container } from '@mui/material';
import ResourceHistoryTableView from './resource-history-table/view/resource-history-view';
import useResourceDetailAnalysis from 'src/hooks/use-resource-details-analysis';
import useResourceHistoryQueryStore from 'src/components/ui/stores/resourceHistoryStore';


const ResourceDetailAnalysisView = () => {
let { id } = useParams();
const resourceDetailsQuery = useResourceHistoryQueryStore((s) => s.resourceDetailsQuery);
const [resourceDetails, setResourceDetails] = useState(null);
const { getResorceDetailById } = useResourceDetailAnalysis();


const filterForDetails = {
  resourceId: id,
  query: resourceDetailsQuery,
};



  const getResourceDetails = () => {
    getResorceDetailById.mutate(filterForDetails, {
      onSuccess: (data) => {
        setResourceDetails(data);
      },
    });
  };



  useEffect(() => {
    getResourceDetails(id);
  }, [resourceDetailsQuery]);
if (!resourceDetails) {
    return null;
  }
  else{
  return (
    <Container maxWidth="100%">
      <div className="flex gap-4 justify-evenly pb-10">
        <ResourceDetailsContents resourceData={resourceDetails} />
        <ResourceDetailsGraph resourceData={resourceDetails} />
      </div>
      <div>
        <ResourceHistoryTableView resourceId={id} />
      </div>
    </Container>
  );
}
};

export default ResourceDetailAnalysisView;
