import axios from 'axios';
import { toast } from 'sonner';

const TOKEN_REFRESH_ENDPOINT = '/login';

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    'Content-type': 'application/json',
  },
});

const refreshTokenInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    'Content-type': 'application/json',
  },
});

axiosInstance.interceptors.request.use((request) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    request.headers.Authorization = `Bearer ${token}`;
  }
  return request;
});

let isRefreshing = false;
let failedQueue = [];
const logout = () => {
  toast.error('Session expired. Please login again.');
  // Clear local storage
  localStorage.clear();
  // Redirect to login page
  window.location.href = '/login';
};

const processQueue = (error, token = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });

  failedQueue = [];
};

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (
      error.response &&(error.response.status === 401 || error.response.status === 403) &&
      !originalRequest._retry &&
      originalRequest.url !== TOKEN_REFRESH_ENDPOINT
    ) {
      if (isRefreshing) {
        return new Promise(function (resolve, reject) {
          failedQueue.push({ resolve, reject });
        })
          .then((token) => {
            originalRequest.headers['Authorization'] = token;
            return axiosInstance(originalRequest);
          })
          .catch((err) => {
            return Promise.reject(err);
          });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      return new Promise(function (resolve, reject) {
        refreshAccessToken()
          .then((newToken) => {
            axiosInstance.defaults.headers.common['Authorization'] = newToken;
            originalRequest.headers['Authorization'] = newToken;
            processQueue(null, newToken);
            resolve(axiosInstance(originalRequest));
          })
          .catch((err) => {
            processQueue(err, null);
            reject(err);
          })
          .then(() => {
            isRefreshing = false;
          });
      });
    }

    return Promise.reject(error);
  }
);

const refreshAccessToken = async () => {
  try {
    let refreshToken = localStorage.getItem('refreshToken');
    const response = await refreshTokenInstance.put(TOKEN_REFRESH_ENDPOINT, {
      refreshToken,
    });
    const newAccessToken = response.data.accessToken;
    const newRefreshToken = response.data.refreshToken;
    localStorage.setItem('accessToken', newAccessToken);
    localStorage.setItem('refreshToken', newRefreshToken);

    return newAccessToken;
  } catch (err) {
    logout();
    throw new Error('Failed to refresh access token.');
  }
};
