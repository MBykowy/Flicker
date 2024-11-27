// frontend/src/pages/HomePage.js
import React, { useEffect, useState } from "react";
import { Container, Box, Typography } from "@mui/material";
import OverlayAlert from "./components/OverlayAlert";

const HomePage = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [open, setOpen] = useState(false);

    useEffect(() => {
        // Check if the user is logged in
        const token = localStorage.getItem("authToken");
        if (token) {
            setIsLoggedIn(true);
            setOpen(false);
        } else {
            setIsLoggedIn(false);
            setOpen(true);
        }
    }, []);

    return (
        <Container>
            <Box display="flex" flexDirection="column" alignItems="center" mt={5}>
                <Typography variant="h4" mb={2}>Home Page</Typography>
                <OverlayAlert open={open} />
            </Box>
        </Container>
    );
};

export default HomePage;