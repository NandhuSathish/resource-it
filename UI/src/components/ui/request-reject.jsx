/* eslint-disable react/react-in-jsx-scope */
import { useState } from 'react';
import PropTypes from 'prop-types';
import { Box, Button } from '@mui/material';
import { Textarea } from 'src/theme/text-area';

const RequestReject = ({ conflictData, onSubmit }) => {
  const [reason, setReason] = useState(conflictData);
  const handleSubmit = (event) => {
    event.preventDefault();
    onSubmit(reason);
    setReason(''); // reset the form
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <Box sx={{ display: 'flex', flexDirection: 'column' }}>
          <Textarea
            aria-label="Reason for rejection"
            minRows={5}
            placeholder="Please fill reason for rejection to proceed."
            value={reason}
            maxLength={1000}
            height={200}
            onChange={(event) => setReason(event.target.value)}
            className="font-sans"
          />

          <Box sx={{ display: 'flex', justifyContent: 'end' }}>
            <Button
              disabled={!reason}
              sx={{ marginTop: 2, width: '20%', borderRadius: 1 }}
              type="submit"
              variant="contained"
              color="inherit"
            >
              Submit
            </Button>
          </Box>
        </Box>
      </form>
    </div>
  );
};

RequestReject.propTypes = {
  conflictData: PropTypes.string,
  onSubmit: PropTypes.func.isRequired,
};

export default RequestReject;
