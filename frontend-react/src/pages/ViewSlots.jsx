import React, { useState, useEffect, useCallback } from 'react';
import SlotView from '../components/SlotView';
import Notification from '../components/Notification';
import Loader from '../components/Loader';
import { slotAPI } from '../api';

const ViewSlots = () => {
  const [slots, setSlots] = useState([]);
  const [loading, setLoading] = useState(true);
  const [notification, setNotification] = useState(null);
  const [filter, setFilter] = useState('all');

  const fetchSlots = useCallback(async () => {
    try {
      setLoading(true);
      let response;
      
      if (filter === 'available') {
        response = await slotAPI.getAvailable();
      } else {
        response = await slotAPI.getAll();
      }
      
      setSlots(response.data.data || []);
    } catch (error) {
      console.error('Error fetching slots:', error);
      setNotification({
        message: 'Failed to load storage slots',
        type: 'error',
      });
    } finally {
      setLoading(false);
    }
  }, [filter]);

  useEffect(() => {
    fetchSlots();
  }, [fetchSlots]);

  return (
    <div>
      <div className="flex-between mb-2">
        <h1>Storage Slots</h1>
        <div className="flex gap-1">
          <select
            className="form-select"
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
            style={{ width: 'auto' }}
          >
            <option value="all">All Slots</option>
            <option value="available">Available Only</option>
          </select>
          <button type="button" className="btn-refresh" onClick={fetchSlots}>
            <span role="img" aria-label="refresh">
              🔄
            </span>
            <span>Refresh</span>
          </button>
        </div>
      </div>

      {notification && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={() => setNotification(null)}
        />
      )}

      <div className="card">
        <div className="card-header">
          Total Slots: {slots.length}
          {filter === 'available' && ' (Available)'}
        </div>
        <SlotView slots={slots} loading={loading} />
      </div>
    </div>
  );
};

export default ViewSlots;
