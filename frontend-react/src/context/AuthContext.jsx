import React, { createContext, useContext, useMemo, useState } from 'react';

const AUTH_STORAGE_KEY = 'meditrack_auth_token';

const AuthContext = createContext({
  isAuthenticated: false,
  token: null,
  login: () => {},
  logout: () => {},
});

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem(AUTH_STORAGE_KEY));

  const login = (newToken = 'session-token') => {
    setToken(newToken);
    localStorage.setItem(AUTH_STORAGE_KEY, newToken);
  };

  const logout = () => {
    setToken(null);
    localStorage.removeItem(AUTH_STORAGE_KEY);
  };

  const value = useMemo(
    () => ({
      isAuthenticated: Boolean(token),
      token,
      login,
      logout,
    }),
    [token]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => useContext(AuthContext);
