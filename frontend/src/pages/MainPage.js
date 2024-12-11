import React from "react";
import { Box, Typography, Button } from "@mui/material";
import Confetti from "react-confetti";
import { useWindowSize } from "react-use";
import { useAuth } from "../context/AuthContext";
import { Link } from "react-router-dom";

const MainPage = () => {
    const { width, height } = useWindowSize();
    const { logout } = useAuth();

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
            <Typography
                variant="h5"
                component="h2"
                gutterBottom
                style={{ animation: 'fadeIn 4s ease-in-out' }}
            >
                You are now logged in
            </Typography>
        </Box>
    );
};

export default MainPage;