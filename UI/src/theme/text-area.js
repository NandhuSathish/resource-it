import { TextareaAutosize as BaseTextareaAutosize } from '@mui/base/TextareaAutosize';
import { styled } from '@mui/system';

const blue = {
  100: '#DAECFF',
  200: '#b6daff',
  400: '#3399FF',
  500: '#007FFF',
  600: '#0072E5',
  900: '#003A75',
};

const grey = {
  50: '#F3F6F9',
  100: '#E5EAF2',
  200: '#DAE2ED',
  300: '#C7D0DD',
  400: '#B0B8C4',
  500: '#9DA8B7',
  600: '#6B7A90',
  700: '#434D5B',
  800: '#303740',
  900: '#1C2025',
};

export const Textarea = styled(BaseTextareaAutosize)(
  ({ theme }) => `
    width: 100%;
    max-width: 100%; 
    min-width: 100%; 
    height: 100px;
    min-height: 100px; 
    max-height: 100px; 
    padding: 8px 12px;
    border-radius: 8px;
    color: ${theme.palette.mode === 'dark' ? grey[300] : grey[900]};
    background: ${theme.palette.mode === 'dark' ? grey[900] : '#fff'};
    border: 1px solid ${theme.palette.mode === 'dark' ? grey[700] : grey[200]};
    box-shadow: 0px 2px 2px ${theme.palette.mode === 'dark' ? grey[900] : grey[50]};
    font-family: 'Public Sans', sans-serif; 
    font-weight: 400;
    letter-spacing: 0.00938em;
    


    &:hover {
      border: 1px solid ${grey[600]};
    }

    &:focus {
      border: 1px solid ${theme.palette.mode === 'dark' ? grey[700] : grey[200]};
      border-color: ${blue[500]};
    }

    // firefox
    &:focus-visible {
      outline: 0;
    }
  `
);
