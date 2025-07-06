import axios from "axios";
import Cookies from "js-cookie";

const API_URL = "/v1/auth";

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor to add  token in header
api.interceptors.request.use(
  (config) => {
    const token = Cookies.get("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor to xyly response
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      Cookies.remove("token");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export const login = async (data) => {
  try {
    const response = await api.post("/login", data);
    const { token } = response.data;
    
    Cookies.set("token", token, { expires: 7 });
    
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const register = async (data) => {
  try {
    const response = await api.post("/register", data);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const getToken = () => {
  return Cookies.get("token") || null;
};

export const logout = () => {
  Cookies.remove("token");
};

export const getUserRole = () => {
  const token = getToken();
  if (!token) return null;

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));

    if (Array.isArray(payload.roles)) {
      return payload.roles;
    }

    return payload.role || payload.authorities?.[0] || payload.iss || null;
  } catch (error) {
    console.error("Error decoding token:", error);
    return null;
  }
};

export const isAuthenticated = () => {
  return !!getToken();
}; 