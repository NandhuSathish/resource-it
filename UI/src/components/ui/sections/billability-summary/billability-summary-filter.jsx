/* eslint-disable no-unexpected-multiline */
/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { Box, Button, Typography, IconButton, Slider, Tooltip } from '@mui/material';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import useDepartment from 'src/hooks/use-department';
import Iconify from 'src/components/iconify';
import useBillabilitySummaryQueryStore from 'src/components/ui/stores/billabilitySummaryStore';
import { filterSelectedSkills, mapToOptions } from 'src/utils/utils';
import { expertise } from 'src/utils/constants';
import { selectStyles } from 'src/utils/cssStyles';
import useSkill from 'src/hooks/use-get-skill';
import useProjects from 'src/hooks/use-projects';
import { FilterDrawer } from 'src/components/ui/filter-drawer';
import { useLocation } from 'react-router-dom';

// ----------------------------------------------------------------------

// ----------------------------------------------------------------------
const animatedComponents = makeAnimated();

export default function BillabilitySummaryFilters() {
  const billabilitySummaryFilters = useBillabilitySummaryQueryStore(
    (s) => s.billabilitySummaryFilters
  );
  const billabilitySummaryQuery = useBillabilitySummaryQueryStore((s) => s.billabilitySummaryQuery);
  const setStartDateFilter = useBillabilitySummaryQueryStore((s) => s.setStartDateFilter);
  const setEndDateFilter = useBillabilitySummaryQueryStore((s) => s.setEndDateFilter);
  const setSkillExpereinceFilter = useBillabilitySummaryQueryStore(
    (s) => s.setSkillExpereinceFilter
  );
  const setDepartmentFilter = useBillabilitySummaryQueryStore((s) => s.setDepartmentFilter);
  const setExperienceFilter = useBillabilitySummaryQueryStore((s) => s.setExperienceFilter);
  const setProjectFilter = useBillabilitySummaryQueryStore((s) => s.setProjectFilter);
  const setClearAllFilters = useBillabilitySummaryQueryStore((s) => s.setClearAllFilters);
  const { getProjectNameAndId } = useProjects();
  const [formFields, setFormFields] = useState(billabilitySummaryFilters.skillAndExperienceFilters);
  const [openFilter, setOpenFilter] = useState(false);
  const location = useLocation();
  const { flag } = location.state ? location.state : { flag: null };
  const expertiseOptions = mapToOptions(expertise, 'id', 'name');

  useEffect(() => {
    if (flag === 1) {
      setFormFields([{ skill: '', proficiency: [], experience: [0, 30] }]);
    }
  }, []);

  const dateStyles = {
    height: '45px',
  };

  // get the department options from the department api. and map it to the options.
  let departmentOptions = [];
  const { data: departments } = useDepartment();
  if (departments) {
    departmentOptions = mapToOptions(departments, 'departmentId', 'name');
  }
  let skillOptions = [];
  const { data: skills } = useSkill();
  if (skills) {
    skillOptions = mapToOptions(skills, 'id', 'name');
  }
  let projectOptions = [];
  const { data: projects } = getProjectNameAndId(false);
  if (projects) {
    projectOptions = mapToOptions(projects, 'projectId', 'projectName', 'projectCode');
  }

  //   add fields to the skillForm.
  const addFields = () => {
    let object = {
      skill: '',
      proficiency: [],
      experience: [0, 30],
    };
    setFormFields([...formFields, object]);
  };

  // handle the value changes in the skill form  .
  const handleSkillForm = (value, index, name) => {
    let data = [...formFields];
    data[index][name] = value;
    setSkillExpereinceFilter(data);
  };

  // remove fields from the skill form .
  const removeFields = (index) => {
    let data = [...formFields];
    if (data.length === 1) {
      data = [
        {
          skill: '',
          proficiency: [],
          experience: [0, 30],
        },
      ];
    } else {
      data.splice(index, 1);
    }

    setFormFields(data);
    setSkillExpereinceFilter(data);
  };

  // handle the filter bar  state.
  const handleOpenFilter = () => {
    setOpenFilter(true);
  };

  const handleCloseFilter = () => {
    setOpenFilter(false);
  };

  const handleClearAllFilters = () => {
    let data = [
      {
        skill: '',
        proficiency: [],
        experience: [0, 30],
      },
    ];
    setFormFields(data);
    setClearAllFilters();
    setSkillExpereinceFilter(data);
  };

  return (
    <FilterDrawer
      openFilter={openFilter}
      handleOpenFilter={handleOpenFilter}
      handleCloseFilter={handleCloseFilter}
      title="Filters"
      icon="ic:round-filter-list"
    >
      <div className="">
        <div id="startDate" className="px-6 py-2">
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
              Start Date
            </Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                format="DD/MM/YYYY"
                sx={dateStyles}
                maxDate={
                  billabilitySummaryQuery.endDate
                    ? dayjs(billabilitySummaryQuery.endDate)
                    : dayjs().endOf('month')
                }
                value={
                  billabilitySummaryFilters.startDateFilters
                    ? dayjs(billabilitySummaryFilters.startDateFilters)
                    : null
                }
                onChange={(newValue) => {
                  setStartDateFilter(newValue ? newValue.toDate() : null);
                }}
                slotProps={{
                  textField: {
                    readOnly: true,
                  },
                }}
              />
            </LocalizationProvider>
          </Box>
        </div>

        <div id="endDate" className="px-6 py-2">
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
                  billabilitySummaryQuery.startDate
                    ? dayjs(billabilitySummaryQuery.startDate)
                    : null
                }
                value={
                  billabilitySummaryFilters.endDateFilters
                    ? dayjs(billabilitySummaryFilters.endDateFilters)
                    : null
                }
                onChange={(newValue) => {
                  setEndDateFilter(newValue ? newValue.toDate() : null);
                }}
                slotProps={{
                  textField: {
                    readOnly: true,
                  },
                }}
              />
            </LocalizationProvider>
          </Box>
        </div>
        <div id="department" className="px-6 py-2">
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            Department
          </Typography>
          <Select
            styles={{
              control: (provided) => ({
                ...provided,
                ...selectStyles,
              }),
            }}
            value={billabilitySummaryFilters.departmentFilters}
            components={animatedComponents}
            closeMenuOnSelect={false}
            isMulti
            options={departmentOptions}
            onChange={(selectedOptions) => {
              setDepartmentFilter(selectedOptions);
            }}
          />
        </div>

        <div id="projectName" className="px-6 py-2">
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            Project Code
          </Typography>
          <Select
            styles={{
              control: (provided) => ({
                ...provided,
                ...selectStyles,
              }),
            }}
            components={animatedComponents}
            closeMenuOnSelect={false}
            isMulti
            value={billabilitySummaryFilters.billabilitySummaryFilters}
            options={projectOptions}
            onChange={(selectedOptions) => {
              setProjectFilter(selectedOptions);
            }}
          />
        </div>

        <div id="totalExperience" className="px-6 py-2">
          <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
            Total Experience
          </Typography>
          <Box sx={{ width: '90%', marginX: 'auto', marginY: 0.5 }}>
            <Slider
              getAriaLabel={() => 'Temperature range'}
              value={billabilitySummaryFilters.ExperienceFilters}
              onChange={(event, newValue) => {
                setExperienceFilter(newValue);
              }}
              valueLabelDisplay="auto"
              disableSwap
              min={0}
              max={30}
              step={1}
              marks={[
                { value: 0, label: '0 Year' },
                { value: 5, label: '5' },
                { value: 10, label: '10' },
                { value: 15, label: '15' },
                { value: 20, label: '20' },
                { value: 25, label: '25' },
                { value: 30, label: '30 Years' },
              ]}
            />
          </Box>
        </div>

        {/* skill and exp  */}
        <div id="skillExp" className="px-6 py-2 ">
          <Box>
            <form>
              {formFields.map((form, index) => {
                const isLastField = index === formFields.length - 1;
                const key = index;
                return (
                  <div key={key}>
                    <div className="flex flex-col items-center justify-between ">
                      <div id="skill" className="w-full ">
                        <Typography variant="subtitle2" sx={{ mb: 0.75, mt: 0.75 }}>
                          Skill
                        </Typography>
                        <Select
                          styles={{
                            control: (provided) => ({
                              ...provided,
                              borderColor: '',
                              ...selectStyles,
                            }),
                          }}
                          closeMenuOnSelect={true}
                          options={filterSelectedSkills(
                            skillOptions,
                            billabilitySummaryFilters.skillAndExperienceFilters
                          )}
                          name="skill"
                          onChange={(event) => handleSkillForm(event.value, index, 'skill')}
                          value={
                            skillOptions.find((option) => option.value === form.skill)
                              ? { ...skillOptions.find((option) => option.value === form.skill) }
                              : null
                          }
                        />
                      </div>

                      <div id="proficiency" className="w-full ">
                        <Typography variant="subtitle2" sx={{ mb: 0.75, mt: 0.75 }}>
                          Proficiency
                        </Typography>
                        <Select
                          isDisabled={!form.skill}
                          isMulti
                          styles={{
                            control: (provided) => ({
                              ...provided,
                              borderColor: '',
                              ...selectStyles,
                            }),
                          }}
                          closeMenuOnSelect={false}
                          options={expertiseOptions}
                          name="proficiency"
                          value={expertiseOptions.filter(
                            (option) =>
                              billabilitySummaryFilters.skillAndExperienceFilters.map(
                                (filter) => filter.proficiency
                              )[index] &&
                              billabilitySummaryFilters.skillAndExperienceFilters
                                .map((filter) => filter.proficiency)
                                [index].includes(option.value)
                          )}
                          onChange={(event) =>
                            handleSkillForm(
                              event.map((filter) => filter.value),
                              index,
                              'proficiency'
                            )
                          }
                        />
                      </div>

                      <div className="w-full flex  items-center justify-between mt-2">
                        <div id="experience" className="w-9/12">
                          <Typography variant="subtitle2" sx={{ mb: 0.75, mt: 0.75 }}>
                            Experience (Y)
                          </Typography>
                          <Box sx={{ width: '90%', marginX: 'auto', marginY: 0.5 }}>
                            <Slider
                              getAriaLabel={() => 'Temperature range'}
                              value={form.experience}
                              onChange={(event, newValue) =>
                                handleSkillForm(newValue, index, 'experience')
                              }
                              valueLabelDisplay="auto"
                              disableSwap
                              min={0}
                              max={30}
                              step={1}
                              marks={[
                                { value: 0, label: '0 Y' },
                                { value: 5, label: '5' },
                                { value: 10, label: '10' },
                                { value: 15, label: '15' },
                                { value: 20, label: '20' },
                                { value: 25, label: '25' },
                                { value: 30, label: '30 Y' },
                              ]}
                              disabled={!form.skill}
                            />
                          </Box>
                        </div>
                        <div className="w-3/12 ml-4  flex ">
                          <Tooltip title={'Delete'}>
                            <span>
                              <IconButton
                                onClick={() => removeFields(index)}
                                size="small"
                                // disabled={formFields.length <= 1}
                                sx={{
                                  color: (theme) => theme.palette.error.main,
                                }}
                              >
                                <Iconify icon="eva:trash-2-outline" />
                              </IconButton>
                            </span>
                          </Tooltip>
                          {/* only show the add_btn in the last element only.  */}
                          {isLastField && formFields.length < 5 && (
                            <Tooltip title={'Add'}>
                              <span>
                                <IconButton disabled={!form.skill} onClick={addFields}>
                                  <Iconify
                                    icon="solar:add-square-broken"
                                    sx={{
                                      color: (theme) =>
                                        form.skill
                                          ? theme.palette.primary.main
                                          : theme.palette.action.disabled,
                                    }}
                                  />
                                </IconButton>
                              </span>
                            </Tooltip>
                          )}
                        </div>
                      </div>
                    </div>
                  </div>
                );
              })}
            </form>
          </Box>
        </div>
      </div>
      {/* clear all btn  */}
      <Box sx={{ p: 3 }}>
        <Button
          fullWidth
          size="large"
          type="submit"
          color="inherit"
          variant="outlined"
          startIcon={<Iconify icon="ic:round-clear-all" />}
          onClick={handleClearAllFilters}
        >
          Clear All
        </Button>
      </Box>
    </FilterDrawer>
  );
}
