import React from "react";
import { Box, Typography } from "@mui/material";
import Confetti from "react-confetti";
import { useWindowSize } from "react-use";

const MainPage = () => {
    const { width, height } = useWindowSize();

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