'use client';

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  GoogleAuthProvider,
  signInWithEmailAndPassword,
  signInWithPopup,
} from 'firebase/auth';
import { auth } from '../firebaseConfig';
import Notification from '../components/Notification';
import { useAuth } from '../context/AuthContext';

const UserIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="24"
    height="24"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    className="login-icon"
  >
    <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2" />
    <circle cx="12" cy="7" r="4" />
  </svg>
);

const EyeIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="24"
    height="24"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    className="login-eye-icon"
  >
    <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z" />
    <circle cx="12" cy="12" r="3" />
  </svg>
);

const EyeOffIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="24"
    height="24"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    className="login-eye-icon"
  >
    <path d="M9.88 9.88a3 3 0 1 0 4.24 4.24" />
    <path d="M10.73 5.08A10.43 10.43 0 0 1 12 5c7 0 10 7 10 7a13.16 13.16 0 0 1-1.67 2.68" />
    <path d="M6.61 6.61A13.526 13.526 0 0 0 2 12s3 7 10 7a9.74 9.74 0 0 0 5.39-1.61" />
    <line x1="2" y1="2" x2="22" y2="22" />
  </svg>
);

const AppleIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" className="login-social-icon">
    <path
      fill="currentColor"
      d="M18.71 19.5C17.88 20.74 17 21.95 15.66 21.97C14.32 22 13.89 21.18 12.37 21.18C10.84 21.18 10.37 21.95 9.09997 22C7.78997 22.05 6.79997 20.68 5.95997 19.47C4.24997 17 2.93997 12.45 4.69997 9.39C5.56997 7.87 7.12997 6.91 8.81997 6.88C10.1 6.86 11.32 7.75 12.11 7.75C12.89 7.75 14.37 6.68 15.92 6.84C16.57 6.87 18.39 7.1 19.56 8.82C19.47 8.88 17.39 10.1 17.41 12.63C17.44 15.65 20.06 16.66 20.09 16.67C20.06 16.74 19.67 18.11 18.71 19.5ZM13 3.5C13.73 2.67 14.94 2.04 15.94 2C16.07 3.17 15.6 4.35 14.9 5.19C14.21 6.04 13.07 6.7 11.95 6.61C11.8 5.46 12.36 4.26 13 3.5Z"
    />
  </svg>
);

const GoogleIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" className="login-social-icon">
    <path
      d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
      fill="#4285F4"
    />
    <path
      d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
      fill="#34A853"
    />
    <path
      d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l3.66-2.84z"
      fill="#FBBC05"
    />
    <path
      d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
      fill="#EA4335"
    />
    <path d="M1 1h22v22H1z" fill="none" />
  </svg>
);

const XIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 24 24"
    fill="currentColor"
    className="login-social-icon"
  >
    <path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z" />
  </svg>
);

const googleProvider = new GoogleAuthProvider();

const Login = () => {
  const navigate = useNavigate();
  const { login: markAuthenticated } = useAuth();
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setNotification(null);

    if (!email || !password) {
      setNotification({
        message: 'Please enter both email and password.',
        type: 'warning',
      });
      return;
    }

    if (!auth) {
      setNotification({
        message: 'Authentication is not configured. Please set up Firebase credentials.',
        type: 'error',
      });
      return;
    }

    try {
      setLoading(true);
      const credential = await signInWithEmailAndPassword(auth, email.trim(), password);
      const token = credential.user?.accessToken || 'email-login';
      markAuthenticated(token);
      setNotification({ message: 'Signed in successfully!', type: 'success' });
      setTimeout(() => navigate('/'), 1200);
    } catch (error) {
      setNotification({
        message: error.message || 'Unable to sign in right now.',
        type: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleGoogleSignIn = async () => {
    setNotification(null);
    
    if (!auth) {
      setNotification({
        message: 'Authentication is not configured. Please set up Firebase credentials.',
        type: 'error',
      });
      return;
    }
    
    try {
      setLoading(true);
      const credential = await signInWithPopup(auth, googleProvider);
      const token = credential.user?.accessToken || 'google-login';
      markAuthenticated(token);
      setNotification({ message: 'Signed in with Google.', type: 'success' });
      setTimeout(() => navigate('/'), 1200);
    } catch (error) {
      setNotification({
        message: error.message || 'Google sign-in failed.',
        type: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <div className="login-card-header">
          <div className="login-icon-wrapper">
            <UserIcon />
          </div>
          <div>
            <h1>Welcome back</h1>
            <p>Enter your credentials to sign in</p>
          </div>
        </div>

        <div className="login-social-row">
          <button
            type="button"
            className="login-social-button"
            onClick={handleGoogleSignIn}
            disabled={loading}
          >
            <GoogleIcon />
          </button>
          <button type="button" className="login-social-button" disabled>
            <AppleIcon />
          </button>
          <button type="button" className="login-social-button" disabled>
            <XIcon />
          </button>
        </div>

        <div className="login-divider">
          <span>Or continue with</span>
        </div>

        {notification && (
          <Notification
            message={notification.message}
            type={notification.type}
            onClose={() => setNotification(null)}
          />
        )}

        <form className="login-form" onSubmit={handleSubmit}>
          <label htmlFor="login-email">Email</label>
          <input
            id="login-email"
            type="email"
            placeholder="name@example.com"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            disabled={loading}
          />

          <label htmlFor="login-password">Password</label>
          <div className="login-password-field">
            <input
              id="login-password"
              type={showPassword ? 'text' : 'password'}
              placeholder="Enter your password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              disabled={loading}
            />
            <button
              type="button"
              onClick={() => setShowPassword((prev) => !prev)}
              className="login-eye-toggle"
              aria-label={showPassword ? 'Hide password' : 'Show password'}
            >
              {showPassword ? <EyeOffIcon /> : <EyeIcon />}
            </button>
          </div>

          <button type="submit" className="btn btn-primary login-submit" disabled={loading}>
            {loading ? 'Signing In…' : 'Sign In'}
          </button>
        </form>

        <div className="login-footer">
          <p>
            Don&apos;t have an account?{' '}
            <button
              type="button"
              className="login-link"
              onClick={() => navigate('/signup')}
            >
              Sign up
            </button>
          </p>
          <button
            type="button"
            className="login-link"
            onClick={() => navigate('/forgot-password')}
          >
            Forgot your password?
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login;
