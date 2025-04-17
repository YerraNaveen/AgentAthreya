import axios from "axios";

const API_BASE_URL = "http://localhost:8080"; // Update if needed

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// ✅ Attach JWT Token Automatically for Every Request
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token"); // Get token from storage
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; // Attach token
    }
    return config;
  },
  (error) => Promise.reject("J Error" + error)
);

export default axiosInstance;
