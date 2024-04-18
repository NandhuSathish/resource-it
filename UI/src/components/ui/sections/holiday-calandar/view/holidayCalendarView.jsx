import React, { useState, useEffect } from 'react';
import HolidayCalendarImport from '../holidayCalenderImport';
import HolidayCalendarExport from '../holidayCalendarExport';
import { CalenderValidationString } from 'src/utils/constants';

import { Typography } from '@mui/material';
import useHolidayCalendar from 'src/hooks/use-holiday-calendar';

export function HolidayCalendarView() {
  const [showExport, setShowExport] = useState(false);
  const { getCalendarData } = useHolidayCalendar();

  useEffect(() => {
    const timer = setTimeout(() => {
      setShowExport(true);
      getCalendarData.mutate({});
    }, 300);
    return () => clearTimeout(timer);
  }, []);

  return (
    <div>
      <div className="mb-6">
        <Typography>{CalenderValidationString}</Typography>
      </div>
      <HolidayCalendarImport />
      {showExport && <HolidayCalendarExport />}
    </div>
  );
}

export default HolidayCalendarView;
