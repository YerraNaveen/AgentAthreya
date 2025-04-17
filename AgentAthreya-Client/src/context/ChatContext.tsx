import React, { createContext, useState, useContext, ReactNode } from 'react';
import chatService from '../services/chatService';

interface Message {
  id: string;
  sender: 'user' | 'system';
  text: string;
  timestamp: Date;
}

interface ChatContextType {
  messages: Message[];
  sendMessage: (text: string) => void;
  clearMessages: () => void;
  setChatHistory: (messages: Message[]) => void;
}

const ChatContext = createContext<ChatContextType | undefined>(undefined);

export const useChat = () => {
  const context = useContext(ChatContext);
  if (!context) {
    throw new Error('useChat must be used within a ChatProvider');
  }
  return context;
};

export const ChatProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [messages, setMessages] = useState<Message[]>([]);

  const setChatHistory = (messages: Message[]) => {
    setMessages(messages.length > 0 ? messages : [{
      id: '1',
      sender: 'system',
      text: 'Hello! How can I assist you today?',
      timestamp: new Date(),
    }]);
  };

  const sendMessage = async (text: string) => {
    // Add user message
    const userMessage: Message = {
      id: Date.now().toString(),
      sender: 'user',
      text,
      timestamp: new Date(),
    };
    
    setMessages(prev => [...prev, userMessage]);
    
    // Send message to server
    const result = await chatService.sendMessage(text);
    if (result.success) {
      const systemMessage: Message = {
        id: result.data.id.toString(),
        sender: 'system',
        text: result.data.message,
        timestamp: new Date(result.data.timestamp),
      };
      setMessages(prev => [...prev, systemMessage]);
    } else {
      console.error(result.error);
    }
  };

  const clearMessages = () => {
    setMessages([
      {
        id: '1',
        sender: 'system',
        text: 'Hello! How can I assist you today?',
        timestamp: new Date(),
      },
    ]);
  };

  return (
    <ChatContext.Provider value={{ messages, sendMessage, clearMessages, setChatHistory }}>
      {children}
    </ChatContext.Provider>
  );
};
