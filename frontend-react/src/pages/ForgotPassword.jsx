import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const ForgotPassword = () => {
  const [email, setEmail] = useState('');
  const [status, setStatus] = useState('');
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = (event) => {
    event.preventDefault();
    if (!email) {
      setError('Please enter your email address.');
      return;
    }

    setError('');
    setStatus('');
    setIsSubmitting(true);

    setTimeout(() => {
      setIsSubmitting(false);
      setStatus('If an account exists for this email, a reset link has been sent.');
    }, 1200);
  };

  return (
    <div className="forgot-page">
      <div className="forgot-card">
        <div className="two-step-window-dots" aria-hidden="true">
          <span />
          <span />
          <span />
        </div>

        <div className="forgot-logo">
          <img
            src="https://i.postimg.cc/SKSNJ5SQ/White-Letter-S-Logo-Instagram-Post.png"
            alt="Secure logo"
            onError={(event) => {
              event.currentTarget.src = 'https://placehold.co/128x128/161B22/FFFFFF?text=S';
            }}
          />
        </div>

        <h1>Forgot password</h1>
        <p className="forgot-subtitle">
          Enter the email associated with your MediTrack account and we'll send you a secure reset link.
        </p>

        <form className="forgot-form" onSubmit={handleSubmit}>
          <label htmlFor="reset-email">Email address</label>
          <div className="forgot-input-wrapper">
            <input
              id="reset-email"
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              placeholder="name@example.com"
              className="forgot-input"
            />
          </div>

          {error && <p className="forgot-error" role="alert">{error}</p>}
          {status && <p className="forgot-status">{status}</p>}

          <button type="submit" className="forgot-submit" disabled={isSubmitting}>
            {isSubmitting ? 'Sending reset link...' : 'Send reset link'}
          </button>
        </form>

        <div className="forgot-links">
          <Link to="/login">Back to sign in</Link>
          <Link to="/verify">Use two-step code</Link>
        </div>
      </div>
    </div>
  );
};

export default ForgotPassword;
