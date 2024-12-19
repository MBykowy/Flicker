import React, { useState } from 'react';

const MessageInput = ({ conversationId, senderId, socket }) => {
    const [content, setContent] = useState('');

    const sendMessage = () => {
        if (content.trim()) {
            const message = { conversationId, senderId, content };
            socket.send(JSON.stringify(message));
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