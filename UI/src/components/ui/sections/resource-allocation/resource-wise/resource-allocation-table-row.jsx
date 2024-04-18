/* eslint-disable no-unused-vars */
/* eslint-disable react/react-in-jsx-scope */
import PropTypes from 'prop-types';
import TableRow from '@mui/material/TableRow';
import Checkbox from '@mui/material/Checkbox';
import TableCell from '@mui/material/TableCell';
import { Typography } from '@mui/material';
import { convertMonthsToYears, getProficency, mapLevelIdToLevel, monthToYear } from 'src/utils/utils';
import { disableCheckBox } from '../utils';

// ----------------------------------------------------------------------

export default function ResourceAllocationTableRow({
  handleClick,
  selected,
  employeeId,
  name,
  department,
  skill,
  experience,
}) {
  return (
    <TableRow hover tabIndex={-1} role="checkbox" selected={selected}>
      <TableCell padding="checkbox">
        <Checkbox
          disableRipple
          checked={selected}
          onChange={handleClick}
          disabled={!disableCheckBox()}
        />
      </TableCell>
      <TableCell>
        {name}
        <Typography variant="body2" sx={{ color: 'text.secondary' }}>
          {employeeId}
        </Typography>
      </TableCell>
      <TableCell>{department}</TableCell>
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
      <TableCell align="center">{monthToYear(experience)}</TableCell>
    </TableRow>
  );
}

ResourceAllocationTableRow.propTypes = {
  handleClick: PropTypes.func,
  selected: PropTypes.any,
  employeeId: PropTypes.any,
  name: PropTypes.any,
  department: PropTypes.any,
  skill: PropTypes.any,
  experience: PropTypes.any,
};
