import React, { useState } from 'react';
import { Client } from '@stomp/stompjs';

const MessageInput = ({ conversationId, senderId, socket }) => {
    const [content, setContent] = useState('');

    const sendMessage = () => {
        if (content.trim()) {
            const message = { conversationId, senderId, content };
            socket.publish({ destination: '/app/chat', body: JSON.stringify(message) });
            setContent('');
        }
    };

    return (
        <div>
            <input
                type="text"
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="Type a message..."
            />
            <button onClick={sendMessage}>Send</button>
        </div>
    );
};

export default MessageInput;