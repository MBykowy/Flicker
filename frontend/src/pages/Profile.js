import React from "react";
import { Box, Typography, Avatar, TextField, Button } from "@mui/material";
import { Link } from "react-router-dom";

const Profile = () => {
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
                John Doe
            </Typography>
            <Typography variant="h6" component="h2" gutterBottom>
                johndoe@example.com
            </Typography>
            <TextField
                label="Bio"
                multiline
                rows={4}
                variant="outlined"
                fullWidth
                sx={{ mb: 4 }}
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