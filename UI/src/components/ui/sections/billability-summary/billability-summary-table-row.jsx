import React from 'react';
import PropTypes from 'prop-types';
import { TableRow, TableCell, Box } from '@mui/material';
import { convertMonthsToYears, getProficency } from 'src/utils/utils';

// ----------------------------------------------------------------------

export default function BillabilitySummaryTableRow({
  empId,
  resourceId,
  name,
  department,
  project,
  exp,
  skill,
  benchDays,
  billableDays,
  handleNavigation,
}) {
  return (
    <TableRow hover>
      <TableCell>
        <Box
          sx={{
            width: {
              xs: '100px', // width on extra-small screens
              sm: '150px', // width on small screens
              md: '200px', // width on medium screens
              lg: '130px', // width on large screens
              xl: '127px', // width on extra-large screens
            },
          }}
        >
          <div className="break-words">{empId}</div>
        </Box>
      </TableCell>

      <TableCell
        onClick={() => {
          handleNavigation(resourceId);
        }}
        sx={{
          cursor: 'pointer',
          textDecoration: 'underline',
          color: 'primary.main',
          '&:hover': {
            color: 'primary.dark',
          },
        }}
      >
        <Box
          sx={{
            width: {
              xs: '100px', // width on extra-small screens
              sm: '150px', // width on small screens
              md: '200px', // width on medium screens
              lg: '130px', // width on large screens
              xl: '197px', // width on extra-large screens
            },
          }}
        >
          <div className="break-words"> {name}</div>
        </Box>
      </TableCell>

      <TableCell>{department}</TableCell>
      <TableCell>
        {project && project !== 'null'
          ? project.split(',').map((item, index, arr) => (
              <div className="break-words w-[10rem]" key={item}>
                {item}
                {index !== arr.length - 1 ? ',' : ''} <br />
              </div>
            ))
          : ''}
      </TableCell>

      <TableCell align="center">
        {skill.map((s) => {
          return (
            <div
              key={s.skillName}
              style={{ display: 'block', whiteSpace: 'nowrap', textAlign: 'justify' }}
            >
              {s.skillName} : {convertMonthsToYears(s.experience)}y : {getProficency(s.proficiency)}
            </div>
          );
        })}
      </TableCell>

      <TableCell align="center">{exp}</TableCell>

      <TableCell align="center">{billableDays}</TableCell>

      <TableCell align="center">{benchDays}</TableCell>
    </TableRow>
  );
}

BillabilitySummaryTableRow.propTypes = {
  empId: PropTypes.any,
  resourceId: PropTypes.any,
  name: PropTypes.any,
  department: PropTypes.any,
  project: PropTypes.any,
  skill: PropTypes.any,
  exp: PropTypes.any,
  benchDays: PropTypes.any,
  billableDays: PropTypes.any,
  handleNavigation: PropTypes.any,
};
