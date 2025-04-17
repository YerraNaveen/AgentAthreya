import axios, { AxiosInstance } from 'axios';
import { refreshToken } from "@/utils/tokenUtils";

class BaseApi {
  private axiosInstance: AxiosInstance;

  constructor(baseURL: string) {
    this.axiosInstance = axios.create({
      baseURL,
    });

    this.axiosInstance.interceptors.request.use((config) => {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    }, (error) => {
      return Promise.reject(error);
    });

    // Add a response interceptor to handle 401 errors
    this.axiosInstance.interceptors.response.use(
      (response) => response,
      async (error) => {
        if (error.response?.status === 401) {
          try {
            const newToken = await refreshToken();
            if (newToken) {
              // Retry the original request with the new token
              error.config.headers.Authorization = `Bearer ${newToken}`;
              return this.axiosInstance.request(error.config);
            }
          } catch (refreshError) {
            console.error("Token refresh failed:", refreshError);
            // Clear token and redirect to login if refresh fails
            localStorage.removeItem('token');
            window.location.href = "/login";
          }
        }
        return Promise.reject(error);
      }
    );
  }

  get instance() {
    return this.axiosInstance;
  }
}

export default new BaseApi('http://localhost:8080/api/v1');
