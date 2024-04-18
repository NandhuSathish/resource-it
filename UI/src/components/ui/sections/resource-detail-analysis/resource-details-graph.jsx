import React from 'react';
import ReactApexChart from 'react-apexcharts';
import PropTypes from 'prop-types';
import ResourceHistoryTableFilters from './resource-history-filter';

const ResourceDetailsGraph = ({ resourceData }) => {
  const data = [resourceData.benchDays, resourceData.billableDays];
  const series = [
    {
      name: resourceData.name,
      data: data,
    },
  ];

  const options = {
    legend: {
      show: false, 
    },
    chart: {
      type: 'bar',
      width: '100%',
      toolbar: {
        show: false,
      },
    },
    plotOptions: {
      bar: {
        horizontal: false,
        distributed: true, // Add this line
        columnWidth: '30%',
      },
    },
    dataLabels: {
      enabled: false,
    },
    xaxis: {
      categories: ['Bench Days', 'Billable Days'],
      labels: {
        style: {
          fontSize: '14px', // Adjust this value to change the size of the x-axis labels
        },
      },
    },
    yaxis: {
      labels: {
        style: {
          fontSize: '14px', // Adjust this value to change the size of the y-axis labels
        },
      },
    },
    colors: ['#023e8a', '#00B3E6'],
  };

  return (
    <div id="chart" className="w-full pl-4 pr-4  ">
      <ResourceHistoryTableFilters joiningDate={resourceData.joiningDate} />
      <ReactApexChart options={options} series={series} type="bar" height={'85%'} width={'100%'} />
    </div>
  );
};

export default ResourceDetailsGraph;

ResourceDetailsGraph.propTypes = {
  resourceData: PropTypes.object.isRequired,
};
