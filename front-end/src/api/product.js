import axios from "axios";
import Cookies from "js-cookie";

const API_BASE_URL = "/v1";

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

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

export const fetchProducts = (params = {}) => {
    return api.get("/stock/product/list", { params });
};

export const fetchProductImageById = (imageId) => {
    return api.get(`/file-storage/get/${imageId}`, {
        responseType: "arraybuffer",
    });
}

export const fetchAddToCart = async (data) => {
    try{
        const response = await api.post(`/stock/cart/item/add`, data);
        return response.data;
    }catch(err){
        throw err;
    }
}
