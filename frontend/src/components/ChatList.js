import React, { useEffect, useState } from 'react';
import axios from 'axios';

const ChatList = ({ onSelectConversation }) => {
  const [conversations, setConversations] = useState([]);

  useEffect(() => {
    axios.get('/api/chat/conversations?userId=1') // Replace with dynamic userId
      .then(response => setConversations(response.data))
      .catch(error => console.error('Error fetching conversations:', error));
  }, []);

  return (
    <div>
      <h2>Conversations</h2>
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