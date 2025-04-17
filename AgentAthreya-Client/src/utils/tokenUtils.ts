import BaseApi from "@/api/BaseApi";

export const refreshToken = async (): Promise<string | null> => {
    try {
        const token = localStorage.getItem("token");
        if (!token) {
            throw new Error("No token found");
        }

        const response = await BaseApi.instance.post("/auth/refresh-token", { token });
        const newToken = response.data;

        // Save the new token in localStorage
        localStorage.setItem("token", newToken);

        return newToken;
    } catch (error) {
        console.error("Failed to refresh token:", error);
        localStorage.removeItem("token");
        return null;
    }
};
