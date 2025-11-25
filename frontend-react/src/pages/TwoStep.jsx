import React, { useEffect, useRef, useState } from 'react';
import { otpAPI } from '../api';

const CODE_LENGTH = 5;

const TwoStep = () => {
  const [code, setCode] = useState(Array(CODE_LENGTH).fill(''));
  const [focusedIndex, setFocusedIndex] = useState(0);
  const [email, setEmail] = useState('');
  const [sendMessage, setSendMessage] = useState('');
  const [sendError, setSendError] = useState('');
  const [verifyMessage, setVerifyMessage] = useState('');
  const [verifyError, setVerifyError] = useState('');
  const [isSending, setIsSending] = useState(false);
  const [isVerifying, setIsVerifying] = useState(false);
  const inputRefs = useRef([]);

  useEffect(() => {
    inputRefs.current[0]?.focus();
  }, []);

  const handleChange = (value, index) => {
    if (!/^\d?$/.test(value)) {
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

  const handleSendCode = async () => {
    if (!email) {
      setSendError('Enter your email address first.');
      return;
    }

    setIsSending(true);
    setSendError('');
    setSendMessage('');
    try {
      const response = await otpAPI.sendCode(email);
      setSendMessage(response.data?.message || 'Verification code sent.');
    } catch (error) {
      const message = error?.response?.data?.message || 'Unable to send verification code right now.';
      setSendError(message);
    } finally {
      setIsSending(false);
    }
  };

  const handleVerify = async () => {
    if (!email) {
      setVerifyError('Enter your email address first.');
      return;
    }

    if (!isComplete) {
      setVerifyError('Please enter the full 5-digit code.');
      return;
    }

    setIsVerifying(true);
    setVerifyError('');
    setVerifyMessage('');

    try {
      const response = await otpAPI.verifyCode(email, filledCode);
      if (response.data?.valid) {
        setVerifyMessage(response.data?.message || 'Code verified successfully.');
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

  const filledCode = code.join('');
  const isComplete = filledCode.length === CODE_LENGTH;

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

        <h1>Sign In with Two-Step Verification</h1>
        <p className="two-step-subtitle">Use the code we emailed you to keep your account secure.</p>

        <div className="two-step-email">
          <label htmlFor="two-step-email">Email address</label>
          <div className="two-step-email-row">
            <input
              id="two-step-email"
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              placeholder="name@example.com"
            />
            <button type="button" onClick={handleSendCode} disabled={isSending}>
              {isSending ? 'Sending...' : 'Send code'}
            </button>
          </div>
          {sendError && <p className="two-step-error" role="alert">{sendError}</p>}
          {sendMessage && <p className="two-step-success">{sendMessage}</p>}
        </div>

        <p className="two-step-instructions">Enter the 5-digit code</p>

        <div className="two-step-inputs" onPaste={handlePaste}>
          {code.map((value, index) => (
            <input
              key={`code-input-${index}`}
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
          Didn't receive a code?{' '}
          <button type="button" onClick={handleSendCode} disabled={isSending}>
            {isSending ? 'Sending...' : 'Resend code'}
          </button>
        </p>
      </div>
    </div>
  );
};

export default TwoStep;
