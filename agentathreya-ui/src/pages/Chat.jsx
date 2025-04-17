import { useState, useEffect, useRef } from "react";
import axiosInstance from "../api/axiosInstance"; // Import Axios instance

function Chat() {
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");
  const chatContainerRef = useRef(null);

  useEffect(() => {
    fetchMessages();
  }, []);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const fetchMessages = async () => {
    try {
      const response = await axiosInstance.get("/api/v1/conversation/history");
      setMessages(response.data);
    } catch (error) {
      console.error("Error fetching messages:", error);
    }
  };

  const sendMessage = async () => {
    if (!message.trim()) return;

    try {
      const response = await axiosInstance.post("/api/v1/conversation/chat", {
        message: message.trim(),
      });
      setMessages([...messages, response.data]);
      setMessage("");
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

  return (
    <div className="flex flex-col h-screen bg-gray-100">
      {/* Header */}
      <div className="bg-gradient-to-r from-blue-600 to-purple-600 text-white text-lg font-semibold p-4 flex justify-between items-center shadow-md">
        <span>💬 AgentAthreya Chat</span>
        <button
          onClick={() => {
            localStorage.removeItem("token"); // Logout functionality
            window.location.href = "/";
          }}
          className="bg-white text-gray-800 px-4 py-1 rounded-md text-sm hover:bg-gray-200"
        >
          Logout
        </button>
      </div>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto p-4 space-y-3">
        {messages.length > 0 ? (
          messages.map((msg, index) => (
            <div
              key={index}
              className={`p-3 max-w-xs rounded-lg shadow-md ${
                index % 2 === 0
                  ? "bg-blue-500 text-white self-end ml-auto"
                  : "bg-gray-300 text-black self-start"
              }`}
            >
              {msg.message}
            </div>
          ))
        ) : (
          <p className="text-center text-gray-500">No messages yet.</p>
        )}
        <div ref={chatContainerRef}></div>
      </div>

      {/* Input Box */}
      <div className="p-4 bg-white flex shadow-md">
        <input
          type="text"
          className="flex-1 p-3 border rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500"
          placeholder="Type a message..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        />
        <button
          onClick={sendMessage}
          className="ml-3 bg-blue-600 text-white px-6 py-3 rounded-xl hover:bg-blue-700 transition"
        >
          Send
        </button>
      </div>
    </div>
  );
}

export default Chat;
