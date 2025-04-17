import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "@/context/AuthContext";
import { ChatProvider } from "@/context/ChatContext";
import Login from "./pages/Login";
import SignUp from "./pages/SignUp";
import Chat from "./pages/Chat";
import NotFound from "./pages/NotFound";
import FileUploadPage from './pages/FileUploadPage';
import React from 'react';
import SessionManager from './components/SessionManager';

const queryClient = new QueryClient();

const App: React.FC = () => {
  const sessionDuration = 360; // 6 minutes in seconds

  const onExtendSession = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('No token found');
      }

      const response = await fetch('/api/v1/auth/refresh-token', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ token }), // Send the token in the request body
      });

      if (!response.ok) {
        throw new Error('Failed to extend session');
      }

      const newToken = await response.text(); // Assuming the backend returns the new token as plain text
      localStorage.setItem('token', newToken);
      console.log('Session successfully extended!');
    } catch (error) {
      console.error('Error extending session:', error);
    }
  };

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <ChatProvider>
          <AuthProvider>
            <TooltipProvider>
              <Toaster />
              <Sonner />
              <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<SignUp />} />
                <Route path="/chat" element={<Chat />} />
                <Route path="/upload" element={<FileUploadPage />} />
                <Route path="*" element={<NotFound />} />
              </Routes>
              <SessionManager sessionDuration={sessionDuration} onExtendSession={onExtendSession} />
            </TooltipProvider>
          </AuthProvider>
        </ChatProvider>
      </BrowserRouter>
    </QueryClientProvider>
  );
};

export default App;
