/* eslint-disable react/react-in-jsx-scope */
import { Helmet } from 'react-helmet-async';
import { LoginView } from 'src/core/login';
// ----------------------------------------------------------------------

export default function LoginPage() {
  return (
    <>
      <Helmet>
        <title> Login </title>
      </Helmet>
      <LoginView />
    </>
  );
}
