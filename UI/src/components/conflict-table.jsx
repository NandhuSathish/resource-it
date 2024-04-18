import React from 'react';
import PropTypes from 'prop-types';
const ConflictTable = ({ data: data }) => {
  return (
    <table>
      <tbody>
        {data.map((item) => (
          <tr key={item.startDate} className="bg-white ">
            <td className="py-4 px-2 ">{item.resourceName}</td>
            <td className="py-4 px-2" style={{ overflow: 'hidden' }}>
              <div className="break-words w-[10rem]" >
                {item.projectName}
              </div>
            </td>
            <td className="py-4 px-2">{item.startDate}</td>
            <td className="py-4 px-2">{item.endDate}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

ConflictTable.propTypes = {
  data: PropTypes.array.isRequired,
};
export default ConflictTable;
