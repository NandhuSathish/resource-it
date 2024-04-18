import React from 'react';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import PropTypes from 'prop-types';
import { Typography } from '@mui/material';

export default function DialogBox({ content, open, handleClose, showActions = true }) {
  const children = content?.content;

  const DialogStyle = styled(Dialog)(({ theme }) => ({
    '& .MuiDialogContent-root': {
      padding: theme.spacing(5),
    },
    '& .MuiDialogActions-root': {
      padding: theme.spacing(2),
    },
    '& .MuiDialog-container': {
      '& .MuiPaper-root': {
        width: '100%',
        maxWidth: content.width,
      },
    },
  }));

  if (!content) {
    return null;
  }

  return (
    <DialogStyle onClose={handleClose} aria-labelledby="customized-dialog-title" open={open}>
      <DialogTitle>
        <Typography variant="h4"> {content.name}</Typography>
        <IconButton
          aria-label="close"
          onClick={handleClose}
          sx={{
            position: 'absolute',
            right: 8,
            top: 8,
            color: (theme) => theme.palette.grey[500],
          }}
        >
          <CloseIcon />
        </IconButton>
      </DialogTitle>
      <DialogContent sx={{ padding: 0 }} dividers>
        {children}
      </DialogContent>
      {showActions && <DialogActions></DialogActions>}
    </DialogStyle>
  );
}
DialogBox.propTypes = {
  content: PropTypes.shape({
    content: PropTypes.node.isRequired,
    name: PropTypes.string.isRequired,
    width: PropTypes.string.isRequired,
  }).isRequired,
  open: PropTypes.bool.isRequired,
  handleClose: PropTypes.func.isRequired,
  showActions: PropTypes.bool,
};
