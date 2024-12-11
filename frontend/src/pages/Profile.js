// Import necessary libraries
import React, { useState, useEffect } from "react";
import { Box, Typography, Avatar, TextField, Button } from "@mui/material";
import { Link } from "react-router-dom";

const Profile = () => {
    const [user, setUser] = useState({ name: "", email: "", bio: "" });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Replace this email with the authenticated user's email in a real app
        const email = "johndoe@example.com";

        fetch(`/auth/user-details?email=${email}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then((data) => {
                setUser({
                    name: data.name,
                    email: data.email,
                    bio: data.bio || "", // Assuming bio is included in response
                });
                setLoading(false);
            })
            .catch((err) => {
                setError("Failed to load user details.");
                setLoading(false);
            });
    }, []);

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
            <Avatar
                alt="Profile Picture"
                src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Placeholder_no_text.svg/150px-Placeholder_no_text.svg.png"
                sx={{ width: 150, height: 150, mb: 2 }}
            />
            <Typography variant="h4" component="h1" gutterBottom>
                {user.name}
            </Typography>
            <Typography variant="h6" component="h2" gutterBottom>
                {user.email}
            </Typography>
            <TextField
                label="Bio"
                multiline
                rows={4}
                variant="outlined"
                fullWidth
                sx={{ mb: 4 }}
                value={user.bio}
                disabled // Assuming this field is not editable here
            />
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
            <Button
                variant="contained"
                color="primary"
                component={Link}
                to="/"
            >
                Go to Main Page
            </Button>
        </Box>
    );
};

export default Profile;
// This component fetches user details from the server and displays them on the page. It also includes a button to navigate back to the main page.