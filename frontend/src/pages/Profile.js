import React, { useState, useEffect } from "react";
import {
    Box,
    Typography,
    Avatar,
    TextField,
    Button,
    Paper,
    Container,
    Grid,
    IconButton,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions
} from "@mui/material";
import {
    Edit as EditIcon,
    SaveAlt as SaveIcon,
    Close as CloseIcon
} from "@mui/icons-material";
import { Link } from "react-router-dom";

const Profile = () => {
    const [user, setUser] = useState({ username: "", email: "", bio: "", picture: "", followersCount: 0, followingCount: 0 });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isEditingPicture, setIsEditingPicture] = useState(false);
    const [newPictureUrl, setNewPictureUrl] = useState("");
    const [isEditingBio, setIsEditingBio] = useState(false);
    const [newBio, setNewBio] = useState("");
    const [isEditingUsername, setIsEditingUsername] = useState(false);
    const [newUsername, setNewUsername] = useState("");
    const [theme, setTheme] = useState("light");
    const [bannedUsers, setBannedUsers] = useState([]); // State for banned users

    const getEmailFromCookies = () => {
        const cookies = document.cookie.split("; ");
        const emailCookie = cookies.find((cookie) => cookie.startsWith("email="));
        return emailCookie ? decodeURIComponent(emailCookie.split("=")[1]) : null;
    };

    useEffect(() => {
        const savedTheme = localStorage.getItem("theme");
        if (savedTheme) {
            setTheme(savedTheme);
        } else {
            setTheme("light");
        }
    }, []);

    useEffect(() => {
        document.body.style.backgroundColor = theme === "light" ? "lightblue" : "#333";
        document.body.style.color = theme === "light" ? "#000" : "#fff";
    }, [theme]);

    useEffect(() => {
        const email = getEmailFromCookies();

        if (!email) {
            setError("Email not found in cookies.");
            setLoading(false);
            return;
        }

        fetch(`/api/user/details?email=${email}`)
            .then((response) => response.json())
            .then((data) => {
                setUser(data);
                setLoading(false);
            })
            .catch((err) => {
                setError("Failed to load user details.");
                setLoading(false);
            });
    }, []);

    useEffect(() => {
        const email = getEmailFromCookies();

        if (!email) {
            setError("Email not found in cookies.");
            setLoading(false);
            return;
        }

        fetch(`/api/user/details?email=${email}`)
            .then((response) => response.json())
            .then((data) => {
                setUser(data);
                setLoading(false);
            })
            .catch((err) => {
                setError("Failed to load user details.");
                setLoading(false);
            });

        fetchBannedUsers(email); // Fetch banned users
    }, []);

    const fetchBannedUsers = async (email) => {
        try {
            const response = await fetch(`/posts/blocked?email=${email}`);
            if (response.ok) {
                const data = await response.json();
                setBannedUsers(data);
            } else {
                throw new Error("Failed to fetch banned users");
            }
        } catch (error) {
            console.error("Error fetching banned users:", error);
        }
    };

    const unbanUser = async (blockedEmail) => {
        const email = getEmailFromCookies();

        if (!email) {
            console.error("Email not found in cookies.");
            setError("Email not found.");
            return;
        }

        console.log(`Attempting to unban user: ${blockedEmail} by ${email}`);

        try {
            const response = await fetch(`/posts/unblock?blockerEmail=${encodeURIComponent(email)}&blockedEmail=${encodeURIComponent(blockedEmail)}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (response.ok) {
                console.log(`Successfully unbanned user: ${blockedEmail}`);
                setBannedUsers(bannedUsers.filter(user => user !== blockedEmail));
            } else {
                const errorText = await response.text();
                console.error(`Failed to unban user: ${blockedEmail}. Server responded with: ${errorText}`);
                throw new Error("Failed to unban user");
            }
        } catch (error) {
            console.error("Error unbanning user:", error);
        }
    };
    const updatePicture = () => {
        const email = getEmailFromCookies();

        if (!email) {
            console.error("Email not found in cookies.");
            setError("Email not found.");
            return;
        }

        fetch(`/auth/update-picture`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                picture: newPictureUrl
            }),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(() => {
                setUser((prevUser) => ({ ...prevUser, picture: newPictureUrl }));
                setNewPictureUrl("");
                setIsEditingPicture(false);
            })
            .catch((error) => {
                console.error("Error updating picture:", error);
                setError("Failed to update picture.");
            });
    };

    const updateBio = () => {
        const email = getEmailFromCookies();

        if (!email) {
            console.error("Email not found in cookies.");
            setError("Email not found.");
            return;
        }

        fetch("/auth/update-bio", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: email, bio: newBio }),
        })
            .then((response) => response.json())
            .then(() => {
                setUser((prevUser) => ({ ...prevUser, bio: newBio }));
                setNewBio("");
                setIsEditingBio(false);
            })
            .catch(() => setError("Failed to update bio."));
    };

    const updateUsername = () => {
        const email = getEmailFromCookies();

        if (!email) {
            console.error("Email not found in cookies.");
            setError("Email not found.");
            return;
        }

        fetch("/auth/update-username", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: email, username: newUsername }),
        })
            .then((response) => response.json())
            .then(() => {
                setUser((prevUser) => ({ ...prevUser, username: newUsername }));
                setNewUsername("");
                setIsEditingUsername(false);
            })
            .catch(() => setError("Failed to update username."));
    };

    if (loading) return (
        <Container maxWidth="sm">
            <Typography variant="h6" align="center" sx={{ mt: 4 }}>
                Loading...
            </Typography>
        </Container>
    );

    if (error) return (
        <Container maxWidth="sm">
            <Typography variant="h6" color="error" align="center" sx={{ mt: 4 }}>
                Error: {error}
            </Typography>
        </Container>
    );

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ p: 4, mt: 4, borderRadius: 2 }}>
                <Grid container spacing={3} alignItems="center" direction="column">
                    <Grid item>
                        <Box position="relative">
                            <Avatar
                                src={user.picture}
                                sx={{
                                    width: 150,
                                    height: 150,
                                    border: '4px solid',
                                    borderColor: 'primary.main'
                                }}
                            />
                            <IconButton
                                color="primary"
                                sx={{
                                    position: 'absolute',
                                    bottom: 0,
                                    right: 0,
                                    bgcolor: 'background.paper',
                                    '&:hover': { bgcolor: 'action.hover' }
                                }}
                                onClick={() => setIsEditingPicture(true)}
                            >
                                <EditIcon />
                            </IconButton>
                        </Box>
                    </Grid>

                    <Grid item container justifyContent="center" alignItems="center" spacing={1}>
                        <Grid item>
                            <Typography variant="h5" color="text.primary">
                                {user.username || "No username set"}
                            </Typography>
                        </Grid>
                        <Grid item>
                            <IconButton
                                color="primary"
                                size="small"
                                onClick={() => setIsEditingUsername(true)}
                            >
                                <EditIcon fontSize="small" />
                            </IconButton>
                        </Grid>
                    </Grid>

                    <Grid item>
                        <Typography variant="body1" color="text.secondary">
                            {user.email}
                        </Typography>
                    </Grid>

                    <Grid item container justifyContent="center" alignItems="center" spacing={1}>
                        <Grid item>
                            <Typography variant="body1" color="text.secondary" align="center">
                                {user.bio || "No bio added yet"}
                            </Typography>
                        </Grid>
                        <Grid item>
                            <IconButton
                                color="primary"
                                size="small"
                                onClick={() => setIsEditingBio(true)}
                            >
                                <EditIcon fontSize="small" />
                            </IconButton>
                        </Grid>
                    </Grid>

                    <Grid item>
                        <Typography variant="body1" color="text.secondary">
                            Followers: {user.followersCount}
                        </Typography>
                    </Grid>

                    <Grid item>
                        <Typography variant="body1" color="text.secondary">
                            Following: {user.followingCount}
                        </Typography>
                    </Grid>

                    <Grid item>
                        <Typography variant="h6">Banned Users</Typography>
                        {bannedUsers.length > 0 ? (
                            <Box>
                                {bannedUsers.map((user) => (
                                    <Box key={user} display="flex" alignItems="center" justifyContent="space-between" mb={1}>
                                        <Typography>{user}</Typography>
                                        <Button variant="contained" color="secondary" onClick={() => unbanUser(user)}>
                                            Unban
                                        </Button>
                                    </Box>
                                ))}
                            </Box>
                        ) : (
                            <Typography>No banned users</Typography>
                        )}
                    </Grid>

                    <Dialog
                        open={isEditingPicture}
                        onClose={() => setIsEditingPicture(false)}
                        fullWidth
                        maxWidth="xs"
                    >
                        <DialogTitle>
                            Update Profile Picture
                            <IconButton
                                onClick={() => setIsEditingPicture(false)}
                                sx={{ position: 'absolute', right: 8, top: 8 }}
                            >
                                <CloseIcon />
                            </IconButton>
                        </DialogTitle>
                        <DialogContent>
                            <TextField
                                fullWidth
                                value={newPictureUrl}
                                onChange={(e) => setNewPictureUrl(e.target.value)}
                                label="New Picture URL"
                                variant="outlined"
                                margin="normal"
                            />
                        </DialogContent>
                        <DialogActions>
                            <Button
                                onClick={updatePicture}
                                color="primary"
                                startIcon={<SaveIcon />}
                            >
                                Save
                            </Button>
                        </DialogActions>
                    </Dialog>

                    <Dialog
                        open={isEditingUsername}
                        onClose={() => setIsEditingUsername(false)}
                        fullWidth
                        maxWidth="xs"
                    >
                        <DialogTitle>
                            Update Username
                            <IconButton
                                onClick={() => setIsEditingUsername(false)}
                                sx={{ position: 'absolute', right: 8, top: 8 }}
                            >
                                <CloseIcon />
                            </IconButton>
                        </DialogTitle>
                        <DialogContent>
                            <TextField
                                fullWidth
                                value={newUsername}
                                onChange={(e) => setNewUsername(e.target.value)}
                                label="New Username"
                                variant="outlined"
                                margin="normal"
                            />
                        </DialogContent>
                        <DialogActions>
                            <Button
                                onClick={updateUsername}
                                color="primary"
                                startIcon={<SaveIcon />}
                            >
                                Save
                            </Button>
                        </DialogActions>
                    </Dialog>

                    <Dialog
                        open={isEditingBio}
                        onClose={() => setIsEditingBio(false)}
                        fullWidth
                        maxWidth="xs"
                    >
                        <DialogTitle>
                            Update Bio
                            <IconButton
                                onClick={() => setIsEditingBio(false)}
                                sx={{ position: 'absolute', right: 8, top: 8 }}
                            >
                                <CloseIcon />
                            </IconButton>
                        </DialogTitle>
                        <DialogContent>
                            <TextField
                                fullWidth
                                value={newBio}
                                onChange={(e) => setNewBio(e.target.value)}
                                label="New Bio"
                                variant="outlined"
                                multiline
                                rows={4}
                                margin="normal"
                            />
                        </DialogContent>
                        <DialogActions>
                            <Button
                                onClick={updateBio}
                                color="primary"
                                startIcon={<SaveIcon />}
                            >
                                Save
                            </Button>
                        </DialogActions>
                    </Dialog>

                    <Grid item>
                        <Link to="/" style={{ textDecoration: 'none' }}>
                            <Button
                                variant="contained"
                                color="primary"
                            >
                                Powrót
                            </Button>
                        </Link>
                    </Grid>
                </Grid>
            </Paper>
        </Container>
    );
};

export default Profile;