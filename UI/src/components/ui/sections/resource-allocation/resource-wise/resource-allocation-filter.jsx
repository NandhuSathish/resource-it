/* eslint-disable react/react-in-jsx-scope */
import { useState } from 'react';
import {
  Box,
  Stack,
  Button,
  Drawer,
  Divider,
  Typography,
  IconButton,
  OutlinedInput,
  Slider,
  Tooltip,
} from '@mui/material';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import useDepartment from 'src/hooks/use-department';
import Iconify from 'src/components/iconify';
import { selectStyles } from 'src/utils/cssStyles';
import useResourceAllocationQueryStore from 'src/components/ui/stores/resourceAllocationStore';
import { filterSelectedSkills, mapToOptions } from 'src/utils/utils';
import useSkill from 'src/hooks/use-get-skill';
import useAuth from 'src/hooks/use-auth';
import { expertise } from 'src/utils/constants';


// ----------------------------------------------------------------------
const animatedComponents = makeAnimated();

export default function ResourceAllocationFilters() {
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  const resourceAllocationFilters = useResourceAllocationQueryStore(
    (s) => s.resourceAllocationFilters
  );
  const resourceAllocationQuery = useResourceAllocationQueryStore((s) => s.resourceAllocationQuery);
  const setDepartmentFilter = useResourceAllocationQueryStore((s) => s.setDepartmentFilter);
  const setExperienceFilter = useResourceAllocationQueryStore((s) => s.setExperienceFilter);
  const setSkillExpereinceFilter = useResourceAllocationQueryStore(
    (s) => s.setSkillExpereinceFilter
  );
  const setClearAllFilters = useResourceAllocationQueryStore((s) => s.setClearAllFilters);
  const [filterName, setFilterName] = useState(
    resourceAllocationQuery.resourceName ? resourceAllocationQuery.resourceName : ''
  );
  const [formFields, setFormFields] = useState(resourceAllocationFilters.skillAndExperienceFilters);
  const setResourceName = useResourceAllocationQueryStore((s) => s.setResourceName);
  const [openFilter, setOpenFilter] = useState(false);
    const expertiseOptions = mapToOptions(expertise, 'id', 'name');

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

  // handle the filter bar  state.
  const handleOpenFilter = () => {
    setOpenFilter(true);
  };
  const handleCloseFilter = () => {
    setOpenFilter(false);
  };

  const handleFilterByName = (event) => {
    setFilterName(event.target.value);
    setResourceName(event.target.value.trim());
  };

  //   add fields to the skillForm.
  const addFields = () => {
    let object = {
      skill: '',
      proficiency:[],
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
          proficiency:[],
          experience: [0, 30],
        },
      ];
      //   setSkillExpereinceFilter([]);
    } else {
      data.splice(index, 1);
    }
    setFormFields(data);
    setSkillExpereinceFilter(data);
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
    setFilterName('');
    setClearAllFilters();
    setSkillExpereinceFilter(data);
  };

  return (
    <>
      <IconButton onClick={handleOpenFilter}>
        <Iconify icon="ic:round-filter-list" />
      </IconButton>
      <Drawer
        anchor="right"
        open={openFilter}
        onClose={handleCloseFilter}
        PaperProps={{
          sx: { width: 360, border: 'none', overflow: 'auto' },
        }}
      >
        <Stack
          direction="row"
          alignItems="center"
          justifyContent="space-between"
          sx={{ px: 1, py: 2 }}
        >
          <Typography variant="h5" sx={{ ml: 1 }}>
            Filters
          </Typography>
          <IconButton onClick={handleCloseFilter}>
            <Iconify icon="eva:close-fill" />
          </IconButton>
        </Stack>
        <Divider />
        <div>
          <div id="resource name" className="px-6 py-2">
            <Box sx={{ display: 'flex', flexDirection: 'column' }}>
              <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
                Resource Name
              </Typography>
              <OutlinedInput
                sx={{ paddingLeft: 1, paddingRight: 1 }}
                value={filterName}
                onChange={handleFilterByName}
                placeholder="Search Resource..."
              />
            </Box>
          </div>
          {/* department filter. */}
          {[2, 3].includes(currentLoggedUser) && (
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
                value={resourceAllocationFilters.departmentFilters}
                components={animatedComponents}
                closeMenuOnSelect={false}
                isMulti
                options={departmentOptions}
                onChange={(selectedOptions) => {
                  setDepartmentFilter(selectedOptions);
                }}
              />
            </div>
          )}

          <div id="totalExperience" className="px-6 py-2">
            <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
              Total Experience
            </Typography>
            <Box sx={{ width: '90%', marginX: 'auto', marginY: 0.5 }}>
              <Slider
                getAriaLabel={() => 'Temperature range'}
                value={resourceAllocationFilters.ExperienceFilters}
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
                  { value: 25, label: '25 ' },
                  { value: 30, label: '30 Years' },
                ]}
              />
            </Box>
          </div>

          {/* skill and exp filter */}
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
                            // options={skillOptions}
                            options={filterSelectedSkills(
                              skillOptions,
                              resourceAllocationFilters.skillAndExperienceFilters
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
                                backgroundColor: form.skill ? 'white' : 'white',
                                borderColor: '',
                                ...selectStyles,
                              }),
                            }}
                            closeMenuOnSelect={true}
                            options={expertiseOptions}
                            name="proficiency"
                            value={expertiseOptions.filter(
                              (option) =>
                                resourceAllocationFilters.skillAndExperienceFilters.map(
                                  (filter) => filter.proficiency
                                )[index] &&
                                resourceAllocationFilters.skillAndExperienceFilters
                                  .map((filter) => filter.proficiency)[index]
                                  .includes(option.value)
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
      </Drawer>
    </>
  );
}
