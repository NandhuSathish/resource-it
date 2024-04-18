import React, { Suspense } from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { HelmetProvider } from 'react-helmet-async';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { GoogleOAuthProvider } from '@react-oauth/google';
import { Toaster } from 'sonner';

import App from './app';
import { CollapseDrawerProvider } from './contexts/CollapseDrawerContext';
// ----------------------------------------------------------------------
const queryClient = new QueryClient();
const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <HelmetProvider>
    <QueryClientProvider client={queryClient}>
      <GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_AUTH_CLIENT_ID}>
        <CollapseDrawerProvider>
          <BrowserRouter>
            <Suspense>
              <Toaster position="top-right" closeButton richColors />
              <App />
            </Suspense>
          </BrowserRouter>
        </CollapseDrawerProvider>
        <ReactQueryDevtools />
      </GoogleOAuthProvider>
    </QueryClientProvider>
  </HelmetProvider>
);
