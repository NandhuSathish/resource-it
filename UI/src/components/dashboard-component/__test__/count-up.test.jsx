// FILEPATH: /home/shobithchandran/Documents/Updated ResourceIT/resource-it/UI/src/components/dashboard-component/__tests__/count-up.test.jsx

import { render } from '@testing-library/react';
import CardCounter from '../count-up';
import { expect, it, describe } from 'vitest';
import React from 'react';

describe('CardCounter', () => {
    it('renders without crashing', () => {
        const { container } = render(
            <CardCounter value={100} />
        );

        expect(container.firstChild).to.exist;
    });
});