import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import HomePage from ".//App";
import MainPage from "./pages/MainPage";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Profile from "./pages/Profile";

const App = () => {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/profile" element={<Profile />} />
                    <Route path="*" element={<ProtectedRoute />} />
                </Routes>
            </Router>
        </AuthProvider>
    );
};

const ProtectedRoute = () => {
    const { isAuthenticated } = useAuth();
    console.log("Is Authenticated:", isAuthenticated); // Debugging log

    return isAuthenticated ? <MainPage /> : <HomePage />;
};

ReactDOM.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>,
    document.getElementById('root')
);

export default App;