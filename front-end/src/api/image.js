import axios from "axios";

const API_BASE_URL = "/v1";

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

export const fetchImageById = (imageId) => {
    return api.get(`/file-storage/get/${imageId}`, {
        responseType: "arraybuffer",
    });
}
