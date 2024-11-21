import React, { useState } from "react";
import "./App.css";

function App() {
  // Symulacja stanu użytkownika (zalogowany/niezalogowany)
  const [username, setUsername] = useState(null);

  // Funkcje obsługujące logowanie/rejestrację/wylogowanie
  const handleLogin = () => {
    // W prawdziwej aplikacji należy tu umieścić logikę logowania
    setUsername("Jan Kowalski");
  };

  const handleRegister = () => {
    // Przekierowanie do formularza rejestracji
    alert("Przejdź do rejestracji!");
  };

  const handleLogout = () => {
    setUsername(null);
  };

  return (
      <div className="App">
        <div className="buttons-container">
          {/* Przyciski logowania/rejestracji */}
          {!username ? (
              <>
                <button className="login-btn" onClick={handleLogin}>
                  Logowanie
                </button>
                <button className="register-btn" onClick={handleRegister}>
                  Rejestracja
                </button>
              </>
          ) : (
              <>
                {/* Informacja o zalogowanym użytkowniku */}
                <span className="user-info">Zalogowano jako: {username}</span>
                <button className="logout-btn" onClick={handleLogout}>
                  Wyloguj
                </button>
              </>
          )}
        </div>

        {/* Treść główna */}
        <div className="content">
          <h1>Flicker</h1>
        </div>
      </div>
  );
}

export default App;
