/* eslint-disable react/react-in-jsx-scope */
import { useEffect, useState } from 'react';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import * as z from 'zod';
import { IconButton, OutlinedInput, Tooltip } from '@mui/material';
import Select from 'react-select';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { mapToSrtingOptions, stringifyNumbers } from 'src/utils/utils';

import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '../ui/form';
import addResource from 'src/hooks/use-add-resource';
import {expertise} from 'src/utils/constants';
import useResources from 'src/hooks/useResources';
import useRole from 'src/hooks/use-role';
import useSkill from 'src/hooks/use-get-skill';
import useDepartment from 'src/hooks/use-department';
import dayjs from 'dayjs';
import { toast } from 'sonner';
import useAuth from 'src/hooks/use-auth';
import Iconify from '../iconify';
import { LoadingButton } from '@mui/lab';
import { useNavigate } from 'react-router-dom';
import customParseFormat from 'dayjs/plugin/customParseFormat';
dayjs.extend(customParseFormat);

// #styles
const selectStyles = {
  height: '42px',
  overflow: 'auto',
  borderRadius: 9,
  border: '0.5px solid #b8b8b854',
};

const skillSelectStyles = {
  height: '40px',
  width:'100%',
  overflow: 'auto',
  borderRadius: 8,
  border: '0.5px solid #b8b8b854',
};

