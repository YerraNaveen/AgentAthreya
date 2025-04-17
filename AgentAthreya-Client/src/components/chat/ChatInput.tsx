import React, { useState, useRef, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Send, Upload } from 'lucide-react';
import fileService from '@/services/fileService';
import { toast } from '@/hooks/use-toast';

interface ChatInputProps {
  onSendMessage: (message: string) => void;
  isDisabled?: boolean;
}

const ChatInput: React.FC<ChatInputProps> = ({ onSendMessage, isDisabled = false }) => {
  const [message, setMessage] = useState('');
  const [uploading, setUploading] = useState(false);
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  // Auto resize the textarea
  useEffect(() => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = 'auto';
      textarea.style.height = `${Math.min(textarea.scrollHeight, 150)}px`;
    }
  }, [message]);

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const handleSendMessage = () => {
    const trimmedMessage = message.trim();
    if (trimmedMessage && !isDisabled) {
      onSendMessage(trimmedMessage);
      setMessage('');
    }
  };

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const file = e.target.files[0];
      setUploading(true);
      const result = await fileService.uploadFile(file);
      setUploading(false);
      if (result.success) {
        toast({
          title: "File uploaded",
          description: `File ${file.name} uploaded successfully!`,
        });
        onSendMessage(`File uploaded: ${file.name}`);
      } else {
        toast({
          title: "Upload failed",
          description: `Failed to upload file: ${result.error}`,
          variant: "destructive",
        });
      }
    }
  };

  const handleUploadClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  return (
    <div className="bg-card border rounded-lg p-3 flex items-end gap-2">
      <input
        type="file"
        accept=".pdf,.doc,.docx"
        onChange={handleFileChange}
        className="hidden"
        ref={fileInputRef}
      />
      <Button variant="ghost" size="icon" onClick={handleUploadClick} disabled={uploading || isDisabled}>
        <Upload className="h-4 w-4" />
      </Button>
      <textarea
        ref={textareaRef}
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Type a message..."
        disabled={isDisabled}
        className="flex-1 outline-none border-0 bg-transparent resize-none max-h-[150px] focus:ring-0 p-2"
        rows={1}
      />
      <Button
        onClick={handleSendMessage}
        disabled={!message.trim() || isDisabled}
        size="icon"
        className="h-9 w-9 rounded-full"
      >
        <Send className="h-4 w-4" />
      </Button>
    </div>
  );
};

export default ChatInput;
