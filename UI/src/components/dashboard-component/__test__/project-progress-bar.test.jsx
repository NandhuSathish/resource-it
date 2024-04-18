// FILEPATH: /home/shobithchandran/Documents/Updated ResourceIT/resource-it/UI/src/components/dashboard-component/__tests__/project-progress-bar.test.jsx

import { render } from '@testing-library/react';
import ProjectProgressBar from '../project-progress-bar';
import {  it, describe,  } from 'vitest';
import React from 'react';
describe('ProjectProgressBar', () => {
    it('renders without crashing', () => {
        render(<ProjectProgressBar content={{ percentage: 50, width: '100px', height: '100px' }} />);
    });
});