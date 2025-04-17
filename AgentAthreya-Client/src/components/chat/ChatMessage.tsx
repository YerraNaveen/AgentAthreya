import React from 'react';
import { format } from 'date-fns';
import { User, Bot } from 'lucide-react';

interface ChatMessageProps {
  message: {
    id: string;
    sender: 'user' | 'system';
    text: string;
    timestamp: Date;
  };
}

const ChatMessage: React.FC<ChatMessageProps> = ({ message }) => {
  const isUser = message.sender === 'user'; // Check if the sender is the user

  return (
    <div className={`flex ${isUser ? 'justify-end' : 'justify-start'} mb-4`}>
      {!isUser && (
        <div className="flex-shrink-0 h-8 w-8 rounded-full bg-secondary flex items-center justify-center mr-2">
          <Bot className="h-4 w-4 text-secondary-foreground" />
        </div>
      )}

      <div>
        <div className={`chat-bubble ${isUser ? 'user-bubble' : 'system-bubble'}`}>
          <p className="whitespace-pre-wrap break-words">{message.text}</p>
        </div>
        <div className={`text-xs text-muted-foreground mt-1 ${isUser ? 'text-right' : 'text-left'}`}>
          {format(message.timestamp, 'p')}
        </div>
      </div>

      {isUser && (
        <div className="flex-shrink-0 h-8 w-8 rounded-full bg-primary flex items-center justify-center ml-2">
          <User className="h-4 w-4 text-primary-foreground" />
        </div>
      )}
    </div>
  );
};

export default ChatMessage;
