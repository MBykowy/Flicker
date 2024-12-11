import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { TextField, Button, Container, Box, Typography, Paper, Avatar } from "@mui/material";
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { styled } from "@mui/system";
import { useAuth } from "../context/AuthContext";

const FadeInContainer = styled(Container)({
    animation: 'fadeIn 1s ease-in-out',
    '@keyframes fadeIn': {
        '0%': { opacity: 0 },
        '100%': { opacity: 1 },
    },
});

function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [captchaToken, setCaptchaToken] = useState("");
    const navigate = useNavigate();
    const { login } = useAuth();

    useEffect(() => {
        const loadRecaptcha = () => {
            if (window.grecaptcha) {
                window.grecaptcha.render("captcha", {
                    sitekey: "6LffRYYqAAAAAE6o2fZKI2PexiriDMok0AIF8DK5", // Your sitekey
                    callback: handleCaptchaSuccess,
                });
            }
        };

        const script = document.createElement("script");
        script.src = "https://www.google.com/recaptcha/api.js";
        script.async = true;
        script.defer = true;
        script.onload = loadRecaptcha;
        document.body.appendChild(script);

        return () => {
            document.body.removeChild(script);
        };
    }, []);

    const handleCaptchaSuccess = (response) => {
        console.log("CAPTCHA token:", response); // Log CAPTCHA token
        setCaptchaToken(response);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!captchaToken) {
            alert("Please complete the CAPTCHA.");
            return;
        }

        try {
            const response = await fetch("/auth/user-login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: email,
                    password: password,
                    captchaResponse: captchaToken,
                }),
            });

            if (response.ok) {
                alert("Login successful");
                login(); // Update authentication state
                document.cookie = `email=${email}; path=/; SameSite=Strict`; // Set email in cookies
                navigate("/"); // Redirect after login
            } else {
                const errorText = await response.text();
                alert(`Login failed: ${errorText}`);
                setCaptchaToken(""); // Reset CAPTCHA on error
                if (window.grecaptcha) {
                    window.grecaptcha.reset(); // Reset CAPTCHA in UI
                }
            }
        } catch (error) {
            console.error("Error during login:", error);
            alert("An error occurred during login.");
        }
    };

    return (
        <FadeInContainer component="main" maxWidth="xs">
            <Paper elevation={6} style={{ padding: '20px', marginTop: '50px' }}>
                <Box display="flex" flexDirection="column" alignItems="center">
                    <Avatar style={{ margin: '10px', backgroundColor: 'secondary.main' }}>
                        <LockOutlinedIcon />
                    </Avatar>
                    <Typography variant="h5">Logowanie</Typography>
                    <form onSubmit={handleSubmit} style={{ width: '100%', marginTop: '20px' }}>
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
                        <div id="captcha" style={{ marginTop: 20 }}></div>
                        <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: 20 }}>
                            Zaloguj się
                        </Button>
                    </form>
                </Box>
            </Paper>
        </FadeInContainer>
    );
}

export default Login;