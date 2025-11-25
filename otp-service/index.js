require('dotenv').config();
const express = require('express');
const cors = require('cors');
const nodemailer = require('nodemailer');

const app = express();
const PORT = process.env.PORT || 4000;
const OTP_TTL_MS = Number(process.env.OTP_TTL_MS || 5 * 60 * 1000);
const CLIENT_ORIGIN = process.env.CLIENT_ORIGIN || 'http://localhost:3000';

app.use(cors({ origin: CLIENT_ORIGIN }));
app.use(express.json());

const otpStore = new Map();

const transporter = nodemailer.createTransport({
  host: process.env.SMTP_HOST,
  port: Number(process.env.SMTP_PORT) || 587,
  secure: process.env.SMTP_SECURE === 'true',
  auth: {
    user: process.env.SMTP_USER,
    pass: process.env.SMTP_PASS,
  },
});

const generateCode = () => Math.floor(10000 + Math.random() * 90000).toString();

const maskEmail = (email) => {
  if (!email.includes('@')) return email;
  const [local, domain] = email.split('@');
  const maskedLocal = local.length > 2 ? `${local[0]}***${local.slice(-1)}` : `${local[0]}*`;
  return `${maskedLocal}@${domain}`;
};

const validateTransporter = async () => {
  try {
    await transporter.verify();
    console.log('SMTP connection verified.');
  } catch (error) {
    console.error('Unable to verify SMTP configuration:', error.message);
  }
};

validateTransporter();

app.post('/send-code', async (req, res) => {
  const { email } = req.body;

  if (!email) {
    return res.status(400).json({ message: 'Email is required.' });
  }

  const code = generateCode();
  const expiresAt = Date.now() + OTP_TTL_MS;
  otpStore.set(email, { code, expiresAt });

  try {
    await transporter.sendMail({
      from: process.env.EMAIL_FROM || process.env.SMTP_USER,
      to: email,
      subject: 'Your MediTrack verification code',
      text: `Your MediTrack verification code is ${code}. The code expires in ${Math.round(OTP_TTL_MS / 60000)} minutes.`,
      html: `<p>Your MediTrack verification code is <strong>${code}</strong>.</p><p>This code expires in ${Math.round(OTP_TTL_MS / 60000)} minutes.</p>`,
    });

    return res.json({ message: `Verification code sent to ${maskEmail(email)}.` });
  } catch (error) {
    console.error('Failed to send verification email:', error);
    return res.status(500).json({ message: 'Unable to send verification code right now.' });
  }
});

app.post('/verify-code', (req, res) => {
  const { email, code } = req.body;

  if (!email || !code) {
    return res.status(400).json({ message: 'Email and code are required.' });
  }

  const record = otpStore.get(email);

  if (!record) {
    return res.status(400).json({ valid: false, message: 'No code found for this email.' });
  }

  if (record.expiresAt < Date.now()) {
    otpStore.delete(email);
    return res.status(400).json({ valid: false, message: 'Code has expired. Please request a new one.' });
  }

  if (record.code !== code) {
    return res.status(400).json({ valid: false, message: 'Invalid code. Please try again.' });
  }

  otpStore.delete(email);
  return res.json({ valid: true, message: 'Code verified successfully.' });
});

setInterval(() => {
  const now = Date.now();
  otpStore.forEach((value, key) => {
    if (value.expiresAt < now) {
      otpStore.delete(key);
    }
  });
}, 60 * 1000);

app.listen(PORT, () => {
  console.log(`OTP service running on port ${PORT}`);
});
