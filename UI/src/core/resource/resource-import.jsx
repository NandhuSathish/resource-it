import { Input } from 'src/components/ui/input';
import React, { useState } from 'react';
import useResourceImport from 'src/hooks/use-resource-import';
import { toast } from 'sonner';
import { Box } from '@mui/material';
import { LoadingButton } from '@mui/lab';

export function ResourceImport() {
  const getUpload = useResourceImport();
    const [boardedFile, setBoarded] = useState(false);
    const [renderKey, setRenderKey] = useState(0);
    const [isLoading, setIsLoading] = useState(false);


    const handleUploadClick = () => {
      setIsLoading(true);
      uploadFile();
    };

     const uploadFile = () => {
      getUpload.mutate(boardedFile, {
        onSuccess: () => {
       setRenderKey((prevKey) => prevKey + 1);   
             setIsLoading(false);

      setBoarded(null);     },
        onError: () => {
       setRenderKey((prevKey) => prevKey + 1);     
      setBoarded(null);  
       setIsLoading(false);
 },
      });
     }




  const validateFile = async (event) => {
    const selectedFile = event.target.files?.[0] ?? null;
    if (selectedFile) {
      const fileName = selectedFile.name;

      if (fileName.startsWith('Employees')) {
        setBoarded(selectedFile)
      } else {
        event.target.value = '';
        toast.warning('Invalid file!');
      }
    }
  };
  return (
    <div className="flex w-full justify-center items-center gap-3 mb-4 text-blue-600">
      <Box
        height={54}
        width={'100%'}
        border={0.5}
        sx={{
          borderStyle: 'dashed',
          borderColor: 'gray',
          borderRadius: 1,
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        <Input key={renderKey} onChange={(event) => validateFile(event)} type="file" />
      </Box>
      <LoadingButton
        sx={{
          height: 54,
        }}
        loading={isLoading}
        variant="contained"
        color="inherit"
        icon="solar:import-bold"
        onClick={handleUploadClick}
        disabled={!boardedFile}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          aria-hidden="true"
          role="img"
          className="component-iconify MuiBox-root css-1t9pz9x iconify iconify--eva"
          width="100%"
          height="100%"
          viewBox="0 0 24 24"
        >
          <path
            fill="currentColor"
            d="M21.9 12c0-.11-.06-.22-.09-.33a4.17 4.17 0 0 0-.18-.57c-.05-.12-.12-.24-.18-.37s-.15-.3-.24-.44S21 10.08 21 10s-.2-.25-.31-.37s-.21-.2-.32-.3L20 9l-.36-.24a3.68 3.68 0 0 0-.44-.23l-.39-.18a4.13 4.13 0 0 0-.5-.15a3 3 0 0 0-.41-.09L17.67 8A6 6 0 0 0 6.33 8l-.18.05a3 3 0 0 0-.41.09a4.13 4.13 0 0 0-.5.15l-.39.18a3.68 3.68 0 0 0-.44.23l-.36.3l-.37.31c-.11.1-.22.19-.32.3s-.21.25-.31.37s-.18.23-.26.36s-.16.29-.24.44s-.13.25-.18.37a4.17 4.17 0 0 0-.18.57c0 .11-.07.22-.09.33A5.23 5.23 0 0 0 2 13a5.5 5.5 0 0 0 .09.91c0 .1.05.19.07.29a5.58 5.58 0 0 0 .18.58l.12.29a5 5 0 0 0 .3.56l.14.22a.56.56 0 0 0 .05.08L3 16a5 5 0 0 0 4 2h3v-1.37a2 2 0 0 1-1 .27a2.05 2.05 0 0 1-1.44-.61a2 2 0 0 1 .05-2.83l3-2.9A2 2 0 0 1 12 10a2 2 0 0 1 1.41.59l3 3a2 2 0 0 1 0 2.82A2 2 0 0 1 15 17a1.92 1.92 0 0 1-1-.27V18h3a5 5 0 0 0 4-2l.05-.05a.56.56 0 0 0 .05-.08l.14-.22a5 5 0 0 0 .3-.56l.12-.29a5.58 5.58 0 0 0 .18-.58c0-.1.05-.19.07-.29A5.5 5.5 0 0 0 22 13a5.23 5.23 0 0 0-.1-1"
          ></path>
          <path
            fill="currentColor"
            d="M12.71 11.29a1 1 0 0 0-1.4 0l-3 2.9a1 1 0 1 0 1.38 1.44L11 14.36V20a1 1 0 0 0 2 0v-5.59l1.29 1.3a1 1 0 0 0 1.42 0a1 1 0 0 0 0-1.42Z"
          ></path>
        </svg>
      </LoadingButton>
    </div>
  );
}

export default ResourceImport;
