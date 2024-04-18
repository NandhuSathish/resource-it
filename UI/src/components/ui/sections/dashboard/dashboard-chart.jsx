import React, { useEffect, useRef, useState } from 'react';
import ReactApexChart from 'react-apexcharts';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import { Menu, Grid, Button, MenuItem, Typography, Box, Skeleton } from '@mui/material';
import useDashboard from 'src/hooks/use-dashboard';
import useDepartment from 'src/hooks/use-department';
import { mapToOptions, mapToSrtingOptions } from 'src/utils/utils';
import useResourceQueryStore from 'src/components/ui/stores/resourceStore';
import { useNavigate } from 'react-router-dom';
import useSkill from 'src/hooks/use-get-skill';
import {
  benchedResourceFilter,
  internalResourceFilter,
  externalResourceFilter,
  getSliceValue,
} from './utils.js';
import Iconify from 'src/components/iconify/iconify.jsx';
import Select from 'react-select';

ChartJS.register(ArcElement, Tooltip, Legend);
const SORT_OPTIONS = [
  { value: '0', label: 'Departments' },
  { value: '1', label: 'Skills' },
];

export default function DashboardChart() {
  const setDepartmentFilter = useResourceQueryStore((s) => s.setDepartmentFilter);
  const setAllocationStatusFilter = useResourceQueryStore((s) => s.setAllocationStatusFilter);
  const setSkillExpereinceFilter = useResourceQueryStore((s) => s.setSkillExpereinceFilter);
  const setProjectFilter = useResourceQueryStore((s) => s.setProjectFilter);
  const setExperienceFilter = useResourceQueryStore((s) => s.setExperienceFilter);
  const setClearAllFilters = useResourceQueryStore((s) => s.setClearAllFilters);
  const setSearchResourceText = useResourceQueryStore((s) => s.setSearchText);
  const navigate = useNavigate();
  const { getDashboardPieChartData } = useDashboard();
  const [chartValues, setChartValues] = useState({});
  const [chartMode, setChartMode] = useState(0);
  const [billable, setBillable] = useState(2);
  const [internal, setInternal] = useState(1);
  const [benched, setBenched] = useState(0);
  const [billableChecked, setBillableChecked] = useState(true);
  const [internalChecked, setInternalChecked] = useState(true);
  const [benchedChecked, setBenchedChecked] = useState(true);
  const [menuOpen, setMenuOpen] = useState(false);
  const [newSkillFilter, setNewSkillFilter] = useState([]);

  const [skillFlag, setSkillFlag] = useState(false);
  const [selectedSkillId, setSelectedSkillId] = useState(null);

  ////trial////////////////
  const [anchorEl, setAnchorEl] = React.useState(null);
  const opens = Boolean(anchorEl);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleCloseSubMenu = () => {
    setAnchorEl(null);
  };

  ////////////////////////////////
  let filteredDepartments = [];
  let skillFilter = [];
  ////////////////////////////////////////

  const [open, setOpen] = useState(null);
  const [selectedMode, setSelectedMode] = useState('Departments');
  const [selectedSubMenu, setSelectedSubMenu] = useState(null);
  const [skillFilterSetter, setSkillFilterSetter] = useState(false);
  const [departmentFilterSetter, setDepartmentFilterSetter] = useState(0);
  const [allocationStatusSetter, setAllocationStatusFilterSetter] = useState(false);

  const handleClose = () => {
    setOpen(null);
  };
  const handleSelect = (value) => {
    setSelectedMode(value);
    handleClose();
  };
  const currentLabel = SORT_OPTIONS.find((option) => option.label === selectedMode)?.label;

  /////////////////////////////////////////////////////////////

  let departmentOptions = [];
  const { data: departments } = useDepartment();
  if (departments) {
    departmentOptions = mapToSrtingOptions(departments, 'departmentId', 'name');
  }

  let skillOptions = [];
  const { data: skillData } = useSkill();
  if (skillData) {
    skillOptions = mapToOptions(skillData, 'id', 'name');
  }

  const initialBillable = 2;
  const initialInternal = 1;
  const initialBenched = 0;

  const chartData = {
    billable: billable,
    internal: internal,
    benched: benched,
    chartMode: chartMode,
    skillIds: [selectedSkillId],
  };

  const handleBillable = () => {
    if (billable == 2) {
      switch (true) {
        case internal === initialInternal: {
          setBillable(1);
          setBillableChecked(!billableChecked);
          let newChartData = { ...chartData };
          for (let key in newChartData) {
            if (key !== 'chartMode' && newChartData[key] === 2) {
              newChartData[key] = 1;
            }
          }
          fetchData(newChartData);
          break;
        }

        case benched === initialBenched: {
          let newChartData = { ...chartData };
          for (let key in newChartData) {
            if (key !== 'chartMode' && newChartData[key] === 2) {
              newChartData[key] = 0;
            }
          }
          setBillable(0);
          setBillableChecked(!billableChecked);
          fetchData(newChartData);
          break;
        }
        default:
          break;
      }
    } else {
      setBillable(2);
      setBillableChecked(!billableChecked);
      let newChartData;
      newChartData = { ...chartData, billable: 2 };
      fetchData(newChartData);
    }
  };

  const handleInternal = () => {
    if (internal == 1) {
      switch (true) {
        case billable === initialBillable: {
          let newChartData = { ...chartData };
          for (let key in newChartData) {
            if (key !== 'chartMode' && newChartData[key] === 1) {
              newChartData[key] = 2;
            }
          }
          setInternalChecked(!internalChecked);
          setInternal(2);
          fetchData(newChartData);
          break;
        }
        case benched === initialBenched: {
          let newChartData = { ...chartData };
          for (let key in newChartData) {
            if (key !== 'chartMode' && newChartData[key] === 1) {
              newChartData[key] = 0;
            }
          }
          setInternal(0);
          setInternalChecked(!internalChecked);
          fetchData(newChartData);
          break;
        }
        default:
          break;
      }
    } else {
      setInternalChecked(!internalChecked);
      setInternal(1);
      let newChartData;
      newChartData = { ...chartData, internal: 1 };
      fetchData(newChartData);
    }
  };

  const handleBenched = () => {
    if (benched == 0) {
      switch (true) {
        case billable === initialBillable: {
          let newChartData = { ...chartData };
          for (let key in newChartData) {
            if (key !== 'chartMode' && newChartData[key] === 0) {
              newChartData[key] = 2;
            }
          }
          setBenchedChecked(!benchedChecked);
          setBenched(2);
          fetchData(newChartData);
          break;
        }
        case internal === initialInternal: {
          setBenched(1);
          setBenchedChecked(!benchedChecked);
          let newChartData = { ...chartData };
          for (let key in newChartData) {
            if (key !== 'chartMode' && newChartData[key] === 0) {
              newChartData[key] = 1;
            }
          }

          fetchData(newChartData);
          break;
        }
        default:
          break;
      }
    } else {
      setBenchedChecked(!benchedChecked);
      setBenched(0);
      let newChartData;
      newChartData = { ...chartData, benched: 0 };
      fetchData(newChartData);
    }
  };

  const handleChartMode = (value) => {
    if (value == 1) {
      setChartMode(1);
      let newChartData;
      newChartData = { ...chartData, chartMode: 1 };
      fetchData(newChartData);
    } else {
      setChartMode(0);
      let newChartData;
      newChartData = { ...chartData, chartMode: 0 };
      fetchData(newChartData);
    }
  };

  const navigateToResource = () => {
    if (chartMode == 0) {
      setAllocationStatusFilterSetter(true)

    } else {
      skillFilter.push({
        skill: selectedSkillId,
        experience: [0, 1300],
      });
      setDepartmentFilter([]);
      setProjectFilter([]);
      setExperienceFilter([0, 30]);
      setSkillFilterSetter(true)
     
    }
  };

  useEffect(() => {
    let filters = [];

    if (billableChecked) {
      filters.push(...externalResourceFilter);
    }

    if (internalChecked) {
      filters.push(...internalResourceFilter);
    }

    if (benchedChecked) {
      filters.push(...benchedResourceFilter);
    }
    setAllocationStatusFilter(filters);
   if(departmentFilterSetter>0){
         navigate('/resourceManagement/resourceList', {
           state: { flag: 1 },
         });
   }
  }, [allocationStatusSetter==true]);

  useEffect(() => {
    if (selectedSkillId != null) {
      setSkillExpereinceFilter(newSkillFilter);
       navigate('/resourceManagement/resourceList');
    }
  }, [skillFilterSetter==true]);

  const [chartSize, setChartSize] = useState(540);
  const fetchData = async (newChartData = chartData) => {
    await getDashboardPieChartData.mutateAsync(newChartData, {
      onSuccess: (chartValues) => {
        setChartValues(chartValues);
        const labelCount = chartValues.labels.length;
        if (labelCount <= 10) setChartSize(540);
        else if (labelCount <= 20) setChartSize(640);
      },
      onError: () => {},
    });
  };

  // Custom hook to detect clicks outside of the component
  function useOutsideAlerter(ref, callback) {
    useEffect(() => {
      function handleClickOutside(event) {
        if (ref.current && !ref.current.contains(event.target)) {
          callback();
        }
      }

      document.addEventListener('mousedown', handleClickOutside);
      return () => {
        document.removeEventListener('mousedown', handleClickOutside);
      };
    }, [ref, callback]);
  }

  const wrapperRef = useRef(null);
  useOutsideAlerter(wrapperRef, () => {
    setMenuOpen(false);
    if (selectedSkillId == null) {
      setSkillFlag(false);
    }
  });

  useEffect(() => {
    fetchData();
  }, []);

  // Original colors of the slices
  const apexOptions = {
    dataLabels: {
      enabled: false, // Add this line
    },
    colors: [
      '#4361ee',
      '#fe5f55',
      '#9d4edd',
      '#80ed99',
      '#61a5c2',
      '#ffc09f',
      '#00B3E6',
      '#bfc0c0',
      '#E6B333',
      '#cdb4db',
      '#274690',
      '#183a37',
    ],

    legend: {
      position: 'bottom',
      fontSize: '16px',
      itemMargin: {
        horizontal: 10, // Add this line
        vertical: 10, // Add this line
      },
    },
    tooltip: {
      style: {
        fontSize: '14px',
        fontWeight: 800, // Add this line
      },
    },

    chart: {
      width: '100%',
      type: 'pie',
      events: {
        dataPointSelection: (event, chartContext, config) => {
          const selectedSliceLabel = config.w.config.labels[config.dataPointIndex];
          if (chartMode === 0) {
            const department = departmentOptions.find(
              (option) => option.label === selectedSliceLabel
            );
            if (department) {
              filteredDepartments.push(department);
              setClearAllFilters();
              setSearchResourceText(null);
              setDepartmentFilter(filteredDepartments);
              setDepartmentFilterSetter(departmentFilterSetter+1);
              navigateToResource();
            }
          } else if (chartMode === 1) {
            setSearchResourceText(null);
            setAllocationStatusFilter([getSliceValue(selectedSliceLabel)]);
            navigateToResource();
          }
        },
      },
    },
    stroke: {
      width: 1,
    },
    labels: chartValues.labels,
    responsive: [
      {
        breakpoint: 480,
        options: {
          chart: {},
          legend: {
            position: 'bottom',
          },
        },
      },
    ],
  };

  const apexSeries = chartValues.datas;

  if (chartValues.labels && chartValues.datas) {
    return (
      <Grid container>
        <Grid item xs={12}>
          <Card>
            <CardContent style={{ marginBottom: '54px' }}>
              <Grid container>
                <Grid item xs={2} sm={2}>
                  <FormGroup>
                    <FormControlLabel
                      control={
                        <Checkbox
                          checked={billableChecked}
                          onChange={handleBillable}
                          disabled={(!internalChecked && !benchedChecked) || chartMode === 1}
                        />
                      }
                      label="Billable"
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          checked={internalChecked}
                          onChange={handleInternal}
                          disabled={(!billableChecked && !benchedChecked) || chartMode === 1}
                        />
                      }
                      label="Internal"
                    />
                    <FormControlLabel
                      control={
                        <Checkbox
                          checked={benchedChecked}
                          onChange={handleBenched}
                          disabled={(!billableChecked && !internalChecked) || chartMode === 1}
                        />
                      }
                      label="Benched"
                    />
                  </FormGroup>
                </Grid>
                <Grid
                  item
                  xs={8}
                  sm={8}
                  md={4}
                  lg={8}
                  xl={8}
                  sx={{ paddingTop: '40px', paddingBottom: '15px' }}
                >
                  {(chartValues.labels.length === 0 && chartValues.datas.length === 0)  ? (
                    <Box
                      display="flex"
                      justifyContent="center"
                      alignItems="center"
                      paddingBottom={8}
                    >
                      <Box display="flex" justifyContent="end" alignItems="center" height={1}>
                        <Skeleton
                          variant="circular"
                          width={450}
                          height={450}
                          style={{ backgroundColor: '#f1f3f4d6' }}
                        />
                      </Box>
                      <Box
                        top={0}
                        left={0}
                        bottom={0}
                        right={0}
                        position="absolute"
                        display="flex"
                        alignItems="center"
                        justifyContent="center"
                      >
                        <Typography
                          variant="body"
                          component="div"
                          color="#637381"
                          style={{ marginTop: '-70px' }}
                        >
                          &nbsp; No data found
                        </Typography>
                      </Box>
                    </Box>
                  ) : (
                    <div
                      style={{
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        height: '100%',
                        paddingBottom:'3px'
                      }}
                    >
                      <ReactApexChart
                        options={apexOptions}
                        series={apexSeries}
                        type="pie"
                        width={chartSize}
                        height={chartSize}
                      />
                    </div>
                  )}
                </Grid>
                <Grid item xs={2} spacing={2} sm={2} sx={{}}>
                  <div>
                    <Button
                      sx={{ width: 130 }}
                      disableRipple
                      color="inherit"
                      onClick={handleClick}
                      endIcon={
                        <Iconify icon={open ? 'eva:chevron-up-fill' : 'eva:chevron-down-fill'} />
                      }
                    >
                      <Typography
                        component="span"
                        variant="subtitle2"
                        sx={{ color: 'text.secondary' }}
                      >
                        {currentLabel}
                      </Typography>
                    </Button>
                    <Menu
                      id="menu"
                      anchorEl={anchorEl}
                      open={opens}
                      onClose={handleCloseSubMenu}
                      MenuListProps={{
                        'aria-labelledby': 'basic-button',
                      }}
                    >
                      <MenuItem
                        selected={'Departments' === selectedMode}
                        onClick={() => {
                          handleCloseSubMenu();
                          handleChartMode(0), handleSelect('Departments');
                          setSkillFlag(false);
                          setSelectedSkillId(null);
                          setSelectedSubMenu(null);
                        }}
                      >
                        Departments
                      </MenuItem>

                      <MenuItem
                        selected={'Skills' === selectedMode}
                        onClick={() => {
                          setMenuOpen(true);
                          handleCloseSubMenu();
                          setSkillFlag(true);
                        }}
                      >
                        Skills
                      </MenuItem>
                    </Menu>

                    {skillFlag ? (
                      <div ref={wrapperRef}>
                        <Select
                          value={
                            selectedSubMenu
                              ? skillOptions.find((option) => option.value === selectedSubMenu)
                              : null
                          }
                          onChange={(selectedOption) => {
                            setNewSkillFilter([
                              {
                                skill: Number(selectedOption.value),
                                experience: [0, 1300],
                              },
                            ]);
                            setSelectedSkillId(selectedOption.value);
                            setSelectedSubMenu(selectedOption.value);
                            handleCloseSubMenu();
                            handleChartMode(1);
                            handleSelect('Skills');
                            let newChartData;
                            newChartData = {
                              ...chartData,
                              skillIds: selectedOption.value,
                              chartMode: 1,
                            };
                            fetchData(newChartData);
                          }}
                          options={skillOptions}
                          components={{
                            DropdownIndicator: () => null,
                            IndicatorSeparator: () => null,
                          }}
                          styles={{
                            control: (base) => ({
                              ...base,
                              width: 130,
                              marginTop: '5px',
                            }),
                          }}
                          menuIsOpen={menuOpen}
                          onMenuOpen={() => setMenuOpen(true)}
                          onMenuClose={() => setMenuOpen(false)}
                        />
                      </div>
                    ) : null}
                  </div>
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    );
  }
}
