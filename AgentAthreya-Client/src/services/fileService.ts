import BaseApi from '@/api/BaseApi';

const uploadFile = async (file: File) => {
    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await BaseApi.instance.post('/files/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.message };
    }
};

export default {
    uploadFile,
};
