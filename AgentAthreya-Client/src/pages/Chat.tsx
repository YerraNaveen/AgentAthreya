import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';
import DashboardLayout from '@/components/layout/DashboardLayout';
import ChatWindow from '@/components/chat/ChatWindow';
import { Card } from '@/components/ui/card';
import axiosInstance from '@/api/axiosInstance';
import chatService from '@/services/chatService'; // Ensure chatService is imported

interface ChatMessage {
  id: number;
  sender: string;
  message: string;
  timestamp: string;
  userMessage?: string; // Add userMessage property
  systemMessage?: string; // Add systemMessage property
}

const Chat: React.FC = () => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const chatContainerRef = useRef<HTMLDivElement | null>(null);
  const [connectionError, setConnectionError] = useState(false);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
    }
  }, [isAuthenticated, navigate]);

  useEffect(() => {
    fetchChatHistory();
    checkServerHealth();
  }, []);
  

  useEffect(() => {
    chatContainerRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const fetchChatHistory = async () => {
    try {
      const response = await axiosInstance.get<ChatMessage[]>('/api/v1/conversation/history');
      const parsedMessages = response.data.map((msg) => ({
        id: msg.id,
        sender: msg.sender === 'user' ? 'user' : 'system', // Ensure sender is correctly mapped
        message: msg.userMessage || msg.systemMessage, // Map to the 'message' property
        timestamp: new Date(msg.timestamp).toISOString(), // Ensure timestamp is a string
      }));
      setMessages(parsedMessages);
    } catch (error) {
      console.error('Error fetching chat history:', error);
    }
  };

  const checkServerHealth = async () => {
    try {
      await axiosInstance.get('/api/v1/health'); // Replace with your server health endpoint
      setConnectionError(false);
    } catch (error) {
      console.error('Server health check failed:', error);
      setConnectionError(true);
    }
  };

  const handleSendMessage = async (message: string) => {
    if (!message.trim()) return;

    // Create the user message object
    const userMessage = {
      id: Date.now(), // Generate a unique ID
      sender: 'user',
      userMessage: message, // Use userMessage instead of message
      timestamp: new Date(),
    };

    // Add the user message to the chat immediately
    setMessages((prev) => [
      ...prev,
      { 
        ...userMessage, 
        message: userMessage.userMessage, // Populate the required 'message' property
        timestamp: userMessage.timestamp.toISOString() 
      },
    ]);

    try {
      // Send the complete message object to the server
      const result = await chatService.sendMessage({
        id: userMessage.id,
        sender: userMessage.sender,
        userMessage: userMessage.userMessage, // Use userMessage here
        timestamp: userMessage.timestamp.toISOString(),
      });

      if (result.success) {
        // Parse the system response and add it to the chat
        const systemMessage = {
          id: result.data.id.toString(),
          sender: 'system',
          message: result.data.message,
          timestamp: new Date(result.data.timestamp).toISOString(),
        };
        setMessages((prev) => [...prev, systemMessage]);
      } else {
        console.error('Failed to send message:', result.error);
      }
    } catch (error) {
      console.error('Error sending message:', error);
    }
  };

  if (!isAuthenticated) {
    return null;
  }

  return (
    <DashboardLayout>
      <div className="max-w-4xl mx-auto h-[calc(100vh-10rem)]">
        <Card className="h-full shadow-md overflow-hidden">
          {connectionError ? (
            <div className="flex items-center justify-center h-full text-red-500">
              Unable to connect to the server. Please try again later.
            </div>
          ) : (
            <ChatWindow
              onSendMessage={handleSendMessage}
              chatContainerRef={chatContainerRef}
              clearMessages={() => setMessages([])}
              messages={messages.map((msg) => ({
                id: msg.id.toString(),
                sender: msg.sender === 'user' ? 'user' : 'system',
                text: msg.message,
                timestamp: new Date(msg.timestamp),
              }))}
            />
          )}
        </Card>
      </div>
    </DashboardLayout>
  );
};

export default Chat;
