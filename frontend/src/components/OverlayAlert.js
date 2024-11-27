// frontend/src/components/OverlayAlert.js
import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Button, Box, Typography, Modal } from "@mui/material";

const OverlayAlert = ({ open }) => {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        // Check if the user is logged in
        const token = localStorage.getItem("authToken");
        if (token) {
            setIsLoggedIn(true);
        } else {
            setIsLoggedIn(false);
        }
    }, []);

    const handleLoginRedirect = () => {
        navigate("/login");
    };

    return (
        <Modal
            open={open}
            aria-labelledby="modal-title"
            aria-describedby="modal-description"
            style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1300 }} // Center the Modal content
            BackdropProps={{ style: { pointerEvents: 'none' } }} // Disable backdrop click
        >
            <Box
                display="flex"
                flexDirection="column"
                alignItems="center"
                justifyContent="center"
                bgcolor="background.paper"
                p={4}
                boxShadow={24}
                textAlign="center"
                zIndex={1400} // Ensure the Box inside the Modal has a higher z-index
            >
                <Typography id="modal-title" variant="h4" component="h2" mb={2}>
                    You are not logged
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleLoginRedirect}
                    style={{ marginTop: 20, fontSize: '1.2rem' }}
                >
                    Go to Login Page
                </Button>
                <Typography variant="body2" mt={2}>
                    Don't have an account? <Link to="/register">Register here</Link>
                </Typography>
            </Box>
        </Modal>
    );
};

export default OverlayAlert;