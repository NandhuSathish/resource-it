import { useMutation } from '@tanstack/react-query';
import { axiosInstance } from '../services/interceptor';
import { useNavigate } from 'react-router-dom';

const useAuth = () => {
  const navigate = useNavigate();

  const login = useMutation({
    mutationFn: (credential) => {
      const token = {
        token: credential.credential,
      };
      return axiosInstance.post('/login', { ...token }).then((res) => res.data);
    },
    onSuccess: ({ email, accessToken, refreshToken, userName, role, pictureUrl,resourceId }) => {
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      localStorage.setItem('role', role);
      localStorage.setItem('email', email);
      localStorage.setItem('username', userName);
      localStorage.setItem('pictureUrl', pictureUrl);
      localStorage.setItem('resourceId', resourceId);
      navigate('/resourceManagement');
    },
    onError: () => {},
  });

  const logout = () => {
    localStorage.clear();
    window.location.reload();
  };

  const getUserDetails = () => {
    return {
      email: localStorage.getItem('email'),
      username: localStorage.getItem('username'),
      role: parseInt(localStorage.getItem('role')),
      pictureUrl: localStorage.getItem('pictureUrl'),
      resourceId: localStorage.getItem('resourceId'),
    };
  };

  const isAuthenticated = () => {
    return localStorage.getItem('accessToken') !== null;
  };

  return { login, logout, getUserDetails, isAuthenticated };
};

export default useAuth;
