import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { TextField, Button, Container, Box, Typography } from "@mui/material";

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
        <Container maxWidth="sm">
            <Box display="flex" flexDirection="column" alignItems="center" mt={5}>
                <Typography variant="h4" mb={2}>Rejestracja</Typography>
                <form onSubmit={handleSubmit} style={{ width: '100%' }}>
                    <TextField
                        label="Nazwa użytkownika"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="E-mail"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Hasło"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        fullWidth
                        margin="normal"
                    />
                    <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: 20 }}>
                        Zarejestruj się
                    </Button>
                </form>
            </Box>
        </Container>
    );
}

export default Register;