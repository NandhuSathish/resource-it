/* eslint-disable react/react-in-jsx-scope */
import { Box, Button, Typography } from '@mui/material';
import PropTypes from 'prop-types';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import useResourceHistoryQueryStore from 'src/components/ui/stores/resourceHistoryStore';

// ----------------------------------------------------------------------

export default function ResourceHistoryTableFilters({joiningDate}) {
  const setStartDateFilter = useResourceHistoryQueryStore((s) => s.setStartDateFilter);
  const setEndDateFilter = useResourceHistoryQueryStore((s) => s.setEndDateFilter);
  const projectFilters = useResourceHistoryQueryStore((s) => s.projectFilters);

console.log('joiningDate', joiningDate);
const joiningDateDayjs = dayjs(joiningDate, 'DD-MM-YYYY');
console.log(joiningDateDayjs, 'joiningDateDayjs');
  const resetFilters = () => { 
    setStartDateFilter(joiningDateDayjs); 
    setEndDateFilter(dayjs());
   }

  const dateStyles = {
    height: '45px',
  };

  return (
    <div className=" flex gap-4 pb-8 mx-2">
      <div id="startDate" className="py-2 w-2/3">
        <Box sx={{ display: 'flex', flexDirection: 'column' }}>
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            Start Date
          </Typography>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
              format="DD/MM/YYYY"
              sx={dateStyles}
              maxDate={projectFilters.endDateFilters ? dayjs(projectFilters.endDateFilters) : null}
              value={
                projectFilters.startDateFilters ? dayjs(projectFilters.startDateFilters) : null
              }
              onChange={(newValue) => {
                setStartDateFilter(newValue ? newValue : null);
              }}
              slotProps={{
                textField: {
                  readOnly: true,
                  error: false,
                },
              }}
            />
          </LocalizationProvider>
        </Box>
      </div>

      <div id="endDate" className="py-2 w-2/3 ">
        <Box sx={{ display: 'flex', flexDirection: 'column' }}>
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            End Date
          </Typography>

          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
              format="DD/MM/YYYY"
              sx={dateStyles}
              maxDate={dayjs().endOf('month')}
              minDate={
                projectFilters.startDateFilters ? dayjs(projectFilters.startDateFilters) : null
              }
              value={projectFilters.endDateFilters ? dayjs(projectFilters.endDateFilters) : null}
              onChange={(newValue) => {
                setEndDateFilter(newValue ? newValue.toDate() : null);
              }}
              slotProps={{
                textField: {
                  readOnly: true,
                  error: false,
                },
              }}
            />
          </LocalizationProvider>
        </Box>
      </div>

      <div id="endDate" className="py-2 w-1/4 ">
        <Box sx={{ display: 'flex', flexDirection: 'column' }}>
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            &nbsp;
          </Typography>

          <Button
            type="submit"
            color="inherit"
            variant="outlined"
            onClick={() => resetFilters()}
            sx={{
              height: {
                sm: '45px',
                lg: '38px',
                xl: '42px',
              },
              width: '100%',
            }}
          >
            Reset
          </Button>
        </Box>
      </div>
    </div>
  );
}

ResourceHistoryTableFilters.propTypes = {
  joiningDate: PropTypes.any,
};
