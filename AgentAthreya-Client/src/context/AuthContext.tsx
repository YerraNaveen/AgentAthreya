import React, { createContext, useState, useContext, ReactNode, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from "@/hooks/use-toast";
import BaseApi from "@/api/BaseApi";
import { useChat } from '@/context/ChatContext';
import chatService from '@/services/chatService';
import { jwtDecode } from 'jwt-decode';
import { refreshToken } from "@/utils/tokenUtils";

interface User {
  id: string;
  email: string;
  name: string;
}

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  login: (username: string, password: string) => Promise<void>;
  signup: (username: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [sessionTimeout, setSessionTimeout] = useState<NodeJS.Timeout | null>(null);
  const navigate = useNavigate();
  const { setChatHistory } = useChat();

  const refreshSession = async () => {
    try {
      const newToken = await refreshToken();
      if (newToken) {
        const decodedToken: any = jwtDecode(newToken);
        setUser({ id: decodedToken.id, email: decodedToken.email, name: decodedToken.username });
        scheduleSessionTimeout(decodedToken.exp);
      } else {
        logout();
      }
    } catch (error) {
      console.error("Failed to refresh session:", error);
      logout();
    }
  };

  const scheduleSessionTimeout = (expirationTime: number) => {
    const currentTime = Date.now() / 1000; // Current time in seconds
    const timeUntilExpiration = (expirationTime - currentTime - 60) * 1000; // Show popup 1 minute before expiration

    if (sessionTimeout) clearTimeout(sessionTimeout);

    if (timeUntilExpiration > 0) {
      const timeout = setTimeout(() => {
        toast({
          title: "Session Expiring",
          description: "Your session is about to expire. Do you want to extend it?",
          action: {
            label: "Extend Session",
            onClick: refreshSession,
          },
        });
      }, timeUntilExpiration);

      setSessionTimeout(timeout);
    }
  };

  const login = async (username: string, password: string) => {
    try {
      const response = await BaseApi.instance.post("/auth/login", {
        username,
        password,
      });
      const token = response.data;
      localStorage.setItem("token", token);
      const decodedToken: any = jwtDecode(token);
      setUser({ id: decodedToken.id, email: decodedToken.email, name: decodedToken.username }); // Use "username" instead of "sub"
      scheduleSessionTimeout(decodedToken.exp);

      // Fetch chat history
      const chatHistoryResult = await chatService.getChatHistory();
      if (chatHistoryResult.success) {
        setChatHistory(chatHistoryResult.data);
      } else {
        console.error(chatHistoryResult.error);
      }

      toast({
        title: "Login successful",
        description: "Welcome back!",
      });
      navigate('/chat');
    } catch (error: any) {
      if (error.code === "ERR_NETWORK") {
        toast({
          title: "Network Error",
          description: "Unable to connect to the server. Please try again later.",
          variant: "destructive",
        });
      } else {
        toast({
          title: "Login failed",
          description: "Invalid credentials. Please try again.",
          variant: "destructive",
        });
      }
      console.error('Login error:', error);
    }
  };

  const signup = async (username: string, password: string) => {
    try {
      const response = await BaseApi.instance.post("/auth/register", {
        username,
        password,
      });
      const token = response.data;
      localStorage.setItem("token", token);
      const decodedToken: any = jwtDecode(token);
      setUser({ id: decodedToken.id, email: decodedToken.email, name: decodedToken.username }); // Use "username" instead of "sub"
      scheduleSessionTimeout(decodedToken.exp);
      toast({
        title: "Account created",
        description: "Your account has been created successfully!",
      });
      navigate('/chat');
    } catch (error: any) {
      if (error.response && error.response.data && error.response.data.status === 500) {
        throw error.response.data;
      } else {
        toast({
          title: "Sign up failed",
          description: "Something went wrong. Please try again.",
          variant: "destructive",
        });
        console.error('Signup error:', error);
      }
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('token');
    if (sessionTimeout) clearTimeout(sessionTimeout);
    navigate('/login');
  };

  // Check for existing user session on load
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decodedToken: any = jwtDecode(token);

        // Check if the token is expired
        const currentTime = Date.now() / 1000; // Current time in seconds
        if (decodedToken.exp < currentTime) {
          console.warn("Token expired. Logging out.");
          logout();
          return;
        }

        const user = { id: decodedToken.id, email: decodedToken.email, name: decodedToken.username };
        setUser(user);
        scheduleSessionTimeout(decodedToken.exp);

        // Fetch chat history
        const fetchChatHistory = async () => {
          const chatHistoryResult = await chatService.getChatHistory();
          if (chatHistoryResult.success) {
            setChatHistory(chatHistoryResult.data);
          } else {
            console.error(chatHistoryResult.error);
          }
        };

        fetchChatHistory();
      } catch (error) {
        console.error("Error decoding token:", error);
        logout();
      }
    }
  }, []); // Ensure this effect runs only once

  const value = {
    user,
    isAuthenticated: !!user,
    login,
    signup,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
