/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import {
  Box,
  Stack,
  Button,
  Drawer,
  Divider,
  Typography,
  IconButton,
  Slider,
  Tooltip,
} from '@mui/material';
import Select from 'react-select';
import { selectStyles } from 'src/utils/cssStyles';
import makeAnimated from 'react-select/animated';
import useDepartment from 'src/hooks/use-department';
import Iconify from 'src/components/iconify';
import useAllocationApprovalResourceQueryStore from 'src/components/ui/stores/allocationApprovalResourceStore';
import { mapToOptions } from 'src/utils/utils';
import useSkill from 'src/hooks/use-get-skill';
import { expertise } from 'src/utils/constants';
// ----------------------------------------------------------------------

const animatedComponents = makeAnimated();

export default function TechnologyWiseApprovalResourceListFilters() {
  const resourceFilters = useAllocationApprovalResourceQueryStore((s) => s.resourceFilters);

  const setSkillExpereinceFilter = useAllocationApprovalResourceQueryStore(
    (s) => s.setSkillExpereinceFilter
  );

  const setDepartmentFilter = useAllocationApprovalResourceQueryStore((s) => s.setDepartmentFilter);
  const setExperienceFilter = useAllocationApprovalResourceQueryStore((s) => s.setExperienceFilter);
  const reset = useAllocationApprovalResourceQueryStore((s) => s.reset);
  const [formFields, setFormFields] = useState(resourceFilters.skillAndExperienceFilters);

  const [openFilter, setOpenFilter] = useState(false);
  const requestDetails = useAllocationApprovalResourceQueryStore((s) => s.requestDetails);

  const setFields = () => {
    setFormFields(resourceFilters.skillAndExperienceFilters);
  };
  useEffect(() => {
    setFields();
  }, [resourceFilters.skillAndExperienceFilters]);

  const resetFilter = () => {
    reset();
    setFields();
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

  const expertiseOptions = mapToOptions(expertise, 'id', 'name');

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
              value={departmentOptions.find(
                (option) => option.value === requestDetails.departmentId
              )}
              components={animatedComponents}
              closeMenuOnSelect={false}
              isMulti
              options={departmentOptions}
              onChange={(selectedOptions) => {
                setDepartmentFilter(selectedOptions);
              }}
              isDisabled={true}
            />
          </div>

          <div id="experience" className="px-6 py-2">
            <Typography variant="subtitle2" sx={{ mb: 0.75 }}>
              Total Experience
            </Typography>
            <Box sx={{ width: '90%', marginX: 'auto', marginY: 0.5 }}>
              <Slider
                getAriaLabel={() => 'Temperature range'}
                value={resourceFilters.ExperienceFilters}
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

          {/* skill and exp filter */}
          <div id="skillExp" className="px-6 py-2 ">
            <Box>
              <form>
                {formFields.map((form, index) => {
                  const key = index;
                  return (
                    <div key={key}>
                      <div className="flex flex-col items-center justify-between ">
                        <div id="skill" className="w-full ">
                          <Typography variant="subtitle2" sx={{ mb: 0.75, mt: 0.75 }}>
                            Skill
                          </Typography>
                          <Select
                            isDisabled={true}
                            styles={{
                              control: (provided) => ({
                                ...provided,
                                borderColor: '',
                                ...selectStyles,
                              }),
                            }}
                            closeMenuOnSelect={true}
                            options={skillOptions}
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
                                backgroundColor: form.skill ? 'white' : null,
                                borderColor: '',
                                ...selectStyles,
                              }),
                            }}
                            closeMenuOnSelect={false}
                            options={expertiseOptions}
                            name="proficiency"
                            // value={expertiseOptions.filter(
                            //   (option) =>
                            //     resourceFilters.skillAndExperienceFilters.map(
                            //       (filter) => filter.proficiency
                            //     )[index] &&
                            //     resourceFilters.skillAndExperienceFilters
                            //       .map((filter) => filter.proficiency)
                            //       [index].includes(option.value)
                            // )}
                            value={
                              form.proficiency
                                ? form.proficiency.map((proficiencyValue) =>
                                    expertiseOptions.find(
                                      (option) => option.value == proficiencyValue
                                    )
                                  )
                                : null
                            }
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
                                  disabled={formFields.length <= 1}
                                  sx={{
                                    color: (theme) => theme.palette.error.main,
                                  }}
                                >
                                  <Iconify icon="eva:trash-2-outline" />
                                </IconButton>
                              </span>
                            </Tooltip>
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
            onClick={() => resetFilter()}
          >
            Reset
          </Button>
        </Box>
      </Drawer>
    </>
  );
}
