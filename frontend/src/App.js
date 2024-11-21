import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./App.css";

function App() {
    const [username, setUsername] = useState(null);
    const navigate = useNavigate();

    const handleLogin = async (email, password) => {
        const response = await fetch("/auth/user-login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });
        if (response.ok) {
            const data = await response.json();
            setUsername(data.username);
            navigate("/"); // Redirect to home page after login
        } else {
            alert("Login failed");
        }
    };

    const handleRegister = async (username, email, password) => {
        const response = await fetch("/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ username, email, password })
        });
        if (response.ok) {
            alert("Registration successful");
            navigate("/"); // Redirect to home page after registration
        } else {
            alert("Registration failed");
        }
    };

    const handleLogout = () => {
        setUsername(null);
        navigate("/"); // Redirect to home page after logout
    };

    return (
        <div className="App">
            <div className="buttons-container">
                {!username ? (
                    <>
                        <button className="login-btn" onClick={() => navigate("/login")}>
                            Logowanie
                        </button>
                        <button className="register-btn" onClick={() => navigate("/register")}>
                            Rejestracja
                        </button>
                    </>
                ) : (
                    <>
                        <span className="user-info">Zalogowano jako: {username}</span>
                        <button className="logout-btn" onClick={handleLogout}>
                            Wyloguj
                        </button>
                    </>
                )}
            </div>
            <div className="content">
                <h1>Flicker</h1>
            </div>
        </div>
    );
}

export default App;