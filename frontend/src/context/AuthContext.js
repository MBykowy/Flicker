import React, { createContext, useState, useContext, useEffect } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(() => {
        // Retrieve the initial state from localStorage
        const savedState = localStorage.getItem("isAuthenticated");
        return savedState ? JSON.parse(savedState) : false;
    });

    useEffect(() => {
        const checkAuth = async () => {
            try {
                const response = await fetch("/auth/check-auth", {
                    method: "GET",
                    credentials: "include",  // Ensure cookies are sent
                });
                if (response.ok) {
                    const isAuthenticated = await response.json();
                    console.log("Authentication status:", isAuthenticated); // Debugging log
                    setIsAuthenticated(isAuthenticated);
                    localStorage.setItem("isAuthenticated", JSON.stringify(isAuthenticated));
                } else {
                    setIsAuthenticated(false);
                    localStorage.setItem("isAuthenticated", JSON.stringify(false));
                }
            } catch (error) {
                console.error("Error checking authentication:", error);
                setIsAuthenticated(false);
                localStorage.setItem("isAuthenticated", JSON.stringify(false));
            }
        };
        checkAuth();
    }, []); // Empty dependency array so this runs once when component mounts

    const login = () => {
        setIsAuthenticated(true);
        localStorage.setItem("isAuthenticated", JSON.stringify(true));
    };

    const logout = async () => {
        try {
            // Optional: Send a request to the server to handle server-side logout
            const response = await fetch("/auth/logout", {
                method: "POST",
                credentials: "include", // Ensures cookies are sent (for session)
            });

            if (response.ok) {
                // If logout was successful on the server, clear the client-side state
                setIsAuthenticated(false);
                localStorage.removeItem("isAuthenticated");
            } else {
                console.error("Error logging out on server side");
            }
        } catch (error) {
            console.error("Error logging out:", error);
        }
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
