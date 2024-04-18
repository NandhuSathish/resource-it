/* eslint-disable react/react-in-jsx-scope */
import { useState } from 'react';
import { Box, Avatar, Divider, Popover, MenuItem, Typography, IconButton } from '@mui/material';
import { alpha } from '@mui/material/styles';
import useAuth from 'src/hooks/use-auth';
import HolidayCalandarView from 'src/components/ui/sections/holiday-calandar/view/holidayCalendarView';
import DialogBox from 'src/components/dialog';
import ResourceImportView from 'src/core/resource/view/resource-import-view';
import SkillDataImportView from 'src/components/ui/sections/skill-data-import/view/skill-data';
import {
  confirmationButtonProps,
  cancelButtonProps,
  dialogProps,
} from 'src/utils/confirmationDialogProps';
import { useConfirm } from 'material-ui-confirm';

// ----------------------------------------------------------------------

// ----------------------------------------------------------------------

export default function AccountPopover() {
  const [open, setOpen] = useState(null);
  const [userImportOpen, setUserImportOpen] = useState(null);
  const [skillImportOpen, setSkillImportOpen] = useState(null);

  const confirm = useConfirm();

  const { logout, getUserDetails } = useAuth();
  const { email, username, pictureUrl } = getUserDetails();
  const { role: currentLoggedUser } = getUserDetails();

  const [dialogOpen, setDialogOpen] = useState(false);

  const handleDialogOpen = () => {
    setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setDialogOpen(false);
  };

  const handleUserImportDialogOpen = () => {
    setUserImportOpen(true);
  };

  const handleUserImportDialogClose = () => {
    setUserImportOpen(false);
  };

  const handleSkillDataImportDialogOpen = () => {
    setSkillImportOpen(true);
  };

  const handleSkillDataImportDialogClose = () => {
    setSkillImportOpen(false);
  };

  const handleLogoutClick = () => {
    confirm({
      title: (
        <Typography variant="h6" style={{ fontSize: '18px' }}>
          Confirmation
        </Typography>
      ),
      description: (
        <Typography variant="body1" style={{ fontSize: '16px' }}>
          Are you sure you want to proceed?
        </Typography>
      ),
      confirmationButtonProps: {
        ...confirmationButtonProps,
      },
      cancelButtonProps: { ...cancelButtonProps },
      dialogProps: {
        ...dialogProps,
      },
      //   onConfirm: handleConfirm,
    })
      .then(() => {
        logout();
      })
      .catch(() => {});
  };

  const calendarComponent = {
    content: (
      <div>
        <HolidayCalandarView />
      </div>
    ),
    name: 'Holiday Calendar',
    width: '560px',
  };
  const resourseComponent = {
    content: (
      <div>
        <ResourceImportView />
      </div>
    ),
    name: ' Resource Import',
    width: '780px',
  };

  const skillDataComponent = {
    content: (
      <div>
        <SkillDataImportView />
      </div>
    ),
    name: ' Resource Skill Import',
    width: '700px',
  };

  const handleOpen = (event) => {
    setOpen(event.currentTarget);
  };

  const handleClose = () => {
    setOpen(null);
  };

  return (
    <>
      <IconButton
        onClick={handleOpen}
        sx={{
          width: 40,
          height: 40,
          background: (theme) => alpha(theme.palette.grey[500], 0.08),
          ...(open && {
            background: (theme) =>
              `linear-gradient(135deg, ${theme.palette.primary.light} 0%, ${theme.palette.primary.main} 100%)`,
          }),
        }}
      >
        <Avatar
          src={pictureUrl}
          alt={username}
          sx={{
            width: 36,
            height: 36,
            border: (theme) => `solid 2px ${theme.palette.background.default}`,
          }}
        >
          {username?.charAt(0).toUpperCase()}
        </Avatar>
      </IconButton>

      <Popover
        open={!!open}
        anchorEl={open}
        onClose={handleClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
        PaperProps={{
          sx: {
            p: 0,
            mt: 1,
            ml: 0.75,
            width: 200,
          },
        }}
      >
        <Box sx={{ my: 1.5, px: 2 }}>
          <Typography variant="subtitle2" noWrap>
            {username}
          </Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary' }} noWrap>
            {email}
          </Typography>
        </Box>

        <Divider sx={{ borderStyle: 'dashed' }} />
        {[1, 2, 3, 6].includes(currentLoggedUser) && (
          <MenuItem style={{ width: '100%' }}>
            {
              <Typography
                onClick={handleDialogOpen}
                variant="body2"
                sx={{ color: 'text.secondary', width: '100%' }}
                noWrap
              >
                Holiday Calendar
              </Typography>
            }

            <DialogBox
              content={calendarComponent}
              open={dialogOpen}
              handleClose={handleDialogClose}
            />
          </MenuItem>
        )}

        {[1, 2, 3, 6].includes(currentLoggedUser) && (
          <MenuItem style={{ width: '100%' }}>
            <Typography
              onClick={handleUserImportDialogOpen}
              variant="body2"
              sx={{ color: 'text.secondary', width: '100%' }}
              noWrap
            >
              Resource Import
            </Typography>
            <DialogBox
              content={resourseComponent}
              open={userImportOpen}
              handleClose={handleUserImportDialogClose}
            />
          </MenuItem>
        )}

        {[8].includes(currentLoggedUser) && (
          <MenuItem style={{ width: '100%' }}>
            <Typography
              onClick={handleSkillDataImportDialogOpen}
              variant="body2"
              sx={{ color: 'text.secondary', width: '100%' }}
              noWrap
            >
              Resource Skill Import
            </Typography>
            <DialogBox
              content={skillDataComponent}
              open={skillImportOpen}
              handleClose={handleSkillDataImportDialogClose}
            />
          </MenuItem>
        )}

        <Divider sx={{ borderStyle: 'dashed', m: 0 }} />

        <MenuItem
          disableRipple
          disableTouchRipple
          onClick={handleLogoutClick}
          sx={{ typography: 'body2', color: 'error.main', py: 1.5 }}
        >
          Logout
        </MenuItem>
      </Popover>
    </>
  );
}
