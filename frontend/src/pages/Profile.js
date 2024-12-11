import React, { useState, useEffect } from "react";
import { Box, Typography, Avatar, TextField, Button } from "@mui/material";
import { Link } from "react-router-dom";

const Profile = () => {
    const [user, setUser] = useState({ username: "", email: "", bio: "", picture: "" });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isEditingPicture, setIsEditingPicture] = useState(false);
    const [newPictureUrl, setNewPictureUrl] = useState("");
    const [isHovered, setIsHovered] = useState(false); // Stan do zarządzania hoverem na avatarze
    const [isEditingBio, setIsEditingBio] = useState(false); // Stan edytowania bio
    const [newBio, setNewBio] = useState(""); // Stan dla nowego bio

    useEffect(() => {
        const getEmailFromCookies = () => {
            const cookies = document.cookie.split("; ");
            const emailCookie = cookies.find((cookie) => cookie.startsWith("email="));
            return emailCookie ? decodeURIComponent(emailCookie.split("=")[1]) : null;
        };

        const email = getEmailFromCookies();

        if (!email) {
            setError("Email not found in cookies.");
            setLoading(false);
            return;
        }

        // Fetching user details using the email from cookies
        fetch(`/auth/user-details?email=${email}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then((data) => {
                // Ensure the email in the response matches the email in cookies
                if (data.email !== email) {
                    setError("Email mismatch between cookies and database.");
                    setLoading(false);
                    return;
                }

                // Populate user state with data received from the backend
                setUser({
                    username: data.username || "No Username Provided",  // Using 'username' from the backend
                    email: data.email || "No Email Provided",
                    bio: data.bio || "",
                    picture: data.picture || "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Placeholder_no_text.svg/150px-Placeholder_no_text.svg.png", // Default picture
                });
                setNewBio(data.bio || ""); // Set the current bio in the newBio state
                setLoading(false);
            })
            .catch((err) => {
                setError("Failed to load user details.");
                setLoading(false);
            });
    }, []);

    const handlePictureEdit = () => {
        setIsEditingPicture(true);
        setNewPictureUrl(user.picture); // Set current picture as default value
    };

    const handlePictureChange = () => {
        if (newPictureUrl && newPictureUrl !== user.picture) { // Sprawdzamy, czy URL jest różny od obecnego
            fetch(`/auth/update-picture?email=${user.email}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ picture: newPictureUrl }),
            })
                .then((response) => response.json())
                .then((data) => {
                    if (data.success) {
                        setUser({ ...user, picture: newPictureUrl });
                        setIsEditingPicture(false); // Po zapisaniu wychodzimy z trybu edycji
                    } else {
                        setError("Failed to update picture.");
                    }
                })
                .catch((err) => setError("Error updating picture"));
        } else {
            // Jeśli URL jest taki sam, nic się nie dzieje, ale i tak wychodzimy z trybu edycji
            setIsEditingPicture(false); // Wyłączenie trybu edycji, gdy nie dokonano zmian
            console.log("The picture URL has not been changed.");
        }
    };

    const handleBioEdit = () => {
        setIsEditingBio(true);
        setNewBio(user.bio); // Set the current bio as the default value
    };

    const handleBioSave = () => {
        if (newBio !== user.bio) {
            fetch(`/auth/update-bio?email=${user.email}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ bio: newBio }),
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error(`Failed to update bio: ${response.statusText}`);
                    }
                    return response.json();
                })
                .then((data) => {
                    if (data.success) {
                        setUser({ ...user, bio: newBio });
                        setIsEditingBio(false); // Exit edit mode after saving
                    } else {
                        setError("Failed to update bio. Server response: " + JSON.stringify(data));
                    }
                })
                .catch((err) => {
                    console.error("Error:", err); // Log error for debugging
                    setError("Error updating bio: " + err.message);
                });
        } else {
            // If bio is unchanged, exit edit mode without updating
            setIsEditingBio(false);
        }
    };


    if (loading) return <Typography>Loading...</Typography>;
    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            minHeight="100vh"
            bgcolor="background.default"
            p={4}
        >
            <Box
                position="relative"
                display="flex"
                flexDirection="column"
                alignItems="center"
                justifyContent="center"
                onMouseEnter={() => setIsHovered(true)} // Zmienia stan na true, gdy najedziesz na avatar
                onMouseLeave={() => setIsHovered(false)} // Zmienia stan na false, gdy opuścisz avatar
            >
                <Avatar
                    alt="Profile Picture"
                    src={user.picture}
                    sx={{ width: 150, height: 150, mb: 2 }}
                />
                {isEditingPicture ? (
                    <Box>
                        <TextField
                            label="New Picture URL"
                            value={newPictureUrl}
                            onChange={(e) => setNewPictureUrl(e.target.value)}
                            fullWidth
                            variant="outlined"
                            sx={{ mb: 2 }}
                        />
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handlePictureChange}
                        >
                            Save Picture
                        </Button>
                    </Box>
                ) : (
                    isHovered && ( // Przycisk pojawia się tylko gdy jest hover na avatarze
                        <Button
                            variant="contained"
                            color="primary"
                            sx={{
                                position: "absolute",
                                top: 10,
                                right: 10,
                                zIndex: 1,
                            }}
                            onClick={handlePictureEdit}
                        >
                            Edit
                        </Button>
                    )
                )}
            </Box>
            <Typography variant="h4" component="h1" gutterBottom>
                {user.username} {/* Wyświetlamy username pobrany z bazy danych */}
            </Typography>
            <Typography variant="h6" component="h2" gutterBottom>
                {user.email}
            </Typography>

            {/* Bio section */}
            {isEditingBio ? (
                <Box width="100%">
                    <TextField
                        label="Edit Bio"
                        multiline
                        rows={4}
                        variant="outlined"
                        fullWidth
                        value={newBio}
                        onChange={(e) => setNewBio(e.target.value)}
                        sx={{ mb: 4 }}
                    />
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleBioSave}
                    >
                        Save Bio
                    </Button>
                </Box>
            ) : (
                <Box>
                    <Typography variant="body1" gutterBottom>
                        {user.bio || "No bio available"}
                    </Typography>
                    <Button
                        variant="outlined"
                        color="primary"
                        onClick={handleBioEdit}
                    >
                        Edit Bio
                    </Button>
                </Box>
            )}

            <Box
                width="100%"
                height="300px"
                bgcolor="grey.200"
                display="flex"
                alignItems="center"
                justifyContent="center"
                mb={4}
            >
                <Typography variant="h6" color="textSecondary">
                    Posts will be displayed here
                </Typography>
            </Box>
            <Button variant="contained" color="primary" component={Link} to="/">
                Go to Main Page
            </Button>
        </Box>
    );
};

export default Profile;
