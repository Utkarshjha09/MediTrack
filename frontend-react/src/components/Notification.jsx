import React, { useEffect } from 'react';

const Notification = ({ message, type = 'info', onClose, duration = 5000 }) => {
  useEffect(() => {
    if (duration && onClose) {
      const timer = setTimeout(() => {
        onClose();
      }, duration);

      return () => clearTimeout(timer);
    }
  }, [duration, onClose]);

  if (!message) return null;

  return (
    <div className={`notification notification-${type}`}>
      <span>{message}</span>
      {onClose && (
        <button
          onClick={onClose}
          style={{
            marginLeft: 'auto',
            background: 'none',
            border: 'none',
            fontSize: '1.2rem',
            cursor: 'pointer',
          }}
        >
          ×
        </button>
      )}
    </div>
  );
};

export default Notification;
