import React, { useEffect, useRef, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { otpAPI } from '../api';
import { useAuth } from '../context/AuthContext';

const CODE_LENGTH = 5;

const SignupVerification = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { login } = useAuth();
  const emailFromSignup = location.state?.email || '';
  const [code, setCode] = useState(Array(CODE_LENGTH).fill(''));
  const [focusedIndex, setFocusedIndex] = useState(0);
  const [sendMessage, setSendMessage] = useState('');
  const [sendError, setSendError] = useState('');
  const [verifyMessage, setVerifyMessage] = useState('');
  const [verifyError, setVerifyError] = useState('');
  const [isSending, setIsSending] = useState(false);
  const [isVerifying, setIsVerifying] = useState(false);
  const inputRefs = useRef([]);

  const filledCode = code.join('');
  const isComplete = filledCode.length === CODE_LENGTH;

  useEffect(() => {
    if (!emailFromSignup) {
      navigate('/signup', { replace: true });
      return;
    }

    inputRefs.current[0]?.focus();
    sendCode();
  }, [emailFromSignup]);

  const sendCode = async () => {
    if (!emailFromSignup) {
      return;
    }

    setIsSending(true);
    setSendError('');
    setSendMessage('');

    try {
      const response = await otpAPI.sendCode(emailFromSignup);
      setSendMessage(response.data?.message || 'Verification code sent.');
    } catch (error) {
      const message = error?.response?.data?.message || 'Unable to send verification code right now.';
      setSendError(message);
    } finally {
      setIsSending(false);
    }
  };

  const handleChange = (value, index) => {
    if (!/^[0-9]?$/g.test(value)) {
      return;
    }

    const nextCode = [...code];
    nextCode[index] = value;
    setCode(nextCode);

    if (value && index < CODE_LENGTH - 1) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  const handleKeyDown = (event, index) => {
    if (event.key === 'Backspace' && !code[index] && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
  };

  const handlePaste = (event) => {
    event.preventDefault();
    const pasted = event.clipboardData.getData('text').replace(/\D/g, '').slice(0, CODE_LENGTH);

    if (!pasted) {
      return;
    }

    const nextCode = Array(CODE_LENGTH)
      .fill('')
      .map((_, idx) => pasted[idx] || '');

    setCode(nextCode);

    const lastFilledIndex = Math.min(pasted.length, CODE_LENGTH) - 1;
    if (lastFilledIndex >= 0) {
      inputRefs.current[lastFilledIndex]?.focus();
    }
  };

  const handleVerify = async () => {
    if (!isComplete) {
      setVerifyError('Please enter the full 5-digit code.');
      return;
    }

    setIsVerifying(true);
    setVerifyError('');
    setVerifyMessage('');

    try {
      const response = await otpAPI.verifyCode(emailFromSignup, filledCode);
      if (response.data?.valid) {
        setVerifyMessage(response.data?.message || 'Code verified successfully.');
        login('verified-user-token');
        setTimeout(() => {
          navigate('/', { replace: true });
        }, 1000);
      } else {
        setVerifyError(response.data?.message || 'Invalid code.');
      }
    } catch (error) {
      const message = error?.response?.data?.message || 'Unable to verify the code right now.';
      setVerifyError(message);
    } finally {
      setIsVerifying(false);
    }
  };

  const obfuscatedEmail = emailFromSignup.replace(/(^.).+(@.+$)/, (_, first, domain) => `${first}***${domain}`);

  return (
    <div className="two-step-page">
      <div className="two-step-card">
        <div className="two-step-window-dots" aria-hidden="true">
          <span />
          <span />
          <span />
        </div>

        <div className="two-step-logo">
          <img
            src="https://i.postimg.cc/SKSNJ5SQ/White-Letter-S-Logo-Instagram-Post.png"
            alt="Secure logo"
            onError={(event) => {
              event.currentTarget.src = 'https://placehold.co/128x128/161B22/FFFFFF?text=S';
            }}
          />
        </div>

        <h1>Verify your new account</h1>
        <p className="two-step-subtitle">We sent a 5-digit code to {obfuscatedEmail}.</p>

        {sendError && <p className="two-step-error" role="alert">{sendError}</p>}
        {sendMessage && <p className="two-step-success">{sendMessage}</p>}

        <div className="two-step-inputs" onPaste={handlePaste}>
          {code.map((value, index) => (
            <input
              key={`signup-code-input-${index}`}
              ref={(element) => {
                inputRefs.current[index] = element;
              }}
              type="tel"
              inputMode="numeric"
              maxLength={1}
              placeholder="•"
              value={value}
              aria-label={`Digit ${index + 1}`}
              onChange={(event) => handleChange(event.target.value, index)}
              onFocus={() => setFocusedIndex(index)}
              onBlur={() => setFocusedIndex(-1)}
              onKeyDown={(event) => handleKeyDown(event, index)}
              className={`two-step-input ${focusedIndex === index ? 'focused' : ''}`}
            />
          ))}
        </div>

        {verifyError && <p className="two-step-error" role="alert">{verifyError}</p>}
        {verifyMessage && <p className="two-step-success">{verifyMessage}</p>}

        <button
          type="button"
          className="two-step-verify"
          onClick={handleVerify}
          disabled={!isComplete || isVerifying}
        >
          {isVerifying ? 'Verifying...' : 'Verify code'}
        </button>

        <p className="two-step-resend">
          Need a new code?{' '}
          <button type="button" onClick={sendCode} disabled={isSending}>
            {isSending ? 'Sending...' : 'Resend code'}
          </button>
        </p>

        <button type="button" className="two-step-secondary" onClick={() => navigate('/login')}>
          Back to login
        </button>
      </div>
    </div>
  );
};

export default SignupVerification;
