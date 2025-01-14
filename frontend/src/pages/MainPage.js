import React, { useState, useEffect } from "react";
import { Box, Typography, TextField, Paper, Container, Avatar, Dialog, DialogTitle, DialogContent, DialogActions, Tabs, Tab, Button } from "@mui/material";
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
    const [editingPost, setEditingPost] = useState(null);
    const [editContent, setEditContent] = useState("");
    const [editMediaUrl, setEditMediaUrl] = useState("");
    const [comments, setComments] = useState({});
    const [newComment, setNewComment] = useState("");
    const [theme, setTheme] = useState("light");
    const [language, setLanguage] = useState("en");
    const [filter, setFilter] = useState('mostLikes');

    const [selectedUser, setSelectedUser] = useState(null);
    const [isFollowing, setIsFollowing] = useState(false);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [selectedTab, setSelectedTab] = useState("forYou");
    const [userDetails, setUserDetails] = useState({});
    useEffect(() => {
        const savedTheme = localStorage.getItem("theme");
        const savedLanguage = localStorage.getItem("language");

        if (savedTheme) {
            setTheme(savedTheme);
        } else {
            setTheme("light");
        }

        if (savedLanguage) {
            setLanguage(savedLanguage);
        } else {
            setLanguage("en");
        }
    }, []);

    useEffect(() => {
        document.body.style.backgroundColor = theme === "light" ? "lightblue" : "#333";
        document.body.style.color = theme === "light" ? "#000" : "#fff";
        localStorage.setItem("theme", theme);
    }, [theme]);

    useEffect(() => {
        localStorage.setItem("language", language);
    }, [language]);

    useEffect(() => {
        fetchPosts();
    }, [filter, isFollowing]);

    useEffect(() => {
        const emailFromCookie = document.cookie.split("; ").find(row => row.startsWith("email="))?.split("=")[1];
        setEmail(emailFromCookie);
    }, []);

    const fetchPosts = async () => {
        let url = '/posts';
        if (filter === 'mostLikes') {
            url = '/posts/sorted/likes/desc';
        } else if (filter === 'leastLikes') {
            url = '/posts/sorted/likes/asc';
        } else if (filter === 'newest') {
            url = '/posts/sorted/date/desc';
        } else if (filter === 'oldest') {
            url = '/posts/sorted/date/asc';
        } else if (filter === 'following') {
            url = `/posts/following?email=${email}`;
        }
        const response = await fetch(url);
        const data = await response.json();
        setPosts(data);
    };
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

    const handleEditPost = (post) => {
        setEditingPost(post);
        setEditContent(post.content);
        setEditMediaUrl(post.mediaUrl);
    };

    const handleEditSubmit = async () => {
        const response = await fetch(`/posts/${editingPost.id}?email=${email}&content=${editContent}&mediaUrl=${editMediaUrl}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (response.ok) {
            const updatedPost = await response.json();
            setPosts(posts.map(post => post.id === updatedPost.id ? updatedPost : post));
            setEditingPost(null);
            setEditContent("");
            setEditMediaUrl("");
        }
    };

    const handleLogout = () => {
        logout();
        window.location.reload();
    };

    const handleShowComments = async (postId) => {
        const response = await fetch(`/posts/${postId}/comments`);
        if (response.ok) {
            const data = await response.json();
            setComments(prevComments => ({ ...prevComments, [postId]: data }));
        }
    };

    const handleAddComment = async (postId) => {
        const trimmedComment = newComment.trim();
        const response = await fetch(`/posts/${postId}/comment?email=${email}`, {
            method: "POST",
            headers: {
                "Content-Type": "text/plain",
            },
            body: trimmedComment,
        });

        if (response.ok) {
            const commentsResponse = await fetch(`/posts/${postId}/comments`);
            if (commentsResponse.ok) {
                const data = await commentsResponse.json();
                setComments(prevComments => ({ ...prevComments, [postId]: data }));
            }
            setNewComment("");
        }
    };

    const toggleTheme = () => {
        setTheme((prevTheme) => (prevTheme === "light" ? "dark" : "light"));
    };

    const toggleLanguage = () => {
        setLanguage((prevLanguage) => (prevLanguage === "en" ? "pl" : "en"));
    };

    const handleUserClick = async (user) => {
        if (user.email !== email) {
            setSelectedUser(user);
            checkIfFollowing(user.email);
            const response = await fetch(`/api/user/details?email=${user.email}`);
            if (response.ok) {
                const data = await response.json();
                setUserDetails(data);
            }
            setDialogOpen(true);
        }
    };

    const checkIfFollowing = async (followedEmail) => {
        const response = await fetch(`/api/follow/check?followerEmail=${email}&followedEmail=${followedEmail}`);
        if (response.ok) {
            const isFollowing = await response.json();
            setIsFollowing(isFollowing);
        }
    };

    const handleFollowClick = async () => {
        const url = isFollowing ? "/api/follow/unfollow" : "/api/follow/follow";
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ followerEmail: email, followedEmail: selectedUser.email }),
        });

        if (response.ok) {
            setIsFollowing(!isFollowing);
        } else {
            console.error("Failed to follow/unfollow user");
        }
    };

    const handleCloseDialog = () => {
        setDialogOpen(false);
        setSelectedUser(null);
    };

    const handleTabChange = (event, newValue) => {
        setSelectedTab(newValue);
    };

    const filteredPosts = selectedTab === "following"
        ? posts.filter(post => post.user.followedBy && post.user.followedBy.includes(email))
        : posts;

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            minHeight="100vh"
            bgcolor={theme === "light" ? "lightblue" : "#333"}
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

            <Button
                variant="contained"
                color="default"
                onClick={toggleTheme}
                style={{
                    position: 'absolute',
                    bottom: 20,
                    right: 20,
                    zIndex: 10
                }}
            >
                Toggle {theme === "light" ? "Dark" : "Light"} Mode
            </Button>

            <Button
                variant="contained"
                color="primary"
                onClick={toggleLanguage}
                style={{
                    position: 'absolute',
                    bottom: 20,
                    left: 20,
                    zIndex: 10
                }}
            >
                {language === "en" ? "Change to Polish" : "Change to English"}
            </Button>

            <Typography
                variant="h2"
                component="h1"
                gutterBottom
                style={{ animation: 'fadeIn 2s ease-in-out' }}
            >
                {language === "en" ? "Welcome to the Main Page!" : "Witaj na Stronie Głównej!"}
            </Typography>

            <Container maxWidth="sm">
                <Tabs
                    value={selectedTab}
                    onChange={handleTabChange}
                    indicatorColor="primary"
                    textColor="primary"
                    centered
                >
                    <Tab label="For You" value="forYou" />
                    <Tab label="Following" value="following" />
                </Tabs>
                <Paper elevation={3} style={{ padding: '20px', width: '100%', marginBottom: '20px' }}>
                    <TextField
                        label={language === "en" ? "What's on your mind?" : "Co masz na myśli?"}
                        fullWidth
                        multiline
                        rows={4}
                        value={newPost}
                        onChange={(e) => setNewPost(e.target.value)}
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
                        {language === "en" ? "Post" : "Opublikuj"}
                    </Button>
                </Paper>

                <Box display="flex" justifyContent="center" mb={2}>
                    <Button onClick={() => setFilter('mostLikes')}>Najwięcej Like</Button>
                    <Button onClick={() => setFilter('leastLikes')}>Najmniej Like</Button>
                    <Button onClick={() => setFilter('newest')}>Najnowsze</Button>
                    <Button onClick={() => setFilter('oldest')}>Najstarsze</Button>
                </Box>

                {filteredPosts.map((post) => (
                    <Paper key={post.id} elevation={3} style={{ padding: '20px', width: '100%', marginBottom: '10px' }}>
                        <Box display="flex" alignItems="center" mb={2}>
                            <Avatar
                                src={post.user.picture}
                                alt={post.user.username}
                                onClick={() => handleUserClick(post.user)}
                                style={{ cursor: 'pointer' }}
                            />
                            <Typography
                                variant="h6"
                                style={{ marginLeft: '10px', cursor: 'pointer' }}
                                onClick={() => handleUserClick(post.user)}
                            >
                                {post.user.username}
                            </Typography>
                        </Box>
                        {editingPost && editingPost.id === post.id ? (
                            <>
                                <TextField
                                    label={language === "en" ? "Edit Content" : "Edytuj Treść"}
                                    fullWidth
                                    multiline
                                    rows={4}
                                    value={editContent}
                                    onChange={(e) => setEditContent(e.target.value)}
                                    style={{ marginBottom: '10px' }}
                                />
                                <TextField
                                    label={language === "en" ? "Edit Media URL" : "Edytuj URL Mediów"}
                                    fullWidth
                                    value={editMediaUrl}
                                    onChange={(e) => setEditMediaUrl(e.target.value)}
                                    style={{ marginBottom: '10px' }}
                                />
                                <Button
                                    variant="contained"
                                    color="primary"
                                    onClick={handleEditSubmit}
                                    style={{ marginRight: '10px' }}
                                >
                                    {language === "en" ? "Save" : "Zapisz"}
                                </Button>
                                <Button
                                    variant="contained"
                                    color="secondary"
                                    onClick={() => setEditingPost(null)}
                                >
                                    {language === "en" ? "Cancel" : "Anuluj"}
                                </Button>
                            </>
                        ) : (
                            <>
                                <Typography variant="body1">{post.content}</Typography>
                                {post.mediaUrl && (
                                    <Box mt={2}>
                                        {post.mediaUrl.match(/\.(jpeg|jpg|gif|png)$/) ? (
                                            <img src={post.mediaUrl} alt="media" style={{ maxWidth: '100%' }} />
                                        ) : (
                                            <video controls style={{ maxWidth: '100%' }}>
                                                <source src={post.mediaUrl} type="video/mp4" />
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
                                {post.user.email === email && (
                                    <>
                                        <Button
                                            variant="contained"
                                            color="primary"
                                            onClick={() => handleEditPost(post)}
                                            style={{ marginRight: '10px' }}
                                        >
                                            {language === "en" ? "Edit" : "Edytuj"}
                                        </Button>
                                        <Button
                                            variant="contained"
                                            color="secondary"
                                            onClick={() => handleDeletePost(post.id)}
                                        >
                                            {language === "en" ? "Delete" : "Usuń"}
                                        </Button>
                                    </>
                                )}
                                <Button
                                    variant="contained"
                                    color="primary"
                                    onClick={() => handleShowComments(post.id)}
                                    style={{ marginTop: '10px' }}
                                >
                                    {language === "en" ? "Comments" : "Komentarze"}
                                </Button>
                                {comments[post.id] && (
                                    <Box mt={2}>
                                        {comments[post.id].map(comment => (
                                            <Paper key={comment.id} style={{ padding: '10px', marginBottom: '10px' }}>
                                                <Typography variant="body2">{comment.content}</Typography>
                                                <Typography variant="caption" color="textSecondary">
                                                    {new Date(comment.createdAt).toLocaleString()}
                                                </Typography>
                                            </Paper>
                                        ))}
                                        <TextField
                                            label={language === "en" ? "Add a comment" : "Dodaj komentarz"}
                                            fullWidth
                                            multiline
                                            rows={2}
                                            value={newComment}
                                            onChange={(e) => setNewComment(e.target.value)}
                                            style={{ marginTop: '10px' }}
                                        />
                                        <Button
                                            variant="contained"
                                            color="primary"
                                            onClick={() => handleAddComment(post.id)}
                                            style={{ marginTop: '10px' }}
                                        >
                                            {language === "en" ? "Comment" : "Komentarz"}
                                        </Button>
                                    </Box>
                                )}
                            </>
                        )}
                    </Paper>
                ))}
            </Container>

            <Dialog open={dialogOpen} onClose={handleCloseDialog}>
                <DialogTitle>
                    <Box display="flex" alignItems="center">
                        <Avatar src={userDetails.picture} alt={userDetails.username} />
                        <Typography variant="h6" style={{ marginLeft: '10px' }}>
                            {userDetails.username}
                        </Typography>
                    </Box>
                </DialogTitle>
                <DialogContent>
                    <Typography>{userDetails.bio}</Typography>
                    <Typography>Followers: {userDetails.followersCount}</Typography>
                    <Typography>Following: {userDetails.followingCount}</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleFollowClick} color="primary">
                        {isFollowing ? "Unfollow" : "Follow"}
                    </Button>
                    <Button onClick={handleCloseDialog} color="secondary">
                        Block
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default MainPage;