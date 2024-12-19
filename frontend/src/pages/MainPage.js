import React, { useState, useEffect } from "react";
import { Box, Typography, Button, TextField, Paper, Container, Avatar } from "@mui/material";
import Confetti from "react-confetti";
import { useWindowSize } from "react-use";
import { useAuth } from "../context/AuthContext";
import { Link } from "react-router-dom";
import ChatList from "../components/ChatList";
import MessageView from "../components/MessegeView";
import MessageInput from "../components/MessageInput";

const MainPage = () => {
    const { width, height } = useWindowSize();
    const { logout } = useAuth();
    const [posts, setPosts] = useState([]);
    const [newPost, setNewPost] = useState("");
    const [mediaUrl, setMediaUrl] = useState("");
    const [file, setFile] = useState(null);
    const [email, setEmail] = useState("");
    const [comments, setComments] = useState({});
    const [newComment, setNewComment] = useState("");
    const [selectedConversation, setSelectedConversation] = useState(null);
    const [socket, setSocket] = useState(null);

    useEffect(() => {
        const fetchPosts = async () => {
            const response = await fetch(`/posts`);
            const data = await response.json();
            setPosts(data);
        };

        const emailFromCookie = document.cookie.split("; ").find(row => row.startsWith("email="))?.split("=")[1];
        setEmail(emailFromCookie);
        if (emailFromCookie) {
            fetchPosts();
        }
    }, [email]);

    useEffect(() => {
        const newSocket = new WebSocket('ws://yourserver.com/chat');
        newSocket.onopen = () => console.log('WebSocket connection established');
        newSocket.onmessage = (event) => {
            const message = JSON.parse(event.data);
            console.log("New message received: ", message);
            if (message.conversationId === selectedConversation) {
                setMessages(prevMessages => [...prevMessages, message]);
            }
        };
        setSocket(newSocket);

        return () => newSocket.close();
    }, [selectedConversation]);

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handlePostSubmit = async () => {
        let uploadedMediaUrl = mediaUrl;

        if (file) {
            const formData = new FormData();
            formData.append("file", file);

            const response = await fetch("/posts/upload", {
                method: "POST",
                body: formData,
            });

            if (response.ok) {
                uploadedMediaUrl = await response.text();
            }
        }

        const response = await fetch("/posts", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, content: newPost, mediaUrl: uploadedMediaUrl }),
        });

        if (response.ok) {
            const post = await response.json();
            setPosts([post, ...posts]);
            setNewPost("");
            setMediaUrl("");
            setFile(null);
        }
    };

    const handleDeletePost = async (postId) => {
        const response = await fetch(`/posts/${postId}?email=${email}`, {
            method: "DELETE",
        });
        if (response.ok) {
            setPosts(posts.filter(post => post.id !== postId));
        }
    };

    const handleToggleLikePost = async (postId) => {
        const response = await fetch(`/posts/${postId}/like?email=${email}`, {
            method: "POST",
        });
        if (response.ok) {
            const updatedPost = await response.json();
            setPosts(posts.map(post => post.id === postId ? updatedPost : post));
        }
    };

    const handleAddComment = async (postId) => {
        const response = await fetch(`/posts/${postId}/comment?email=${email}&content=${newComment}`, {
            method: "POST",
        });
        if (response.ok) {
            const comment = await response.json();
            setComments({
                ...comments,
                [postId]: [...(comments[postId] || []), comment]
            });
            setNewComment("");
        }
    };

    const fetchComments = async (postId) => {
        const response = await fetch(`/posts/${postId}/comments`);
        if (response.ok) {
            const data = await response.json();
            setComments({
                ...comments,
                [postId]: data
            });
        }
    };

    const handleLogout = () => {
        logout();
        window.location.reload();
    };

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            minHeight="100vh"
            bgcolor="background.default"
            p={4}
            style={{ position: 'relative', overflow: 'hidden' }}
        >
            <Confetti width={width} height={height} />

            <Button
                variant="contained"
                color="secondary"
                component={Link}
                to="/profile"
                style={{
                    position: 'absolute',
                    top: 20,
                    left: 20,
                    zIndex: 10
                }}
            >
                Profile
            </Button>

            <Button
                variant="contained"
                color="secondary"
                onClick={handleLogout}
                style={{
                    position: 'absolute',
                    top: 20,
                    right: 20,
                    zIndex: 10
                }}
            >
                Logout
            </Button>

            <Typography
                variant="h2"
                component="h1"
                gutterBottom
                style={{ animation: 'fadeIn 2s ease-in-out' }}
            >
                Welcome to the Main Page!
            </Typography>

            <Container maxWidth="sm">
                <Paper elevation={3} style={{ padding: '20px', width: '100%', marginBottom: '20px' }}>
                    <TextField
                        label="What's on your mind?"
                        fullWidth
                        multiline
                        rows={4}
                        value={newPost}
                        onChange={(e) => setNewPost(e.target.value)}
                    />
                    <input
                        type="file"
                        onChange={handleFileChange}
                        style={{ marginTop: '10px' }}
                    />
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handlePostSubmit}
                        style={{ marginTop: '10px' }}
                    >
                        Post
                    </Button>
                </Paper>

                {posts.map((post) => (
                    <Paper key={post.id} elevation={3} style={{ padding: '20px', width: '100%', marginBottom: '10px' }}>
                        <Box display="flex" alignItems="center" mb={2}>
                            <Avatar src={post.userProfilePictureUrl} alt={post.userName} />
                            <Typography variant="h6" style={{ marginLeft: '10px' }}>
                                {post.userName}
                            </Typography>
                        </Box>
                        <Typography variant="body1">{post.content}</Typography>
                        {post.mediaUrl && (
                            <Box mt={2}>
                                {post.mediaUrl.match(/\.(jpeg|jpg|gif|png)$/) ? (
                                    <img src={post.mediaUrl} alt="media" style={{ maxWidth: '100%' }} />
                                ) : (
                                    <video controls style={{ maxWidth: '100%' }}>
                                        <source src={post.mediaUrl} type="video/mp4" />
                                        Your browser does not support the video tag.
                                    </video>
                                )}
                            </Box>
                        )}
                        <Typography variant="caption" color="textSecondary">
                            {new Date(post.createdAt).toLocaleString()}
                        </Typography>
                        <Box display="flex" alignItems="center" mt={2}>
                            <Button
                                variant="contained"
                                color="primary"
                                onClick={() => handleToggleLikePost(post.id)}
                                style={{ marginRight: '10px' }}
                            >
                                {post.likedBy.includes(email) ? "Unlike" : "Like"}
                            </Button>
                            <Typography variant="body2">{post.likes} Likes</Typography>
                        </Box>
                        <Box mt={2}>
                            <TextField
                                label="Add a comment"
                                fullWidth
                                value={newComment}
                                onChange={(e) => setNewComment(e.target.value)}
                            />
                            <Button
                                variant="contained"
                                color="primary"
                                onClick={() => handleAddComment(post.id)}
                                style={{ marginTop: '10px' }}
                            >
                                Comment
                            </Button>
                        </Box>
                        <Box mt={2}>
                            <Button
                                variant="contained"
                                color="secondary"
                                onClick={() => fetchComments(post.id)}
                                style={{ marginBottom: '10px' }}
                            >
                                Load Comments
                            </Button>
                            {comments[post.id] && comments[post.id].map((comment) => (
                                <Paper key={comment.id} elevation={1} style={{ padding: '10px', marginBottom: '10px' }}>
                                    <Typography variant="body2">
                                        <strong>{comment.user.email}</strong>: {comment.content}
                                    </Typography>
                                    <Typography variant="caption" color="textSecondary">
                                        {new Date(comment.createdAt).toLocaleString()}
                                    </Typography>
                                </Paper>
                            ))}
                        </Box>
                        {post.user.email === email && (
                            <Button
                                variant="contained"
                                color="secondary"
                                onClick={() => handleDeletePost(post.id)}
                                style={{ marginTop: '10px' }}
                            >
                                Delete
                            </Button>
                        )}
                    </Paper>
                ))}
            </Container>

            <Container maxWidth="md" style={{ marginTop: '20px' }}>
                <Paper elevation={3} style={{ padding: '20px', width: '100%' }}>
                    <Typography variant="h4" gutterBottom>
                        Chat
                    </Typography>
                    <Box display="flex">
                        <Box flex={1} style={{ marginRight: '20px' }}>
                            <ChatList onSelectConversation={setSelectedConversation} />
                        </Box>
                        <Box flex={2}>
                            {selectedConversation && (
                                <>
                                    <MessageView conversationId={selectedConversation} />
                                    <MessageInput conversationId={selectedConversation} senderId={1} socket={socket} />
                                </>
                            )}
                        </Box>
                    </Box>
                </Paper>
            </Container>
        </Box>
    );
};

export default MainPage;
