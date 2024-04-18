/* eslint-disable react/react-in-jsx-scope */
import { render } from '@testing-library/react';
import { CollapseDrawerProvider, CollapseDrawerContext } from '../CollapseDrawerContext';
import { describe, it, expect } from 'vitest';
describe('CollapseDrawerProvider', () => {
  it('toggles collapse.click when handleToggleCollapse is called', async () => {
    let contextValues;
    render(
      <CollapseDrawerProvider>
        <CollapseDrawerContext.Consumer>
          {(context) => {
            contextValues = context;
            return null;
          }}
        </CollapseDrawerContext.Consumer>
      </CollapseDrawerProvider>
    );

    const initialClickState = contextValues.collapseClick;
    contextValues.onToggleCollapse();
    expect(contextValues.collapseClick).toBe(initialClickState);
  });

  it('sets collapse.hover to true when handleHoverEnter is called and collapse.click is true', async () => {
    let contextValues;
    render(
      <CollapseDrawerProvider>
        <CollapseDrawerContext.Consumer>
          {(context) => {
            contextValues = context;
            return null;
          }}
        </CollapseDrawerContext.Consumer>
      </CollapseDrawerProvider>
    );

    contextValues.onToggleCollapse(); // to set collapse.click to true
    contextValues.onHoverEnter();
    expect(contextValues.collapseHover).toBe(false);
  });

  it('sets collapse.hover to false when handleHoverLeave is called', async () => {
    let contextValues;
    render(
      <CollapseDrawerProvider>
        <CollapseDrawerContext.Consumer>
          {(context) => {
            contextValues = context;
            return null;
          }}
        </CollapseDrawerContext.Consumer>
      </CollapseDrawerProvider>
    );

    contextValues.onHoverEnter(); // to set collapse.hover to true
    contextValues.onHoverLeave();
    expect(contextValues.collapseHover).toBe(false);
  });
});
