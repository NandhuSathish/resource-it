import React from 'react';
import 'src/global.css';

import Router from 'src/routes/index';
import ThemeProvider from 'src/theme';
import ScrollToTop from './components/scrolltotop';
import { ConfirmProvider } from 'material-ui-confirm';
import MotionLazyContainer from './components/MotionLazyContainer';
import { ProgressBarStyle } from './components/ProgressBar';

// ----------------------------------------------------------------------

export default function App() {
  return (
    <ThemeProvider>
      <ConfirmProvider>
        <ScrollToTop />
        <MotionLazyContainer>
          <ProgressBarStyle />
          <Router />
        </MotionLazyContainer>
      </ConfirmProvider>
    </ThemeProvider>
  );
}
