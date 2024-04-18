import React from 'react';
import { render } from '@testing-library/react';
import {
  TreeViewEndIcon,
  TreeViewExpandIcon,
  TreeViewCollapseIcon,
  InputSelectIcon,
  CheckboxIndeterminateIcon,
  CheckboxCheckedIcon,
  CheckboxIcon,
  ErrorIcon,
  SuccessIcon,
  WarningIcon,
  InfoIcon,
  StarIcon,
  CloseIcon,
} from '../CustomIcons';
import { describe, it, expect } from 'vitest';
import '@testing-library/jest-dom/extend-expect';

describe('TreeViewEndIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<TreeViewEndIcon />);
    expect(container).toBeInTheDocument();
  });
});
describe('TreeViewExpandIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<TreeViewExpandIcon />);
    expect(container).toBeInTheDocument();
  });
});
describe('TreeViewCollapseIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<TreeViewCollapseIcon />);
    expect(container).toBeInTheDocument();
  });
});
describe('InputSelectIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<InputSelectIcon />);
    expect(container).toBeInTheDocument();
  });
});
describe('CheckboxIndeterminateIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<CheckboxIndeterminateIcon />);
    expect(container).toBeInTheDocument();
  });
});
describe('CheckboxCheckedIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<CheckboxCheckedIcon />);
    expect(container).toBeInTheDocument();
  });
});
describe('CheckboxIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<CheckboxIcon />);
    expect(container).toBeInTheDocument();
  });
});
describe('ErrorIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<ErrorIcon />);
    expect(container).toBeInTheDocument();
  });
});
describe('SuccessIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<SuccessIcon />);
    expect(container).toBeInTheDocument();
  });
});
describe('WarningIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<WarningIcon />);
    expect(container).toBeInTheDocument();
  });
});

describe('InfoIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<InfoIcon />);
    expect(container).toBeInTheDocument();
  });
});

describe('StarIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<StarIcon />);
    expect(container).toBeInTheDocument();
  });
});

describe('CloseIcon', () => {
  it('renders without crashing', () => {
    const { container } = render(<CloseIcon />);
    expect(container).toBeInTheDocument();
  });
});
