/* eslint-disable react/react-in-jsx-scope */
import { GoogleLogin } from '@react-oauth/google';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import useAuth from 'src/hooks/use-auth';

// -----------------------------------------------------

function GoogleLoginButton() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSuccess = (response) => {
    login.mutate(response, {
      onSuccess: (data) => {
        if (data.role === 6) {
          navigate('/resourceManagement');
          return;
        }
        navigate('/dashboard');
      },
      onError: () => {
        toast.error('Unable to perform this action');
      },
    });
  };

  const handleError = (error) => {
    console.error(error);
  };
  return <GoogleLogin onSuccess={handleSuccess} onError={handleError} />;
}

export default GoogleLoginButton;
