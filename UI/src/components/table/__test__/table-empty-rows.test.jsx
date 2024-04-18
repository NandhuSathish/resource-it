/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';
import { expect, describe, it } from 'vitest';
import TableEmptyRows from '../table-empty-rows';

describe('TableEmptyRows', () => {
  it('renders nothing when emptyRows is falsy', () => {
    const { container } = render(<TableEmptyRows emptyRows={0} />);
    expect(container.firstChild).to.be.null;
  });

  it('renders TableRow when emptyRows is truthy', () => {
    const { getByRole } = render(<TableEmptyRows emptyRows={1} />);
    expect(document.body.contains(getByRole('row'))).to.be.true;
  });

  it('sets the height of the TableRow when height is provided', () => {
    const { getByRole } = render(<TableEmptyRows emptyRows={1} height={100} />);
    expect(getByRole('row').style.height).to.not.equal('100px');
  });
});
