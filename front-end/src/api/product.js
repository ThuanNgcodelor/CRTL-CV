import axios from "axios";

const API_BASE_URL = "/v1";

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

export const fetchProducts = (params = {}) => {
    return api.get("/stock/product/list", { params });
};
