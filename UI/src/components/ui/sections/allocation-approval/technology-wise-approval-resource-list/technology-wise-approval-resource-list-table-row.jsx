/* eslint-disable no-unused-vars */
/* eslint-disable react/react-in-jsx-scope */
import PropTypes from 'prop-types';
import TableRow from '@mui/material/TableRow';
import Checkbox from '@mui/material/Checkbox';
import TableCell from '@mui/material/TableCell';
import { convertMonthsToYears, getProficency, mapLevelIdToLevel } from 'src/utils/utils';
// ----------------------------------------------------------------------

export default function TechnologyWiseApprovalResourceListTableRow({
  handleClick,
  selected,
  employeeId,
  name,
  department,
  skill,
  selectedLength,
  count,
}) {
  return (
    <TableRow hover tabIndex={-1} role="checkbox" selected={selected}>
      <TableCell padding="checkbox">
        <Checkbox
          disableRipple
          checked={selected}
          onChange={handleClick}
          disabled={selectedLength === count && !selected}
        />
      </TableCell>
      <TableCell align="center">{employeeId}</TableCell>
      <TableCell>{name}</TableCell>
      <TableCell>{department}</TableCell>
      <TableCell>
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
    </TableRow>
  );
}

TechnologyWiseApprovalResourceListTableRow.propTypes = {
  handleClick: PropTypes.func,
  selected: PropTypes.any,
  employeeId: PropTypes.any,
  name: PropTypes.any,
  department: PropTypes.any,
  skill: PropTypes.any,
  selectedLength: PropTypes.any,
  count: PropTypes.any,
};
