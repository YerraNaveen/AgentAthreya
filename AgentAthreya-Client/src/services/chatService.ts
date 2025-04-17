import BaseApi from '@/api/BaseApi';

const sendMessage = async (message: { id: number; sender: string; userMessage: string; timestamp: string }) => {
  try {
    const response = await BaseApi.instance.post('/conversation/chat', message); // Send the complete message object
    if (response.data && response.data.systemMessage) {
      return {
        success: true,
        data: {
          id: response.data.id,
          sender: response.data.sender || 'system', // Default to 'system' if sender is not provided
          message: response.data.systemMessage, // Use systemMessage for the response
          timestamp: response.data.timestamp,
        },
      };
    } else {
      return { success: false, error: 'Invalid response from server' };
    }
  } catch (error: any) {
    return { success: false, error: error.response?.data?.message || error.message };
  }
};

const getChatHistory = async () => {
    try {
        const response = await BaseApi.instance.get('/conversation/history');
        const parsedData = response.data.map((item: any) => ([
            {
                id: `${item.id}-user`,
                sender: 'user',
                text: item.userMessage,
                timestamp: new Date(item.timestamp),
            },
            {
                id: `${item.id}-system`,
                sender: 'system',
                text: item.systemMessage,
                timestamp: new Date(item.timestamp),
            }
        ])).flat();
        return { success: true, data: parsedData };
    } catch (error: any) {
        return { success: false, error: error.response?.data?.message || error.message }; // Handle error response properly
    }
};

export default {
    sendMessage,
    getChatHistory,
};
