import React from "react";
import { Box, Typography } from "@mui/material";

const MainPage = () => {
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
            <Typography variant="h2" component="h1" gutterBottom>
                Welcome to the Main Page!
            </Typography>
            <Typography variant="h5" component="h2" gutterBottom>
                You are now logged in.
            </Typography>
        </Box>
    );
};

export default MainPage;