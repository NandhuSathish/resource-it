import PropTypes from 'prop-types';
import { Box, Button, Card, Chip, Divider, Stack, Typography } from '@mui/material';
import useResourceAllocationQueryStore from '../../../stores/resourceAllocationStore';
import Scrollbar from 'src/components/scrollbar';
import Iconify from 'src/components/iconify';
import dayjs from 'dayjs';
import { isButtonEnabledInResource } from '../utils';

/* eslint-disable react/react-in-jsx-scope */
const ResourceSelectedList = ({ onDeselect, onApprove }) => {
  const allocationRequestQuery = useResourceAllocationQueryStore((s) => s.allocationRequestQuery);

  return (
    <Card sx={{ maxHeight: '100vh', height: '100%', paddingBottom: '200px' }}>
      <Box sx={{ p: 3 }}>
        <Button
          disabled={
            dayjs(allocationRequestQuery.startDate).isAfter(
              dayjs(allocationRequestQuery.endDate)
            ) || !isButtonEnabledInResource(allocationRequestQuery)
          }
          sx={{
            padding: 1,
            width: '100%',
          }}
          variant="contained"
          color="inherit"
          startIcon={<Iconify icon="streamline:send-email-solid" />}
          onClick={() => {
            onApprove();
          }}
        >
          Request
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
          <span className="text-gray-500"> - {allocationRequestQuery.resources.length}</span>
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

export default ResourceSelectedList;
ResourceSelectedList.propTypes = {
  onDeselect: PropTypes.func,
  onApprove: PropTypes.func,
};
