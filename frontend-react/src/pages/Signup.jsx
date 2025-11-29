import React, { useMemo, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

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
  >
    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
    <circle cx="12" cy="7" r="4" />
  </svg>
);

const MailIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="20"
    height="20"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
  >
    <path d="M4 4h16a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2Z" />
    <polyline points="22 6 12 13 2 6" />
  </svg>
);

const LockIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="20"
    height="20"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
  >
    <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
    <circle cx="12" cy="16" r="1" />
    <path d="M7 11V7a5 5 0 0 1 10 0v4" />
  </svg>
);

const EyeIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="18"
    height="18"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
  >
    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8Z" />
    <circle cx="12" cy="12" r="3" />
  </svg>
);

const EyeOffIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="18"
    height="18"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
  >
    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20C5 20 1 12 1 12a18.49 18.49 0 0 1 5.06-5.94" />
    <path d="M9.9 4.24A9.1 9.1 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19" />
    <path d="M9.88 9.88a3 3 0 0 0 4.24 4.24" />
    <line x1="1" y1="1" x2="23" y2="23" />
  </svg>
);

const CheckIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="18"
    height="18"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
  >
    <polyline points="20 6 9 17 4 12" />
  </svg>
);

const ArrowRightIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="18"
    height="18"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
  >
    <path d="M5 12h14" />
    <path d="m12 5 7 7-7 7" />
  </svg>
);

const ArrowLeftIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="18"
    height="18"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
  >
    <path d="M19 12H5" />
    <path d="m12 19-7-7 7-7" />
  </svg>
);

const TOTAL_STEPS = 3;

const SignUp = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [phone, setPhone] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [step, setStep] = useState(1);

  const progressLabel = useMemo(() => Math.round((step / TOTAL_STEPS) * 100), [step]);

  const handleNext = () => {
    if (step < TOTAL_STEPS) {
      setStep((prev) => prev + 1);
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      navigate('/verify-signup', { state: { email, fullName, phone } });
    }, 1400);
  };

  return (
    <div className="signup-page">
      <div className="signup-card">
        <header className="signup-header">
          <div>
            <p className="signup-step-label">Step {step} of {TOTAL_STEPS}</p>
            <div className="signup-progress" aria-hidden="true">
              <span
                className="signup-progress-bar"
                style={{ width: `${progressLabel}%` }}
              />
            </div>
          </div>
          <p className="signup-progress-value">{progressLabel}%</p>
        </header>

        <section className="signup-intro">
          <span className="signup-intro-icon">
            <UserIcon />
          </span>
          <h1>Create account</h1>
          <p>
            {step === 1 && "Let's start with your basic information"}
            {step === 2 && 'Now, set up your credentials'}
            {step === 3 && 'Almost done! Review your details'}
          </p>
        </section>

        <form className="signup-form" onSubmit={handleSubmit}>
          {step === 1 && (
            <div className="signup-step">
              <label htmlFor="fullName">Full name</label>
              <div className="signup-input-wrapper">
                <input
                  id="fullName"
                  type="text"
                  value={fullName}
                  onChange={(event) => setFullName(event.target.value)}
                  placeholder="Enter your full name"
                  className="signup-input"
                />
                {fullName && (
                  <span className="signup-input-icon">
                    <CheckIcon />
                  </span>
                )}
              </div>
              
              <label htmlFor="phone">Phone number</label>
              <div className="signup-input-wrapper">
                <input
                  id="phone"
                  type="tel"
                  value={phone}
                  onChange={(event) => setPhone(event.target.value)}
                  placeholder="Enter your phone number"
                  className="signup-input"
                />
                {phone && (
                  <span className="signup-input-icon">
                    <CheckIcon />
                  </span>
                )}
              </div>
              
              <button
                type="button"
                className="signup-button"
                onClick={handleNext}
                disabled={!fullName || !phone}
              >
                Next step
                <ArrowRightIcon />
              </button>
            </div>
          )}

          {step === 2 && (
            <div className="signup-step">
              <label htmlFor="email">Email address</label>
              <div className="signup-input-wrapper">
                <span className="signup-input-leading">
                  <MailIcon />
                </span>
                <input
                  id="email"
                  type="email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  placeholder="name@example.com"
                  className="signup-input"
                />
              </div>

              <label htmlFor="password">Password</label>
              <div className="signup-input-wrapper">
                <span className="signup-input-leading">
                  <LockIcon />
                </span>
                <input
                  id="password"
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(event) => setPassword(event.target.value)}
                  placeholder="Create a password"
                  className="signup-input"
                />
                <button
                  type="button"
                  className="signup-input-icon"
                  onClick={() => setShowPassword((prev) => !prev)}
                  aria-label={showPassword ? 'Hide password' : 'Show password'}
                >
                  {showPassword ? <EyeOffIcon /> : <EyeIcon />}
                </button>
              </div>

              <button
                type="button"
                className="signup-button"
                onClick={handleNext}
                disabled={!email || !password}
              >
                Next step
                <ArrowRightIcon />
              </button>
            </div>
          )}

          {step === 3 && (
            <div className="signup-step">
              <div className="signup-review">
                <header>
                  <CheckIcon />
                  <span>Review details</span>
                </header>
                <ul>
                  <li>
                    <span>Name</span>
                    <strong>{fullName}</strong>
                  </li>
                  <li>
                    <span>Phone</span>
                    <strong>{phone}</strong>
                  </li>
                  <li>
                    <span>Email</span>
                    <strong>{email}</strong>
                  </li>
                  <li>
                    <span>Password</span>
                    <strong>••••••••</strong>
                  </li>
                </ul>
              </div>

              <button type="submit" className="signup-button" disabled={isLoading}>
                {isLoading ? 'Creating account…' : 'Create account'}
              </button>
            </div>
          )}
        </form>

        {step > 1 && (
          <button
            type="button"
            className="signup-back-button"
            onClick={() => setStep((prev) => Math.max(prev - 1, 1))}
          >
            <ArrowLeftIcon />
            Back to previous step
          </button>
        )}

        <footer className="signup-footer">
          <p>
            Already have an account? <Link to="/login">Sign in</Link>
          </p>
        </footer>
      </div>
    </div>
  );
};

export default SignUp;