export function AddResourceForm(resourceProps) {
  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  const [isLoading, setIsLoading] = useState(false);
  const [renderKey, setRenderKey] = useState(0);
  const navigate = useNavigate();

  const FormSchema = z.object({
    id: z.string().optional(),
    employeeId: z
      .string()
      .min(1, { message: 'Required' })
      .min(4, { message: 'Employee Id must be at least 4 numbers' })
      .max(8, { message: 'Employee Id must be at most 8 numbers' })
      .refine((value) => /^\d+$/.test(value), {
        message: 'Employee Id must be numbers',
      })
      .refine((value) => !/^0+$/.test(value), {
        message: 'Employee Id cannot be all zeros',
      }),
    name: z
      .string()
      .min(1, { message: 'Required' })
      .min(3, { message: 'Name must be at least 3 characters' })
      .max(24, { message: 'Name must not be more than 24 characters' })
      .refine((name) => /^[a-zA-Z ]*$/.test(name), {
        message: 'Name should only contain alphabets',
      }),
    email: z
      .string()
      .min(4, { message: 'Required' })
      .refine((value) => /\S+@\S+\.\S+/.test(value), { message: 'Invalid email address' }),
    joiningDate: resourceProps.isEdit ? z.any() : z.date({ required_error: 'Required' }),
    departmentId: z.string().refine((value) => value !== '', { message: 'Required' }),
    role: z.string().refine((value) => value !== '', { message: 'Required' }),
    experience: z
      .string()
      .min(1, { message: 'Required' })
      .refine((value) => /^\d+$/.test(value), {
        message: 'Experience must be a number',
      }),
    skills: z.array(
      z.object({
        skillId: z.string(),
        proficiency: z.string(),
        experience: z
          .string()
          .min(1, { message: 'Experience is required' })
          .refine((value) => /^\d+$/.test(value), {
            message: 'Experience must be a number',
          }),
      })
    ),
  });

  const { editResource } = useResources();

  const setData = addResource();
  let roleOptions = [];
  const { data: roles } = useRole();
  if (roles) {
    roleOptions = mapToSrtingOptions(roles, 'id', 'role');
  }

  //skill option

  const { data: skillData } = useSkill();
  const [skillDisabled, setSkillDisabled] = useState(false);
  let allSkillOption = [];
  if (skillData) {
    allSkillOption = mapToSrtingOptions(skillData, 'id', 'name');
  }

  const [skillOptions, setSkillOptions] = useState({});

  // geting the department options
  let departmentOptions = [];
  const { data: departments } = useDepartment();

  if (departments) {
    departmentOptions = mapToSrtingOptions(departments, 'departmentId', 'name');
  }
  let resourceData = [];
  resourceData = stringifyNumbers(resourceProps.initialValues);

  useEffect(() => {
    setDepartmentFlag();
  }, [resourceData.departmentId]);

  const parts =
    typeof resourceData.joiningDate === 'string'
      ? resourceData.joiningDate.split('-')
      : ['01', '01', '1970'];

  const date = new Date(parts[2], parts[1] - 1, parts[0]);

  resourceData.joiningDate = dayjs(date);

  resourceData.skills = resourceData.skills?.map((skill) => ({
    ...skill,
    id: Math.random(), // or some unique value
  }));

  const form = useForm({
    resolver: zodResolver(FormSchema),
    defaultValues: resourceProps.isEdit
      ? resourceData
      : {
          employeeId: '',
          name: '',
          email: '',
          departmentId: '',
          role: '',
          experience: '',
          skills: [],
        },
    reValidateMode: 'onChange',
  });
  const expertiseOptions = mapToSrtingOptions(expertise, 'id', 'name');
  const [skills, setskills] = useState(resourceData.skills || form.getValues('skills'));
  const [localSkills, setLocalSkills] = useState(resourceData.skills);

  const setDepartmentFlag = () => {
    setSkillOptions(() => {
      let newOptions = mapToSrtingOptions(skillData, 'id', 'name');
      setSkillOptions(newOptions);

      filterAvailableOptions(newOptions);
      return newOptions;
    });
  };

  useEffect(() => {
    setDepartmentFlag();
  }, [skillData]);

  const resetForm = () => {
    setRenderKey((prevKey) => prevKey + 1);
  };

  const [availableOptions, setAvailableOptions] = useState(skillOptions);
  // dayjs(formattedStartDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),

  const onSubmit = async (formData) => {
    setIsLoading(true);
    let newFormData;
    let newJoiningDate = dayjs(formData.joiningDate)
    newFormData = {
      ...formData,
      joiningDate: dayjs(newJoiningDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
    };
    console.log(newFormData, '');
    if (!resourceProps.isEdit) {
      await setData.mutate(newFormData, {
        onSuccess: (data) => {
          if (data) {
            form.reset();
            toast.success('Resource added successfully');
            setIsLoading(false);
            form.reset({
              employeeId: '',
              name: '',
              email: '',
              joiningDate: null,
              departmentId: '',
              role: '',
              experience: '',
              skills: [],
            });
            setskills([]);
            form.reset();
            resetForm();
            resourceProps.handleFetch();
          }
        },
        onError: (error) => {
          if(error.response.data.errorCode == 1054) {
            toast.warning('Provided email already exist');
            setIsLoading(false);
          } else if (error.response.data.errorCode == 2006) {
            toast.warning('Provided Employee Id already exist');
            setIsLoading(false);
          }
        },
      });
    } else if (resourceProps.isEdit) {
      await editResource.mutate(newFormData, {
        onSuccess: () => {
          toast.success('Resource updated successfully');
          navigate('/resourceManagement/resourceList');
          setIsLoading(false);
          resourceProps.handleFetch();
        },
        onError: (error) => {
          if (error.response.data.errorCode == 2006) {
            setIsLoading(false);
            toast.warning('Provided Employee Id already exist');
          } else if (error.response.data.errorCode == 1054) {
            setIsLoading(false);
            toast.warning('Provided email already exist');
          } else if (error.response.data.errorCode == 2117) {
            setIsLoading(false);
            toast.warning('Unable to update role, as the allocation exists for the resource');
          } else if (error.response.data.errorCode == 2118) {
            setIsLoading(false);
            toast.warning(
              'Unable to update joining date, as the allocation exists for the resource'
            );
          }
        },
      });
    }
  };

  const addDynamicField = () => {
    if(skillData.length -1  === skills.length){
    setSkillDisabled(true);
      
    }
    console.log(availableOptions,'jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj');
    setDepartmentFlag();
    const newSkill = { id: Math.random() };
    setskills([...skills, newSkill]);
    const updatedFormValues = [...form.getValues('skills'), newSkill];
    form.reset({ ...form.getValues(), skills: updatedFormValues });
    filterAvailableOptions(skillOptions);
  };

  const removeDynamicField = (id, index) => {
    if (skillData.length -1 !== skills.length) {
      setSkillDisabled(false);
    }

    if (!resourceProps.isEdit) {
      const currentSkills = form.getValues('skills');
      const updatedSkills = currentSkills.filter((_, i) => i !== index);
      // Update the form state with the new array
      form.setValue('skills', updatedSkills);
      setskills(skills.filter((skill) => skill.id !== id));
      filterAvailableOptions(skillOptions);
    } else if (resourceProps.isEdit) {
      setskills((prevSkills) => {
        const updatedSkills = prevSkills.filter((skill) => skill.id !== id);
        const isDuplicate = updatedSkills.some((skill) => skill.id === id);
        if (isDuplicate) {
          // Remove the duplicate skill
          return updatedSkills.filter((skill) => skill.id !== id);
        } else {
          return updatedSkills;
        }
      });
      const updatedSkills = localSkills.filter((skill, i) => i !== index);
      setLocalSkills(updatedSkills);
      const currentFormValues = form.getValues('skills');
      const updatedFormValues = currentFormValues.filter((skill) => skill.id !== id);
      form.setValue('skills', updatedFormValues);
      filterAvailableOptions(skillOptions);
    }
  };

  const filterAvailableOptions = (newOptions) => {
    const skillIds = form
      .getValues('skills')
      .map((skill) => skill.skillId)
      .flat();
    const filteredOptions = newOptions?.filter((option) => !skillIds.includes(option.value));
    setAvailableOptions(filteredOptions);
  };

  const [defaultFormValues, setDefaultFormValues] = useState(JSON.stringify(form.getValues()));

  useEffect(() => {
    setDefaultFormValues(JSON.stringify(form.getValues()));
  }, []);


  return (
    <div>
      <Form {...form}>
        <form
          key={renderKey}
          onSubmit={form.handleSubmit(onSubmit)}
          className="space-y-6 my-4 mx-8"
        >
          <div className="grid grid-cols-2 gap-6 w-full">
            <FormField
              control={form.control}
              name="employeeId"
              render={({ field }) => (
                <FormItem className="flex flex-col ">
                  <FormLabel>
                    Employee Id<span style={{ color: 'red' }}>*</span>
                  </FormLabel>
                  <FormControl>
                    <OutlinedInput
                      key={renderKey}
                      placeholder="Employee Id"
                      {...field}
                      onChange={(event) => {
                        field.onChange(event.target.value.trim());
                      }}
                      inputProps={{
                        maxLength: 8,
                        inputMode: 'numeric',
                        onKeyPress: (event) => {
                          if (!/\d/.test(event.key)) {
                            event.preventDefault();
                          }
                        },
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="departmentId"
              render={({ field }) => (
                <FormItem className="flex flex-col ">
                  <FormLabel>
                    Department<span style={{ color: 'red' }}>*</span>
                  </FormLabel>
                  <Select
                    {...field}
                    key={renderKey}
                    styles={{
                      control: (provided) => ({
                        ...provided,
                        ...skillSelectStyles,
                      }),
                      singleValue: (provided) => ({
                        ...provided,
                        fontSize: '16px', // Change this to your desired font size
                      }),
                    }}
                    value={departmentOptions.find((option) => option.value === field.value)}
                    onChange={(selectedOption) => {
                      field.onChange(selectedOption.value);
                      setDepartmentFlag();
                    }}
                    closeMenuOnSelect={true}
                    options={departmentOptions}
                  ></Select>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <div>
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>
                    Name<span style={{ color: 'red' }}>*</span>
                  </FormLabel>
                  <FormControl>
                    <OutlinedInput
                      placeholder="Name"
                      inputProps={{
                        maxLength: 24,
                      }}
                      {...field}
                      onKeyDown={(event) => {
                        if (!/[a-zA-Z\s]/.test(event.key)) {
                          event.preventDefault();
                        }
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <div className="grid grid-cols-2 gap-6 w-full">
            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem className="flex flex-col ">
                  <FormLabel>
                    Email Id<span style={{ color: 'red' }}>*</span>{' '}
                  </FormLabel>
                  <FormControl>
                    <OutlinedInput
                      placeholder="Email Id"
                      {...field}
                      onChange={(event) => {
                        field.onChange(event.target.value.trim());
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="role"
              render={({ field }) => (
                <FormItem
                  className="flex flex-col "
                  style={{
                    pointerEvents:
                      resourceProps.isEdit && currentLoggedUser !== 1 ? 'none' : 'auto',
                  }}
                >
                  <FormLabel>
                    Role<span style={{ color: 'red' }}>*</span>
                  </FormLabel>
                  <FormControl>
                    <Select
                      key={renderKey}
                      {...field}
                      styles={{
                        control: (provided) => ({
                          ...provided,
                          ...selectStyles,
                        }),
                        singleValue: (provided) => ({
                          ...provided,
                          fontSize: '16px', // Change this to your desired font size
                        }),
                      }}
                      value={roleOptions.find((option) => option.value === field.value)}
                      isDisabled={resourceProps.isEdit && currentLoggedUser !== 1}
                      onChange={(selectedOption) => field.onChange(selectedOption.value)}
                      closeMenuOnSelect={true}
                      options={roleOptions}
                    ></Select>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-2 gap-6 w-full">
            <FormField
              control={form.control}
              name="experience"
              render={({ field }) => (
                <FormItem className="flex flex-col ">
                  <FormLabel>
                    Previous Experience (Months)<span style={{ color: 'red' }}>*</span>{' '}
                  </FormLabel>
                  <FormControl>
                    <OutlinedInput
                      placeholder="Previous Experience"
                      {...field}
                      inputProps={{
                        inputMode: 'numeric',
                        maxLength: 4,
                        onKeyPress: (event) => {
                          if (!/\d/.test(event.key)) {
                            event.preventDefault();
                          }
                        },
                      }}
                      onChange={(event) => {
                        field.onChange(event.target.value.trim());
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="joiningDate"
              render={({ field }) => (
                <FormItem className="flex flex-col ">
                  <FormLabel className="mb-[px]">
                    Joining Date<span style={{ color: 'red' }}>*</span>
                  </FormLabel>
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DatePicker
                      {...field}
                      format="DD/MM/YYYY"
                      slotProps={{
                        textField: {
                          readOnly: true,
                        },
                      }}
                      value={field.value}
                      onChange={(newValue) => {
                        field.onChange(newValue.toDate());
                      }}
                    />
                  </LocalizationProvider>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className=" w-full ">
            <FormItem>
              {form.getValues('skills').length != [] && (
                <div className="w-full grid grid-cols-3 gap-4 pb-2">
                  <FormLabel>Skill</FormLabel>
                  <FormLabel>Experience</FormLabel>
                  <FormLabel>Proficiency</FormLabel>
                </div>
              )}
              {skills.map((skill, index) => {
                const experience = form.watch(`skills[${index}].experience`);
                let skillId = form.getValues(`skills[${index}].skillId`);

                return (
                  <div key={skill.id} className=" w-full grid grid-cols-3 gap-4 pb-4">
                    <FormField
                      control={form.control}
                      name={`skills[${index}].skillId`}
                      render={({ field }) => (
                        <div className="">
                          <Select
                            key={renderKey}
                            {...field}
                            styles={{
                              control: (provided) => ({
                                ...provided,
                                ...skillSelectStyles,
                              }),
                              singleValue: (provided) => ({
                                ...provided,
                                fontSize: '16px',
                              }),
                            }}
                            value={allSkillOption.find((option) => option.value === field.value)}
                            onChange={(selectedOption) => {
                              field.onChange(selectedOption.value);
                              filterAvailableOptions(skillOptions);
                              form.setValue(`skills[${index}].experience`, '');
                              form.setValue(`skills[${index}].proficiency`, null);
                              form.trigger(`skills[${index}].proficiency`);
                            }}
                            closeMenuOnSelect={true}
                            options={availableOptions}
                          ></Select>
                        </div>
                      )}
                    />

                    <div className="">
                      <FormField
                        control={form.control}
                        name={`skills[${index}].experience`}
                        render={({ field }) => (
                          <OutlinedInput
                            inputProps={{
                              inputMode: 'numeric',
                              maxLength: 4,
                              onKeyPress: (event) => {
                                if (!/\d/.test(event.key)) {
                                  event.preventDefault();
                                }
                              },
                            }}
                            key={renderKey}
                            placeholder="Experience (Months)"
                            {...field}
                            disabled={!form.getValues('skills')[index].skillId}
                            sx={{
                              width: '100%',
                              background: !form.getValues('skills')[index].skillId
                                ? '#ededed'
                                : '#ffff',
                            }}
                          />
                        )}
                      />
                    </div>
                    <div className="flex gap-3">
                      <div className="w-3/4">
                        <FormField
                          control={form.control}
                          key={`proficiency-${skillId}`}
                          name={`skills[${index}].proficiency`}
                          render={({ field }) => (
                            <Select
                              key={renderKey}
                              {...field}
                              styles={{
                                control: (provided) => ({
                                  ...provided,
                                  backgroundColor:
                                    !form.getValues('skills')[index].skillId ||
                                    !form.getValues('skills')[index].experience
                                      ? '#ededed'
                                      : '#ffff',
                                  ...skillSelectStyles,
                                }),
                                singleValue: (provided) => ({
                                  ...provided,
                                  fontSize: '16px',
                                }),
                              }}
                              value={expertiseOptions.find(
                                (option) => option.value === field.value
                              )}
                              isDisabled={!form.getValues('skills')[index].skillId || !experience}
                              valu
                              onChange={(selectedOption) => field.onChange(selectedOption.value)}
                              closeMenuOnSelect={true}
                              options={expertiseOptions}
                            ></Select>
                          )}
                        />
                      </div>
                      <div className="w-1/4 flex justify-center items-center text-gray-500 ">
                        <Tooltip title={'Delete'}>
                          <span>
                            <IconButton
                              onClick={() => removeDynamicField(skill.id, index)}
                              size="small"
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
                );
              })}
            </FormItem>
          </div>

          <div className="flex justify-center items-center text-gray-500">
            <Tooltip title={'Add Skill'}>
              <span>
                <IconButton onClick={addDynamicField} disabled={skillDisabled}>
                  <Iconify
                    icon="solar:add-square-broken"
                    sx={{
                      color: skillDisabled ? 'disabled' : 'primary.main',
                    }}
                  />
                </IconButton>
              </span>
            </Tooltip>
          </div>

          <div>
            <div className="flex justify-end">
              <LoadingButton
                loading={isLoading}
                disabled={
                  resourceProps.isEdit
                    ? defaultFormValues === JSON.stringify(form.getValues())
                    : false
                }
                loadingPosition="center"
                variant="contained"
                color="inherit"
                className="h-10 px-12"
                type="submit"
              >
                Submit
              </LoadingButton>
            </div>
          </div>
        </form>
      </Form>
    </div>
  );
}
