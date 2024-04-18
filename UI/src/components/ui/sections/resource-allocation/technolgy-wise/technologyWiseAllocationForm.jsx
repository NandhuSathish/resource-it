/* eslint-disable react/react-in-jsx-scope */
import {
  Box,
  Button,
  Grid,
  IconButton,
  OutlinedInput,
  Slider,
  Stack,
  TextField,
  Tooltip,
  Typography,
} from '@mui/material';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from 'src/components/ui/form';
import { useEffect, useState } from 'react';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import Select from 'react-select';
import { selectStyles } from 'src/utils/cssStyles';
import useDepartment from 'src/hooks/use-department';
import useProjects from 'src/hooks/use-projects';
import useTechnologyWiseAllocationQueryStore from 'src/components/ui/stores/technologyWiseAllocationStore';
import { mapToOptions } from 'src/utils/utils';
import Iconify from 'src/components/iconify';
import useSkill from 'src/hooks/use-get-skill';
import makeAnimated from 'react-select/animated';
import { z } from 'zod';
import {
  getAllocationStartMinDate,
  getMaxDate,
  getNonSelectedOptions,
  isButtonEnabled,
  transformTechnologyWiseRequest,
} from '../utils';
import dayjs from 'dayjs';
import { toast } from 'sonner';
import { errorCodeMap } from 'src/utils/error-codes';
import useTechnologyWiseAllocation from 'src/hooks/use-technology-wise-allocation';
import useAuth from 'src/hooks/use-auth';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { expertise } from 'src/utils/constants';

const animatedComponents = makeAnimated();

const schema = z.object({
  experience: z
    .string()
    .min(1, 'Required')
    .refine((value) => /^[0-9]+$/.test(value), 'Experience should be numbers only')
    .refine((value) => /^[0-9]{1,2}$/.test(value), 'Maximum limit for experience is 30')
    .refine((value) => {
      const numberValue = Number(value);
      return numberValue <= 30;
    }, 'Maximum limit for experience is 30'),
  count: z
    .string()
    .min(1, 'Required')
    .refine((value) => /^[0-9]+$/.test(value), 'Count should be numbers only')
    .refine((value) => /^[0-9]{1,2}$/.test(value), 'Maximum limit for count is 20')
    .refine((value) => {
      const numberValue = Number(value);
      return numberValue <= 20 && numberValue > 0;
    }, 'Count should be greater than 0 and less than or equal to 20'),
});

