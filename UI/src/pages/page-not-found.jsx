import React from 'react';
import { Helmet } from 'react-helmet-async';
import { NotFoundView } from 'src/core/error';
// ----------------------------------------------------------------------

export default function NotFoundPage() {
  return (
    <>
      <Helmet>
        <title> 404 Page Not Found </title>
      </Helmet>
      <NotFoundView />
    </>
  );
}
