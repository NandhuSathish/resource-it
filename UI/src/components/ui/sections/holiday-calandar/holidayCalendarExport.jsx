/* eslint-disable react/react-in-jsx-scope */
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../../table';
import useHolidayCalendar from 'src/hooks/use-holiday-calendar';
import calendarStore from 'src/components/ui/stores/calendarStore';
import { Button, Typography } from '@mui/material';
import { useState, useEffect } from 'react';

const HolidayCalendarExport = () => {
  const [holidayList, setHolidayList] = useState(calendarStore.getState().calendars);
  const { exportCalendar } = useHolidayCalendar();

  const handleDownload = (year) => {
    exportCalendar.mutate(year);
  };

  useEffect(() => {
    const fetchData = async () => {
      const data = await calendarStore.getState().calendars;
      if (data.length > holidayList.length) {
        setHolidayList(data);
      }
    };
    fetchData();
    const unsubscribe = calendarStore.subscribe(() => {
      fetchData();
    });
    return () => {
      unsubscribe();
    };
  }, []);

  if (holidayList[0] !== undefined) {
    return (
      <div className="max-h-[300px] mt-4 overflow-y-auto">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="w-[100px]">No</TableHead>
              <TableHead>Year</TableHead>
              <TableHead>Upload Date</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {holidayList.map((calendar) => (
              <TableRow key={calendar.id}>
                <TableCell className="font-medium">{calendar.id}</TableCell>
                <TableCell>{calendar.year}</TableCell>
                <TableCell>{calendar.uploadedDate}</TableCell>
                <TableCell>
                  <div className="flex justify-start text-blue-400 hover:text-blue-500">
                    <Button onClick={() => handleDownload(calendar.year)}>
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="18"
                        height="18"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        strokeWidth="2"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        className="feather feather-download"
                      >
                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                        <polyline points="7 10 12 15 17 10"></polyline>
                        <line x1="12" y1="15" x2="12" y2="3"></line>
                      </svg>
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    );
  }
  if (holidayList[0] == undefined) {
    return (
      <div className="h-[50px] flex items-center justify-center ">
        <Typography variant="body2">No imports found!</Typography>
      </div>
    );
  }
};
export default HolidayCalendarExport;
