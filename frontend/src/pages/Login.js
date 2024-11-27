import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { TextField, Button, Container, Box, Typography } from "@mui/material";

function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [captchaToken, setCaptchaToken] = useState("");
    const navigate = useNavigate();

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
        <Container maxWidth="sm">
            <Box display="flex" flexDirection="column" alignItems="center" mt={5}>
                <Typography variant="h4" mb={2}>Logowanie</Typography>
                <form onSubmit={handleSubmit} style={{ width: '100%' }}>
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
                    <div id="captcha" style={{ marginTop: 20 }}></div>
                    <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: 20 }}>
                        Zaloguj się
                    </Button>
                </form>
            </Box>
        </Container>
    );
}

export default Login;