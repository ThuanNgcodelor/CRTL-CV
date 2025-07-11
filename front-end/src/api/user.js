import axios from "axios";
import Cookies from "js-cookie";

const API_URL = "/v1/user";

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

api.interceptors.request.use(
    (config) => {
        const token = Cookies.get("token");
        if (token){
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export const getCart = async () => {
    try{
        const response = await api("/cart");
        return response.data;
    }catch(e){
        throw e;
    }
}

