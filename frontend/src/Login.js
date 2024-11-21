import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [captchaResponse, setCaptchaResponse] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        const response = await fetch("/auth/user-login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password, "g-recaptcha-response": captchaResponse })
        });
        if (response.ok) {
            alert("Login successful");
            navigate("/"); // Redirect to home page after successful login
        } else {
            alert("Login failed");
        }
    };

    return (
        <div>
            <h2>Logowanie</h2>
            <form onSubmit={handleSubmit}>
                <label htmlFor="email">E-mail:</label><br />
                <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} required /><br /><br />

                <label htmlFor="password">Hasło:</label><br />
                <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)} required /><br /><br />

                <div className="g-recaptcha" data-sitekey="6LffRYYqAAAAAE6o2fZKI2PexiriDMok0AIF8DK5" data-callback={(response) => setCaptchaResponse(response)}></div><br />

                <button type="submit">Zaloguj się</button>
            </form>
        </div>
    );
}

export default Login;