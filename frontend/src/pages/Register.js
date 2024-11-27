import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { TextField, Button, Container, Box, Typography, Paper, Avatar } from "@mui/material";
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { styled } from "@mui/system";

const FadeInContainer = styled(Container)({
    animation: 'fadeIn 1s ease-in-out',
    '@keyframes fadeIn': {
        '0%': { opacity: 0 },
        '100%': { opacity: 1 },
    },
});

function Register() {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        const response = await fetch("/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ username, email, password })
        });
        if (response.ok) {
            alert("Registration successful");
            navigate("/"); // Redirect to home page after successful registration
        } else {
            alert("Registration failed");
        }
    };

    return (
        <FadeInContainer component="main" maxWidth="xs">
            <Paper elevation={6} style={{ padding: '20px', marginTop: '50px' }}>
                <Box display="flex" flexDirection="column" alignItems="center">
                    <Avatar style={{ margin: '10px', backgroundColor: 'secondary.main' }}>
                        <LockOutlinedIcon />
                    </Avatar>
                    <Typography variant="h5">Rejestracja</Typography>
                    <form onSubmit={handleSubmit} style={{ width: '100%', marginTop: '20px' }}>
                        <TextField
                            label="Nazwa użytkownika"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            fullWidth
                            margin="normal"
                            variant="outlined"
                        />
                        <TextField
                            label="E-mail"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            fullWidth
                            margin="normal"
                            variant="outlined"
                        />
                        <TextField
                            label="Hasło"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            fullWidth
                            margin="normal"
                            variant="outlined"
                        />
                        <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: '20px' }}>
                            Zarejestruj się
                        </Button>
                    </form>
                </Box>
            </Paper>
        </FadeInContainer>
    );
}

export default Register;