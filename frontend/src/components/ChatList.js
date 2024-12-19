import React, { useEffect, useState } from 'react';
import axios from 'axios';

const ChatList = ({ onSelectConversation }) => {
    const [conversations, setConversations] = useState([]);
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState('');

    useEffect(() => {
        axios.get('/api/users')
            .then(response => setUsers(response.data))
            .catch(error => console.error('Error fetching users:', error));
    }, []);

    useEffect(() => {
        if (selectedUser) {
            axios.get(`/api/chat/conversations?userId=${selectedUser}`)
                .then(response => setConversations(response.data))
                .catch(error => console.error('Error fetching conversations:', error));
        }
    }, [selectedUser]);

    return (
        <div>
            <h2>Conversations</h2>
            <select onChange={(e) => setSelectedUser(e.target.value)} value={selectedUser}>
                <option value="">Select User</option>
                {users.map(user => (
                    <option key={user.id} value={user.id}>{user.name}</option>
                ))}
            </select>
            <ul>
                {conversations.map(conversation => (
                    <li key={conversation.id} onClick={() => onSelectConversation(conversation.id)}>
                        {conversation.otherParticipant}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ChatList;