import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";
function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        // Inicjalizacja CAPTCHA
        const loadRecaptcha = () => {
            if (window.grecaptcha) {
                window.grecaptcha.render("captcha", {
                    sitekey: "6LffRYYqAAAAAE6o2fZKI2PexiriDMok0AIF8DK5", // Twój klucz sitekey
                    callback: handleCaptchaSuccess,
                });
            }
        };

        // Załaduj CAPTCHA po załadowaniu komponentu
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
        // Obsługa sukcesu CAPTCHA
        setCaptchaToken(response);
    };

    const [captchaToken, setCaptchaToken] = useState("");

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
                    captchaResponse: captchaToken, // Przekazanie tokenu CAPTCHA na serwer
                }),
            });

            if (response.ok) {
                alert("Login successful");
                navigate("/"); // Przekierowanie po zalogowaniu
            } else {
                const errorText = await response.text();
                alert(`Login failed: ${errorText}`);
            }
        } catch (error) {
            console.error("Error during login:", error);
            alert("An error occurred during login.");
        }
    };

    return (
        <div>
            <h2>Logowanie</h2>
            <form onSubmit={handleSubmit}>
                {/* Pole E-mail */}
                <label htmlFor="email">E-mail:</label>
                <br />
                <input
                    type="email"
                    id="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <br />
                <br />

                {/* Pole Hasło */}
                <label htmlFor="password">Hasło:</label>
                <br />
                <input
                    type="password"
                    id="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <br />
                <br />

                {/* CAPTCHA */}
                <div id="captcha"></div>
                <br />

                {/* Przycisk logowania */}
                <button type="submit">Zaloguj się</button>
            </form>
        </div>
    );
}

export default Login;
