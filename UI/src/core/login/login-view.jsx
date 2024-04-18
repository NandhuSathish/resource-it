/* eslint-disable react/react-in-jsx-scope */
import GoogleLoginButton from 'src/components/login-btn/google-login-button';

// ----------------------------------------------------------------------

const LoginView = () => {
  return (
    <div className="bg-white  ">
      <div className="flex justify-center h-screen">
        <div className="  lg:w-2/3 flex items-center justify-center">
          <img className="w-2/3" src="/assets/LoginBG.png" alt="" />
        </div>

        <div className="flex items-center w-full max-w-md px-6 mx-auto lg:w-2/6">
          <div className="flex-1">
            <div className="text-center">
              <div className="flex justify-center mx-auto w-2/3">
                <img src="/assets/logoLG.png" alt="logo" style={{ userSelect: 'none' }} />
              </div>

              <h6 className=" text-gray-600 text-base">Sign in to access your account</h6>
              <div className="w-full p-4 flex justify-center items-center">
                <GoogleLoginButton />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginView;
