import PropTypes from 'prop-types';
import { Box, Button, Card, Chip, Divider, Stack, Typography } from '@mui/material';
import Scrollbar from 'src/components/scrollbar';
import Iconify from 'src/components/iconify';
import useAllocationApprovalResourceQueryStore from '../../../stores/allocationApprovalResourceStore';

/* eslint-disable react/react-in-jsx-scope */
const TechnologyWiseApprovalResourceSelectedList = ({ onDeselect, onApprove }) => {
  const allocationRequestQuery = useAllocationApprovalResourceQueryStore(
    (s) => s.allocationRequestQuery
  );
  const requestDetails = useAllocationApprovalResourceQueryStore((s) => s.requestDetails);
  return (
    <Card sx={{ maxHeight: '100vh', height: '100%', paddingBottom: '200px' }}>
      <Box sx={{ p: 3 }}>
        <Button
          disabled={allocationRequestQuery.resources.length < requestDetails.count}
          sx={{
            padding: 1,
            width: '100%',
          }}
          variant="contained"
          color="inherit"
          startIcon={<Iconify icon="typcn:tick-outline" />}
          onClick={() => {
            onApprove();
          }}
        >
          Approve
        </Button>
      </Box>
      <Stack
        direction="row"
        alignItems="center"
        justifyContent="space-between"
        sx={{ px: 1, py: 2 }}
      >
        <Typography variant="subtitle1" sx={{ ml: 1 }}>
          Selected Resources{' '}
          <span className="text-gray-500">
            {' '}
            - {allocationRequestQuery.resources.length}/{requestDetails.count}
          </span>
        </Typography>
      </Stack>
      <Divider />
      <Scrollbar>
        <Stack spacing={3} sx={{ p: 3 }}>
          {allocationRequestQuery.resources.length > 0 &&
            allocationRequestQuery.resources.map((resource) => (
              <Chip
                key={resource.id}
                label={resource.name + ' (' + resource.code + ')'}
                sx={{
                  px: 1,
                  py: 3,
                  justifyContent: 'space-between',
                }}
                onDelete={() => onDeselect(resource)}
              ></Chip>
            ))}
        </Stack>
      </Scrollbar>
    </Card>
  );
};

export default TechnologyWiseApprovalResourceSelectedList;
TechnologyWiseApprovalResourceSelectedList.propTypes = {
  onDeselect: PropTypes.func,
  onApprove: PropTypes.func,
};
