import React  from 'react';
import SkillDataImport from '../skill-data-Import';
import { resourceSkillImportString } from 'src/utils/constants';
import {Typography} from '@mui/material';


export function SkillDataImportView() {
  return (
    <div>
      <div className='mb-10'>
        <Typography>
          <pre className="font-sans text-base">{resourceSkillImportString}</pre>
        </Typography>
      </div>
      <SkillDataImport />
    </div>
  );
}

export default SkillDataImportView;
