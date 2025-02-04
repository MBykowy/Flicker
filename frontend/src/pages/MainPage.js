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
    const [blockedUsers, setBlockedUsers] = useState([]); // State for blocked users


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
        fetchBlockedUsers();
    }, []);


    useEffect(() => {
        localStorage.setItem("language", language);
    }, [language]);


    useEffect(() => {
        const emailFromCookie = document.cookie.split("; ").find(row => row.startsWith("email="))?.split("=")[1];
        setEmail(emailFromCookie);
    }, []);

    useEffect(() => {
        fetchPosts();
    }, [filter, isFollowing]);

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
            await checkIfFollowing(user.email);
            const response = await fetch(`/api/user/details?email=${user.email}`);
            if (response.ok) {
                const data = await response.json();
                setUserDetails(data);
            }
            setDialogOpen(true);
        }
    };

    const handleBlockUser = async (blockedEmail) => {
        const emailFromCookie = document.cookie
            .split("; ")
            .find(row => row.startsWith("email="))
            ?.split("=")[1];

        if (!emailFromCookie) {
            console.error("Failed to retrieve blocker email from cookies");
            return;
        }

        try {
            const response = await fetch("/posts/block", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    blockerEmail: emailFromCookie,
                    blockedEmail
                }),
            });

            if (response.ok) {
                await fetchBlockedUsers();
                await fetchPosts(); // Refresh posts to remove blocked user's content
                // Optionally show success message
                alert("User blocked successfully");
            } else {
                throw new Error("Failed to block user");
            }
        } catch (error) {
            console.error("Error blocking user:", error);
            alert("Failed to block user. Please try again.");
        }
    };
    const fetchBlockedUsers = async () => {
        const emailFromCookie = document.cookie.split("; ").find(row => row.startsWith("email="))?.split("=")[1];
        if (!emailFromCookie) return;

        try {
            const response = await fetch(`/posts/blocked?email=${emailFromCookie}`);
            if (response.ok) {
                const blocked = await response.json();
                setBlockedUsers(blocked);
            }
        } catch (error) {
            console.error("Failed to fetch blocked users:", error);
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
        if (newValue === 'following') {
            setFilter('following');
        } else {
            setFilter('mostLikes'); // Default filter for other tabs
        }
    };

// Determine the filtered posts based on the selected tab (works)

    let filteredPosts;
    if (selectedTab === "following") {
        filteredPosts = posts.filter(post => {
            const isBlocked = blockedUsers.includes(post.user?.email);
            return post.user?.followedBy?.includes(email) && !isBlocked;
        });
    } else {
        filteredPosts = posts.filter(post => {
            return !blockedUsers.includes(post.user?.email);
        });
    }
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
                {language === "en" ? "Profile" : "Profil"}
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
                {language === "en" ? "Logout" : "Wyloguj się"}
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
                {language === "en" ? "Toggle Dark/Light Mode" : "Zmień tryb Dark/Light"}
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
                {language === "en" ? "Change to Polish" : "Zmień na Angielski"}
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
                    <Tab label={language === "en" ? "For You" : "Dla Ciebie"} value="forYou" />
                    <Tab label={language === "en" ? "Following" : "Obserwowani"} value="following" />
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
                    <Button onClick={() => setFilter('mostLikes')}>
                        {language === "en" ? "Most Likes" : "Najwięcej Like"}
                    </Button>
                    <Button onClick={() => setFilter('leastLikes')}>
                        {language === "en" ? "Least Likes" : "Najmniej Like"}
                    </Button>
                    <Button onClick={() => setFilter('newest')}>
                        {language === "en" ? "Newest" : "Najnowsze"}
                    </Button>
                    <Button onClick={() => setFilter('oldest')}>
                        {language === "en" ? "Oldest" : "Najstarsze"}
                    </Button>
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
                                        {post.likedBy.includes(email) ? (language === "en" ? "Unlike" : "Nie lubię") : (language === "en" ? "Like" : "Lubię")}
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
                                                <Typography variant="body2">
                                                    <strong>{comment.user.username}</strong>
                                                </Typography>
                                                <Typography variant="body2">
                                                    {comment.content}
                                                </Typography>
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
                    <Typography>{language === "en" ? "Followers:" : "Obserwujący:"} {userDetails.followersCount}</Typography>
                    <Typography>{language === "en" ? "Following:" : "Obserwujący:"} {userDetails.followingCount}</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleFollowClick} color="primary">
                        {isFollowing ? (language === "en" ? "Unfollow" : "Przestań obserwować") : (language === "en" ? "Follow" : "Obserwuj")}
                    </Button>
                    <Button onClick={() => handleBlockUser(selectedUser.email)} color="secondary">
                        {language === "en" ? "Block" : "Zablokuj"}
                    </Button>

                </DialogActions>
            </Dialog>
        </Box>
    );

};

export default MainPage;