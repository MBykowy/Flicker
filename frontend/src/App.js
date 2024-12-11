import React from "react";
import { Link } from "react-router-dom";
import { Box, Typography, Button } from "@mui/material";
import { styled } from "@mui/system";

const AnimatedBox = styled(Box)({
    animation: 'fadeIn 1s ease-in-out',
    '@keyframes fadeIn': {
        '0%': { opacity: 0 },
        '100%': { opacity: 1 },
    },
});

const App = () => {
    return (
        <AnimatedBox
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            minHeight="100vh"
            bgcolor="background.default"
            p={4}
        >
            <img src="https://gratisography.com/wp-content/uploads/2024/10/gratisography-cool-cat-800x525.jpg" alt="Fancy Image" style={{ width: '450px', marginBottom: '20px' }} />
            <Typography variant="h2" component="h1" gutterBottom>
               Witaj na Flicker!
            </Typography>
            <Typography variant="h5" component="h2" gutterBottom>
                Jeśli jesteś zainteresowany, zaloguj się lub zarejestruj!
            </Typography>
            <Box mt={4}>
                <Button variant="contained" color="primary" component={Link} to="/login">
                    Logowanie
                </Button>
                <Button variant="outlined" color="primary" component={Link} to="/register" style={{ marginLeft: 16 }}>
                    Rejestracja
                </Button>

            </Box>
        </AnimatedBox>
    );
};

export default App;