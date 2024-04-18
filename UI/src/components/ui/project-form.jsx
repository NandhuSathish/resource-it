/* eslint-disable react/react-in-jsx-scope */
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import * as z from 'zod';
import { OutlinedInput, Typography } from '@mui/material';
import Select from 'react-select';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { mapToSrtingOptions, stringifyNumbers, convertDate, getSkillIds } from 'src/utils/utils';
import { projectFormType } from 'src/utils/constants';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from './form';

import useProjects from 'src/hooks/use-projects';
import useSkill from 'src/hooks/use-get-skill';
import { toast } from 'sonner';
import useManager from 'src/hooks/use-manager';
import { Textarea } from 'src/theme/text-area';
import { useEffect, useState } from 'react';
import dayjs from 'dayjs';
import useAuth from 'src/hooks/use-auth';
import { useConfirm } from 'material-ui-confirm';
import {
  confirmationButtonProps,
  cancelButtonProps,
  dialogProps,
} from 'src/utils/confirmationDialogProps';
import { errorCodeMap } from 'src/utils/error-codes';
import { LoadingButton } from '@mui/lab';
import { useNavigate } from 'react-router-dom';

import customParseFormat from 'dayjs/plugin/customParseFormat';
dayjs.extend(customParseFormat);

//#styles
const selectStyles = {
  height: '42px',
  overflow: 'auto',
  borderRadius: 9,
  border: '0.5px solid #b8b8b854',
};