const TechnologyWiseAllocationForm = () => {
  const { getProjectNameAndId, getProjectById } = useProjects();
  const expertiseOptions = mapToOptions(expertise, 'id', 'name');
  const { requestTechnologyWise } = useTechnologyWiseAllocation();
  const { getUserDetails } = useAuth();
  const { role } = getUserDetails();
  const [project, setProject] = useState(null);
  const projectQuery = useTechnologyWiseAllocationQueryStore((s) => s.projectQuery);
  //query we used to send request to the api
  const technologyWiseAllocationRequestQuery = useTechnologyWiseAllocationQueryStore(
    (s) => s.technologyWiseAllocationRequestQuery
  );
  const setProjectId = useTechnologyWiseAllocationQueryStore((s) => s.setProjectId);
  const setDepartmentId = useTechnologyWiseAllocationQueryStore((s) => s.setDepartmentId);
  const setCount = useTechnologyWiseAllocationQueryStore((s) => s.setCount);
  const setExperience = useTechnologyWiseAllocationQueryStore((s) => s.setExperience);
  const setProjectStartDate = useTechnologyWiseAllocationQueryStore((s) => s.setProjectStartDate);
  const setProjectEndDate = useTechnologyWiseAllocationQueryStore((s) => s.setProjectEndDate);
  const setAllocationStartDate = useTechnologyWiseAllocationQueryStore(
    (s) => s.setAllocationStartDate
  );
  const setAllocationEndDate = useTechnologyWiseAllocationQueryStore((s) => s.setAllocationEndDate);
  const setSkillExperience = useTechnologyWiseAllocationQueryStore((s) => s.setSkillExperience);
  const setClearAllOnRequest = useTechnologyWiseAllocationQueryStore((s) => s.setClearAllOnRequest);
  const [requestCount, setRequestCount] = useState(
    technologyWiseAllocationRequestQuery.count ? technologyWiseAllocationRequestQuery.count : ''
  );
  const [requestExperience, setRequestExperience] = useState(
    technologyWiseAllocationRequestQuery.experience
      ? technologyWiseAllocationRequestQuery.experience
      : ''
  );
  // State variable for the project details
  const [formFields, setFormFields] = useState(projectQuery.skillExperience);
  // Effect that triggers when the selected project changes
  useEffect(() => {
    const fetchProjectDetails = async () => {
      try {
        const data = await getProjectById.mutateAsync(project);
        setProjectStartDate(data.startDate);
        setProjectEndDate(data.endDate);
        setFormFields([{ skillId: '', proficiency: [], experience: [0, 30] }]);
      } catch (error) {
        console.error('Failed to fetch project details', error);
      }
    };
    fetchProjectDetails();
  }, [project]);

  const handleProjectSelect = (selectedProject) => {
    setProject(selectedProject.value);
    setProjectId(selectedProject);
  };

  const handleRequest = () => {
    const transformedRequest = transformTechnologyWiseRequest(technologyWiseAllocationRequestQuery);
    requestTechnologyWise.mutate(transformedRequest, {
      onSuccess: () => {
        onReset();
        toast.success('Resource Requested Successfully');
      },
      onError: (error) => {
        toast.error(errorCodeMap[error.response.data.errorCode] || 'An unexpected error occurred');
      },
    });
  };
  let projectOptions = [];
  const { data: projects } = getProjectNameAndId(!(role === 2 || role === 3 || role === 4));
  if (projects) {
    projectOptions = mapToOptions(projects, 'projectId', 'projectCode', 'projectName');
  }
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

  //   add fields to the skillForm.
  const addFields = () => {
    let object = {
      skillId: '',
      proficiency: [],
      experience: [0, 30],
    };
    setFormFields([...formFields, object]);
  };
  // handle the value changes in the skill form  .
  const handleSkillForm = (value, index, name) => {
    let data = [...formFields];
    data[index][name] = value;
    setSkillExperience(data);
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
    setSkillExperience(data);
  };

  const onReset = () => {
    setClearAllOnRequest();
    setFormFields([{ skillId: '', proficiency: [], experience: [0, 30] }]);
    setRequestCount('');
    setRequestExperience('');
    setProject(null);
  };

  const form = useForm({
    resolver: zodResolver(schema),
    defaultValues:
      requestExperience && requestCount
        ? {
            experience: requestExperience,
            count: requestCount,
          }
        : {
            experience: '',
            count: '',
          },
    reValidateMode: 'onChange',
  });

  const onSubmit = (data) => {
    console.log(data);
    handleRequest();
  };

  return (
    <Grid item xs={12} md={8} sx={{ paddingBottom: 30 }}>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)}>
          <Form {...form}></Form>
          <Box
            sx={{
              display: 'grid',
              columnGap: 2,
              rowGap: 3,
              gridTemplateColumns: { xs: 'repeat(1, 1fr)', sm: 'repeat(3, 1fr)' },
            }}
          >
            {/* project */}
            <Box id="project" sx={{ minWidth: '100%' }}>
              <Typography variant="requestHeading" sx={{ mb: 0.75 }}>
                Project Name
              </Typography>
              <Select
                styles={{
                  control: (provided) => ({
                    ...provided,
                    ...selectStyles,
                    marginTop: '8px',
                  }),
                }}
                value={
                  projectQuery.projectId !== null
                    ? projectOptions.find((option) => option.value == projectQuery.projectId.value)
                    : null
                }
                components={animatedComponents}
                closeMenuOnSelect={true}
                options={projectOptions}
                onChange={handleProjectSelect}
              />
            </Box>
            {/* start date */}
            <Box
              sx={{
                width: '100%',
                display: 'flex',
                flexDirection: 'column',
              }}
            >
              <Typography variant="requestHeading" sx={{ marginBottom: '8px' }}>
                Start Date
              </Typography>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker
                  format="DD/MM/YYYY"
                  className="date-picker-input"
                  value={
                    technologyWiseAllocationRequestQuery.startDate
                      ? dayjs(technologyWiseAllocationRequestQuery.startDate)
                      : null
                  }
                  minDate={
                    projectQuery.projectStartDate
                      ? getAllocationStartMinDate(projectQuery.projectStartDate)
                      : null
                  }
                  maxDate={getMaxDate(projectQuery.projectEndDate)}
                  onChange={(newDate) => {
                    setAllocationStartDate(newDate);
                  }}
                  slotProps={{
                    textField: {
                      readOnly: true,
                    },
                  }}
                  renderInput={(props) => (
                    <TextField
                      {...props}
                      sx={{
                        padding: 0,
                      }}
                    />
                  )}
                />
              </LocalizationProvider>
            </Box>
            {/* end date */}
            <Box
              sx={{
                width: '100%',
                display: 'flex',
                flexDirection: 'column',
              }}
            >
              <Typography variant="requestHeading" sx={{ marginBottom: '8px' }}>
                End Date
              </Typography>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker
                  format="DD/MM/YYYY"
                  value={
                    technologyWiseAllocationRequestQuery.endDate
                      ? dayjs(technologyWiseAllocationRequestQuery.endDate)
                      : null
                  }
                  // setting the min date to the allocation start date
                  minDate={
                    technologyWiseAllocationRequestQuery.startDate
                      ? dayjs(technologyWiseAllocationRequestQuery.startDate)
                      : null
                  }
                  //   setting the max date to the project end date
                  maxDate={projectQuery.projectEndDate ? dayjs(projectQuery.projectEndDate) : null}
                  onChange={(newDate) => {
                    setAllocationEndDate(newDate);
                  }}
                  renderInput={(props) => (
                    <TextField
                      {...props}
                      sx={{
                        padding: 0,
                      }}
                    />
                  )}
                  slotProps={{
                    textField: {
                      readOnly: true,
                    },
                  }}
                  // other props...
                />
              </LocalizationProvider>
            </Box>
            {/* department */}
            <Box id="department" sx={{ width: '100%' }}>
              <Typography variant="subTitle2">Department</Typography>
              <Select
                styles={{
                  control: (provided) => ({
                    ...provided,
                    ...selectStyles,
                    marginTop: '8px',
                  }),
                }}
                value={projectQuery.departmentId}
                components={animatedComponents}
                closeMenuOnSelect={true}
                options={departmentOptions}
                onChange={(selectedOptions) => {
                  setDepartmentId(selectedOptions);
                }}
              />
            </Box>
            {/* total experience  */}
            <Box sx={{ width: '100%', display: 'flex', flexDirection: 'column' }}>
              <FormField
                control={form.control}
                name="experience"
                render={({ field }) => (
                  <FormItem>
                    <div>
                      <FormLabel>Total Experience (Years)</FormLabel>
                    </div>
                    <FormControl>
                      <OutlinedInput
                        key={project}
                        sx={{ paddingLeft: 1, paddingRight: 1, width: '100%' }}
                        inputProps={{
                          inputMode: 'numeric',
                          maxLength: 2,
                          onKeyPress: (event) => {
                            if (!/\d/.test(event.key)) {
                              event.preventDefault();
                            }
                          },
                        }}
                        {...field}
                        value={requestExperience}
                        onChange={(event) => {
                          field.onChange(event.target.value.trim());
                          setRequestExperience(event.target.value);
                          setExperience(event.target.value);
                        }}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </Box>
            {/* count.  */}
            <Box sx={{ width: '100%', display: 'flex', flexDirection: 'column' }}>
              <FormField
                control={form.control}
                name="count"
                render={({ field }) => (
                  <FormItem>
                    <div>
                      <FormLabel>Count</FormLabel>
                    </div>
                    <FormControl>
                      <OutlinedInput
                        key={project}
                        inputProps={{
                          inputMode: 'numeric',
                          onKeyPress: (event) => {
                            const value = event.target.value + event.key;
                            if (!/\d/.test(event.key) || value < 0 || value > 20) {
                              event.preventDefault();
                            }
                          },
                        }}
                        sx={{ paddingLeft: 1, paddingRight: 1, width: '100%' }}
                        {...field}
                        value={requestCount}
                        onChange={(event) => {
                          field.onChange(event.target.value.trim());
                          setRequestCount(event.target.value);
                          setCount(event.target.value);
                        }}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              {/* </div> */}
            </Box>
          </Box>
          {/* skill and experience  */}
          {formFields.map((form, index) => {
            const isLastField = index === formFields.length - 1;
            const key = index;
            return (
              <div key={key} className="w-full">
                <div className="flex  items-center gap-3">
                  <div id="skill" className="w-full">
                    <Typography variant="requestHeading">Skill</Typography>
                    <Select
                      key={project}
                      styles={{
                        control: (provided) => ({
                          ...provided,
                          borderColor: '',
                          ...selectStyles,
                          marginTop: '8px',
                        }),
                      }}
                      closeMenuOnSelect={true}
                      options={getNonSelectedOptions(
                        skillOptions,
                        technologyWiseAllocationRequestQuery.skillExperienceRequestDTO
                      )}
                      name="skill"
                      onChange={(event) => handleSkillForm(event.value, index, 'skillId')}
                      value={
                        skillOptions.find((option) => option.value === form.skillId)
                          ? { ...skillOptions.find((option) => option.value === form.skillId) }
                          : null
                      }
                    />
                  </div>
                  <div id="proficiency" className="w-full ">
                    <Typography variant="requestHeading">Proficiency</Typography>
                    <Select
                      key={project}
                      isDisabled={!form.skillId}
                      isMulti
                      styles={{
                        control: (provided) => ({
                          ...provided,
                          borderColor: '',
                          ...selectStyles,
                          marginTop: '8px',
                        }),
                      }}
                      closeMenuOnSelect={true}
                      options={expertiseOptions}
                      name="proficiency"
                      onChange={(event) =>
                        handleSkillForm(
                          event.map((filter) => filter.value),
                          index,
                          'proficiency'
                        )
                      }
                      value={
                        form.proficiency
                          ? form.proficiency.map((proficiencyValue) =>
                              expertiseOptions.find((option) => option.value == proficiencyValue)
                            )
                          : null
                      }
                    />
                  </div>
                  <div className="flex w-full mt-4">
                    <div id="experience" className="w-4/5">
                      <Typography variant="requestHeading" sx={{ marginBottom: '8px' }}>
                        Experience(Y)
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
                          disabled={!form.skillId}
                        />
                      </Box>
                    </div>
                    <div className="w-1/5  flex justify-center items-center">
                      <Tooltip title={'Delete'}>
                        <span>
                          <IconButton
                            onClick={() => removeFields(index)}
                            size="small"
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
                            <IconButton disabled={!form.skillId} onClick={addFields}>
                              <Iconify
                                icon="solar:add-square-broken"
                                sx={{
                                  color: (theme) =>
                                    form.skillId
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
          <Stack alignItems="flex-end" sx={{ mt: 3 }}>
            <Box sx={{ display: 'flex' }}>
              <Button
                sx={{ mr: 2 }}
                variant="outlined"
                color="inherit"
                onClick={() => {
                  onReset();
                }}
              >
                Reset
              </Button>
              <Button
                disabled={!isButtonEnabled(technologyWiseAllocationRequestQuery)}
                type="submit"
                variant="contained"
                color="inherit"
                startIcon={<Iconify icon="streamline:send-email-solid" />}
              >
                Request
              </Button>
            </Box>
          </Stack>
        </form>
      </Form>
    </Grid>
  );
};

export default TechnologyWiseAllocationForm;
