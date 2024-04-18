import React from 'react';
import { Helmet } from 'react-helmet-async';
import { NotPermissionView } from 'src/core/error';
// ----------------------------------------------------------------------

export default function NotPermissionPage() {
  return (
    <>
      <Helmet>
        <title> 403 Page Not Found </title>
      </Helmet>
      <NotPermissionView />
    </>
  );
}
