import React from 'react';

const SlotView = ({ slots, loading }) => {
  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  if (!slots || slots.length === 0) {
    return (
      <div className="card">
        <p className="text-center">No storage slots found.</p>
      </div>
    );
  }

  const getOccupancyPercentage = (slot) => {
    return ((slot.currentOccupancy / slot.capacity) * 100).toFixed(0);
  };

  return (
    <div className="slot-grid">
      {slots.map((slot) => (
        <div
          key={slot.slotId}
          className={`slot-card ${!slot.isAvailable ? 'unavailable' : ''}`}
        >
          <div className="slot-number">{slot.slotNumber}</div>
          <div className="slot-location">📍 {slot.location}</div>
          <div className="slot-capacity">
            <strong>Occupancy:</strong> {slot.currentOccupancy} / {slot.capacity}
          </div>
          <div className="slot-capacity">
            <strong>Usage:</strong> {getOccupancyPercentage(slot)}%
          </div>
          <div className="mt-1">
            <span
              className={`badge ${
                slot.isAvailable ? 'badge-success' : 'badge-danger'
              }`}
            >
              {slot.isAvailable ? 'Available' : 'Full'}
            </span>
          </div>
        </div>
      ))}
    </div>
  );
};

export default SlotView;
