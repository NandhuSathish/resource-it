/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';
import DialogBox from '../dialog';
import { describe, it } from 'vitest';

describe('DialogBox', () => {
  it('renders without crashing', () => {
    const content = {
      trigger: <button>Open Dialog</button>,
      name: 'Test Dialog',
      width: '500px',
      content: 'Test Content',
    };

    render(<DialogBox content={content} />);
  });
});
