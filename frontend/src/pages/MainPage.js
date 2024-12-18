import React, { useState, useEffect } from "react";
import { Box, Typography, Button, TextField, Paper, Container } from "@mui/material";
import Confetti from "react-confetti";
import { useWindowSize } from "react-use";
import { useAuth } from "../context/AuthContext";
import { Link } from "react-router-dom";

const MainPage = () => {
    const { width, height } = useWindowSize();
    const { logout } = useAuth();
    const [posts, setPosts] = useState([]);
    const [newPost, setNewPost] = useState("");
    const [mediaUrl, setMediaUrl] = useState("");
    const [file, setFile] = useState(null);
    const [email, setEmail] = useState("");

    useEffect(() => {
        const fetchPosts = async () => {
            const response = await fetch(`/posts/user/${email}`);
            const data = await response.json();
            setPosts(data);
        };

        const emailFromCookie = document.cookie.split("; ").find(row => row.startsWith("email="))?.split("=")[1];
        setEmail(emailFromCookie);
        if (emailFromCookie) {
            fetchPosts();
        }
    }, [email]);

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
                    <TextField
                        label="Media URL"
                        fullWidth
                        value={mediaUrl}
                        onChange={(e) => setMediaUrl(e.target.value)}
                        style={{ marginTop: '10px' }}
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
                        <Button
                            variant="contained"
                            color="secondary"
                            onClick={() => handleDeletePost(post.id)}
                            style={{ marginTop: '10px' }}
                        >
                            Delete
                        </Button>
                    </Paper>
                ))}
            </Container>
        </Box>
    );
};

export default MainPage;