import React, { MutableRefObject, useEffect } from 'react';
import ChatMessage from './ChatMessage';
import ChatInput from './ChatInput';
import { Button } from '@/components/ui/button';
import { Trash2 } from 'lucide-react';

interface Message {
  id: string;
  sender: 'user' | 'system';
  text: string;
  timestamp: Date;
}

interface ChatWindowProps {
  messages: Message[];
  onSendMessage: (message: string) => void;
  chatContainerRef: MutableRefObject<HTMLDivElement | null>;
  clearMessages: () => void;
}

const ChatWindow: React.FC<ChatWindowProps> = ({ messages, onSendMessage, chatContainerRef, clearMessages }) => {
  // Auto-scroll to bottom when messages update
  useEffect(() => {
    chatContainerRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, chatContainerRef]);

  return (
    <div className="flex flex-col h-full bg-gray-50">
      {/* Header */}
      <div className="flex items-center justify-between px-6 py-4 border-b shadow-sm bg-white">
        <h2 className="text-xl font-semibold">AgentAthreya Chat</h2>
        <Button
          variant="ghost"
          size="sm"
          onClick={clearMessages}
          className="text-muted-foreground hover:text-foreground"
        >
          <Trash2 className="h-4 w-4 mr-2" />
          Clear chat
        </Button>
      </div>

      {/* Message list */}
      <div className="flex-1 overflow-y-auto px-6 py-4 space-y-4">
        {messages.map((message) => (
          <ChatMessage
            key={message.id}
            message={{
              id: message.id,
              sender: message.sender,
              text: message.text,
              timestamp: message.timestamp,
            }}
          />
        ))}
        <div ref={chatContainerRef} />
      </div>

      {/* Chat Input */}
      <div className="border-t bg-white px-6 py-4 shadow-sm">
        <ChatInput onSendMessage={onSendMessage} />
      </div>
    </div>
  );
};

export default ChatWindow;
