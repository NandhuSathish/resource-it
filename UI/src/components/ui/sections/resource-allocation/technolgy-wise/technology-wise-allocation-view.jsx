/* eslint-disable react/react-in-jsx-scope */
import { Card } from '@mui/material';
import TechnologyWiseAllocationForm from './technologyWiseAllocationForm';
// ----------------------------------------------------------------------

export default function TechnologyWiseView() {
  return (
    <div>
      <Card sx={{ padding: 3, minHeight: '100vh' }}>
        <TechnologyWiseAllocationForm />
      </Card>
    </div>
  );
}
