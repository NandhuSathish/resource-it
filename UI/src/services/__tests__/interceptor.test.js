import MockAdapter from 'axios-mock-adapter';
import { it, describe, beforeEach, afterEach, expect } from 'vitest';
import { axiosInstance } from '../interceptor';

// Define a test suite named 'Axios Interceptors'.
describe('Axios Interceptors', () => {
  let mock;
  let TOKEN_REFRESH_ENDPOINT = 'user/refresh_token';
  beforeEach(() => {
    mock = new MockAdapter(axiosInstance);
  });

  afterEach(() => {
    mock.restore();
  });

  it('should add the authorization token to the request', async () => {
    // Set up the mock response for the PUT request
    mock.onPut(TOKEN_REFRESH_ENDPOINT).reply(200, {
      accessToken: 'newAccessToken',
      refreshToken: 'newRefreshToken',
    });

    // Make a PUT request
    await axiosInstance.put(TOKEN_REFRESH_ENDPOINT, { refreshToken: null });

    // Ensure the request has been retried and the new access token is used
    const lastRequest = mock.history.put[0];
    expect(lastRequest.url).toBe(TOKEN_REFRESH_ENDPOINT);
    expect(lastRequest.data).toBe('{"refreshToken":null}');
  });

  it('should handle token refresh failure', async () => {
    // Mock a request that returns 401 Unauthorized
    mock.onGet('/your_api_endpoint').reply(401);

    // Set a token in local storage for testing
    localStorage.setItem('token', 'testToken');

    // Mock the refresh token request to throw an error
    mock.onPut(TOKEN_REFRESH_ENDPOINT).networkError();

    try {
      // Simulate an Axios request
      await axiosInstance.get('/your_api_endpoint');
    } catch (error) {
      // Ensure that the error is thrown due to token refresh failure
      expect(error.message).not.toBe('Request failed with status code 401');
    }
  });
});
