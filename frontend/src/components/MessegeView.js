import React, { useEffect, useState } from 'react';
import axios from 'axios';

const MessageView = ({ conversationId }) => {
    const [messages, setMessages] = useState([]);

    useEffect(() => {
        if (conversationId) {
            axios.get(`/api/chat/messages/${conversationId}`)
                .then(response => setMessages(response.data))
                .catch(error => console.error('Error fetching messages:', error));
        }
    }, [conversationId]);

    return (
        <div>
            <h2>Messages</h2>
            <ul>
                {messages.map(message => (
                    <li key={message.id}>
                        <strong>{message.senderId}:</strong> {message.content}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default MessageView;