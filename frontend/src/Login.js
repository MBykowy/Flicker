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
            const data = await response.json();
            alert("Login successful");
            navigate("/"); // Redirect to home page after successful login
        } else {
            alert("Login failed");
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                E-mail:
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </label>
            <br />
            <label>
                Hasło:
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
            </label>
            <br />
            <div className="g-recaptcha" data-sitekey="6LffRYYqAAAAAE6o2fZKI2PexiriDMok0AIF8DK5" data-callback={(response) => setCaptchaResponse(response)}></div>
            <br />
            <button type="submit">Zaloguj się</button>
        </form>
    );
}

export default Login;