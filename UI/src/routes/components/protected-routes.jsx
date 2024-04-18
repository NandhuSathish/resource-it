import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import useAuth from 'src/hooks/use-auth';

function ProtectedRoutes({ children, allowedRoles }) {
  const navigate = useNavigate();
  const { getUserDetails } = useAuth();
  const { role } = getUserDetails();

  useEffect(() => {
    if (!allowedRoles.includes(role)) {
      navigate('/404');
    }
  }, [allowedRoles, role, navigate]);

  return children;
}

ProtectedRoutes.propTypes = {
  children: PropTypes.node.isRequired,
  allowedRoles: PropTypes.arrayOf(PropTypes.number).isRequired,
};

export default ProtectedRoutes;
