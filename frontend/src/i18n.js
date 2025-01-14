import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

// Tłumaczenia
const resources = {
    en: {
        translation: {
            welcome: "Welcome to the Main Page!",
            profile: "Profile Page",
            logout: "Logout",

        }
    },
    pl: {
        translation: {
            welcome: "Witaj na Stronie Głównej!",
            profile: "Strona Profilu",
            logout: "Wyloguj",

        }
    }
};

// Inicjalizacja i18next
i18n
    .use(initReactI18next)
    .init({
        resources,
        lng: "pl", // Domyślny język
        interpolation: {
            escapeValue: false
        }
    });

export default i18n;