export function ProjectForm(projectProps) {
  const [startDateFlag, setStartDateFlag] = useState(
    projectProps.formMode == 'edit' ? dayjs(convertDate(projectProps.initialValues.startDate)) : ''
  );
  const [isLoading, setIsLoading] = useState(false);

  const [disableButton, setDisableButton] = useState(false);
  const navigate = useNavigate();

  const [endDateFlag, setEndDateFlag] = useState(projectProps.initialValues.endDate);
  const [renderKey, setRenderKey] = useState(0);

  const { getUserDetails } = useAuth();
  const { role: currentLoggedUser } = getUserDetails();
  const confirm = useConfirm();
  const FormSchema = z
    .object({
      projectRequestId: z.string().optional(),

      projectId: z.string().optional(),

      approvalStatus: z.string().optional(),

      projectCode: z
        .string()
        .min(1, { message: 'Required' })
        .max(1000, { message: 'Maximum length is 1000 characters' })
        .refine((value) => /^[a-zA-Z0-9_]*$/.test(value), {
          message: 'Invalid Project Code',
        })
        .refine((value) => value !== '0'.repeat(value.length), {
          message: 'Project code cannot be all zeros',
        }),

      name: z
        .string()
        .min(1, { message: 'Required' })
        .max(1000, { message: 'Maximum length is 1000 characters' }),

      teamSize:
        projectProps.formMode === 'edit'
          ? z.string().min(1, { message: 'Required' })
          : z.string().optional(),

      manDay:
        z.string()
              .optional()
              .refine(
                (value) =>
                  !value ||
                  (value !== '0'.repeat(value.length) &&
                    /^\d+$/.test(value) &&
                    value.length >= 1 &&
                    value.length <= 24),
                { message: 'Estimated man days must be a number greater than zero' }
              ),

      clientName: z
        .string()
        .min(1, { message: 'Required' })
        .min(3, { message: 'Client name must be at least 3 characters' })
        .max(24, { message: 'Client name must not be more than 24 characters' }),

      startDate:
        projectProps.formMode == 'add' || projectProps.formMode == 'approve'
          ? z.any()
          : projectProps.initialValues.startDate
            ? z.any()
            : z.date(),

      endDate:
        projectProps.formMode == 'add' || projectProps.formMode == 'approve'
          ? z.any()
          : projectProps.initialValues.endDate
            ? z.any()
            : z.date(),

      projectType: z.string().min(1, { message: 'Required' }),

      description: z
        .string()
        .optional()
        .refine((value) => !value || (value.length >= 3 && value.length <= 200), {
          message: 'Description must be between 3 and 200 characters',
        }),

      managerId:
        projectProps.currentRole === 2
          ? z.string().min(1, { message: 'Required' })
          : z.string().optional(),

      skillIds:
        projectProps.formMode === 'edit'
          ? z.array(z.string().min(1)).nonempty({ message: 'Required' })
          : z.any().optional(),
      status: z.string().optional(),
    })

    .refine((data) => (data.startDate ? data.endDate !== undefined : true), {
      message: 'Required',
      path: ['endDate'],
    })
    .refine((data) => (data.endDate ? data.startDate !== undefined : true), {
      message: 'Required',
      path: ['startDate'],
    });

  const { addProject } = useProjects();
  const { approveProject } = useProjects();
  const { editProject } = useProjects();

  let managerOptions = [];
  const { data: managers } = useManager();
  if (managers) {
    managerOptions = mapToSrtingOptions(managers, 'id', 'name');
  }

  //skill option
  let skillOptions = [];
  const { data: skillData } = useSkill();
  if (skillData) {
    skillOptions = mapToSrtingOptions(skillData, 'id', 'name');
  }


  //projectType option
  let projectTypeOptions = [];
  projectTypeOptions = mapToSrtingOptions(projectFormType, 'id', 'name');
  const projectData = stringifyNumbers(projectProps.initialValues);
  projectData.startDate = projectData.startDate ? convertDate(projectData.startDate) : undefined;
  projectData.endDate = projectData.endDate ? convertDate(projectData.endDate) : undefined;
  projectData.managerId = projectData.manager ? projectData.manager.id : '';
  projectData.skillIds = projectData.skill ? getSkillIds(projectData.skill) : '';
  projectData.approvalStatus = '1';
  const [currentEndDate, setCurrentEndDate] = useState(projectData.endDate);
  projectData.manDay = projectData.manDay ? projectData.manDay : '';
  const form = useForm({
    resolver: zodResolver(FormSchema),
    defaultValues: projectProps.isEdit
      ? projectData
      : {
          projectId: '',
          projectCode: '',
          name: '',
          teamSize: '',
          manDay: '',
          clientName: '',
          projectType: '',
          description: '',
          managerId: '',
          skillIds: [],
        },
    reValidateMode: 'onChange',
  });

  //state to keep button disabled if form has no changes
  const [defaultFormValues, setDefaultFormValues] = useState(JSON.stringify(form.getValues()));

  const onEditProject = (formData, projectProps) => {
    editProject.mutate(formData, {
      onSuccess: (data) => {
        setIsLoading(false);
        if (data.successCode == 1302) {
          setDisableButton(true);
        }
        toast.success(errorCodeMap[data.successCode] || 'Sucesss');
        navigate('/projectManagement/projectList');
        projectProps.handleClose();
      },
      onError: (error) => {
        setIsLoading(false);
        if (error.response.data.errorCode == 1341) {
          toast.warning('Provided project name already exist');
        }
      },
    });
  };

  const onSubmit = (formData) => {
let newFormData;
let newStartDate; 
let newEndDate;
if(formData.startDate == null )
{
  newStartDate=null
  newEndDate=null
}
else{
  newStartDate = dayjs(formData.startDate);
  newEndDate = dayjs(formData.endDate);
}


newFormData = {
  ...formData,
  startDate: newStartDate == null ? null : dayjs(newStartDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
  endDate: newEndDate == null ? null : dayjs(newEndDate, 'DD-MM-YYYY').format('YYYY-MM-DD'),
};


    setIsLoading(true);
    ////////////////////////////////////////////////////////add
    if (projectProps.formMode == 'add') {
      addProject.mutate(newFormData, {
        onSuccess: (data) => {
          if (data) {
            setIsLoading(false);
            toast.success('Project request sent');
            form.reset();
            form.setValue('startDate', null);
            form.setValue('endDate', null);
            setRenderKey((prevKey) => prevKey + 1);
          }
        },
        onError: (error) => {
          setIsLoading(false);
          if (error.response.data.errorCode == 1341) {
            toast.warning('Provided project name already exist');
          }
        },
      });
    }

    ////////////////////////////////////////////////////////edit
    else if (projectProps.formMode == 'edit') {
      if (
        (projectData.startDate && !projectData.startDate?.isSame(formData.startDate)) ||
        (projectData.endDate && !projectData.endDate?.isSame(formData.endDate))
      ) {
        confirm({
          title: (
            <Typography variant="h6" style={{ fontSize: '18px' }}>
              Confirmation
            </Typography>
          ),
          description: (
            <Typography variant="body1" style={{ fontSize: '16px' }}>
              *This may result in the removal of allocations inside the project and allocation
              requests.
            </Typography>
          ),
          confirmationButtonProps: {
            ...confirmationButtonProps,
          },
          cancelButtonProps: { ...cancelButtonProps },
          dialogProps: {
            ...dialogProps,
          },
        })
          .then(() => {
            onEditProject(newFormData, projectProps);
          })
          .catch(() => {
             setIsLoading(false);
          });
      } else {
        onEditProject(newFormData, projectProps);
      }
    }

    ////////////////////////////////////////////////////////approve
    else if (projectProps.formMode == 'approve') {
      approveProject.mutate(newFormData, {
        onSuccess: (data) => {
          setIsLoading(false);
          projectProps.handleClose();
          toast.success(errorCodeMap[data.successCode]);
        },
        onError: (error) => {
          setIsLoading(false);
          toast.error(errorCodeMap[error.response.data.errorCode] || 'Something went wrong');
        },
      });
    }
  };
  useEffect(() => {
    setDefaultFormValues(JSON.stringify(form.getValues()));
  }, []);

  return (
    <div>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6 my-4 mx-6 ">
          <div className="grid grid-cols-2 gap-6 w-full">
            <FormField
              control={form.control}
              name="projectCode"
              render={({ field }) => (
                <FormItem
                  className="flex flex-col "
                  style={{ pointerEvents: projectProps.currentRole !== 3 ? 'none' : 'auto' }}
                >
                  <FormLabel>
                    Project Code
                    {currentLoggedUser === 3 && <span style={{ color: 'red' }}>*</span>}
                  </FormLabel>
                  <FormControl>
                    <OutlinedInput
                      sx={{
                        backgroundColor: projectProps.currentRole !== 3 ? '#ededed' : 'white',
                      }}
                      placeholder="Project Code"
                      value={field.value.trim()}
                      onChange={(event) => {
                        field.onChange(event.target.value.trim());
                      }}
                      disabled={projectProps.currentRole !== 3}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="projectType"
              render={({ field }) => (
                <FormItem
                  className="flex flex-col "
                  style={{
                    pointerEvents: projectProps.currentRole !== 3 ? 'none' : 'auto',
                    cursor: projectProps.currentRole !== 3 ? 'text' : 'auto',
                  }}
                >
                  <FormLabel>
                    Project Type
                    {currentLoggedUser === 3 && <span style={{ color: 'red' }}>*</span>}
                  </FormLabel>
                  <FormControl>
                    <Select
                      {...field}
                      key={renderKey}
                      styles={{
                        control: (provided) => ({
                          ...provided,
                          backgroundColor: projectProps.currentRole !== 3 ? '#ededed' : 'white',
                          ...selectStyles,
                        }),
                      }}
                      value={projectTypeOptions.find((option) => option.value === field.value)}
                      isDisabled={projectProps.currentRole !== 3}
                      onChange={(selectedOption) => field.onChange(selectedOption.value)}
                      closeMenuOnSelect={true}
                      options={projectTypeOptions}
                    ></Select>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-1 w-full">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem
                  className="flex flex-col "
                  style={{ pointerEvents: projectProps.currentRole !== 3 ? 'none' : 'auto' }}
                >
                  <div>
                    <FormLabel>
                      Project Name
                      {currentLoggedUser === 3 && <span style={{ color: 'red' }}>*</span>}
                    </FormLabel>
                  </div>
                  <FormControl>
                    <OutlinedInput
                      sx={{
                        backgroundColor: projectProps.currentRole !== 3 ? '#ededed' : 'white',
                      }}
                      placeholder="Name"
                      {...field}
                      disabled={projectProps.currentRole !== 3}
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
              name="clientName"
              render={({ field }) => (
                <FormItem
                  className="flex flex-col "
                  style={{ pointerEvents: projectProps.currentRole !== 3 ? 'none' : 'auto' }}
                >
                  <FormLabel>
                    Client Name
                    {currentLoggedUser === 3 && <span style={{ color: 'red' }}>*</span>}
                  </FormLabel>
                  <FormControl>
                    <OutlinedInput
                      sx={{
                        backgroundColor: projectProps.currentRole !== 3 ? '#ededed' : 'white',
                      }}
                      placeholder="Client Name"
                      inputProps={{
                        maxLength: 24,
                      }}
                      {...field}
                      disabled={projectProps.currentRole !== 3}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="managerId"
              render={({ field }) => (
                <FormItem
                  className="flex flex-col "
                  style={{
                    pointerEvents: projectProps.currentRole !== 2 ? 'none' : 'auto',

                    cursor: projectProps.currentRole !== 2 ? 'not-allowed' : 'auto',
                  }}
                >
                  <FormLabel>
                    Project Manager
                    {currentLoggedUser === 2 && <span style={{ color: 'red' }}>*</span>}
                  </FormLabel>
                  <Select
                    {...field}
                    onKeyDown={(event) => {
                      if (projectProps.currentRole !== 2 && event.key === ' ') {
                        event.preventDefault();
                      }
                    }}
                    styles={{
                      control: (provided) => ({
                        ...provided,
                        backgroundColor: projectProps.currentRole !== 2 ? '#ededed' : 'white',
                        ...selectStyles,
                      }),
                    }}
                    isDisabled={projectProps.currentRole !== 2}
                    value={managerOptions.find((option) => option.value == field.value)}
                    onChange={(selectedOption) => {
                      field.onChange(selectedOption.value);
                    }}
                    closeMenuOnSelect={true}
                    options={managerOptions}
                  ></Select>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-2 gap-6 w-full">
            <FormField
              control={form.control}
              name="manDay"
              render={({ field }) => (
                <FormItem className="flex flex-col ">
                  <FormLabel>Estimated Man Days</FormLabel>
                  <FormControl>
                    <OutlinedInput
                      inputProps={{
                        inputMode: 'numeric',
                        maxLength: 24,
                        onKeyPress: (event) => {
                          const { value } = event.target;
                          if (!/\d/.test(event.key) || (value.length === 0 && event.key === '0')) {
                            event.preventDefault();
                          }
                        },
                      }}
                      placeholder="Estimated Man Days "
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="skillIds"
              render={({ field }) => (
                <FormItem className="flex flex-col ">
                  <FormLabel>
                    Technology
                    {projectProps.formMode === 'edit' && <span style={{ color: 'red' }}>*</span>}
                  </FormLabel>
                  <Select
                    isMulti
                    {...field}
                    styles={{
                      control: (provided) => ({
                        ...provided,
                        ...selectStyles,
                      }),
                    }}
                    value={skillOptions.filter(
                      (option) => field.value && field.value.includes(option.value)
                    )}
                    onChange={(selectedOptions) =>
                      field.onChange(selectedOptions.map((option) => option.value))
                    }
                    closeMenuOnSelect={false}
                    options={skillOptions}
                  />
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-2 gap-6 w-full">
            <FormField
              control={form.control}
              name="startDate"
              render={({ field }) => (
                <FormItem className="flex flex-col ">
                  <FormLabel className="mb-[6px]">
                    Start Date
                    {projectProps.formMode === 'edit' && <span style={{ color: 'red' }}>*</span>}
                  </FormLabel>
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DatePicker
                      {...field}
                      format="DD/MM/YYYY"
                      value={field.value}
                      maxDate={currentEndDate}
                      slotProps={{
                        textField: {
                          readOnly: true,
                        },
                      }}
                      onChange={(newValue) => {
                        field.onChange(newValue.toDate());
                        setStartDateFlag(newValue);
                        setEndDateFlag(newValue);
                      }}
                    />
                  </LocalizationProvider>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="endDate"
              render={({ field }) => (
                <FormItem className="flex flex-col ">
                  <FormLabel className="mb-[6px]">
                    End Date
                    {projectProps.formMode === 'edit' && <span style={{ color: 'red' }}>*</span>}
                  </FormLabel>
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DatePicker
                      {...field}
                      format="DD/MM/YYYY"
                      value={field.value}
                      slotProps={{
                        textField: {
                          readOnly: true,
                        },
                      }}
                      minDate={startDateFlag || projectData.startDate}
                      onChange={(newValue) => {
                        field.onChange(newValue.toDate());
                        setCurrentEndDate(newValue);
                      }}
                      disabled={
                        (projectProps.formMode == 'add' && startDateFlag === '') ||
                        ((projectProps.formMode == 'approve' || projectProps.formMode == 'edit') &&
                          !endDateFlag)
                      }
                    />
                  </LocalizationProvider>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-1 w-full">
            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem className="flex flex-col ">
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <Textarea maxLength={200} minRows={4} {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="flex justify-end">
            <LoadingButton
              loading={isLoading}
              disabled={
                disableButton == true ||
                (projectProps.formMode === 'edit'
                  ? defaultFormValues === JSON.stringify(form.getValues())
                  : false)
              }
              loadingPosition="center"
              sx={{
                marginRight: 2,
              }}
              variant="contained"
              color="inherit"
              className="px-12  h-10"
              type="submit"
            >
              Submit
            </LoadingButton>
          </div>
        </form>
      </Form>
    </div>
  );
}
