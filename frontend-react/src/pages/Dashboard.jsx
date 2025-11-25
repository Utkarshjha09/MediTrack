import React, { useEffect, useState } from 'react';
import { medicineAPI, batchAPI, slotAPI } from '../api';
import Notification from '../components/Notification';
import Loader from '../components/Loader';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalMedicines: 0,
    totalBatches: 0,
    totalSlots: 0,
    expiringBatches: 0,
  });
  const [expiringBatches, setExpiringBatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [notification, setNotification] = useState(null);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      const today = new Date();
      const thirtyDaysFromNow = new Date();
      thirtyDaysFromNow.setDate(today.getDate() + 30);
      
      const todayStr = today.toISOString().split('T')[0];
      const thirtyDaysStr = thirtyDaysFromNow.toISOString().split('T')[0];
      const [medicinesRes, batchesRes, slotsRes, expiringRes] = await Promise.all([
        medicineAPI.getAll(),
        batchAPI.getAll(),
        slotAPI.getAll(),
        batchAPI.getExpiringBetween(todayStr, thirtyDaysStr),
      ]);

      setStats({
        totalMedicines: medicinesRes.data.data?.length || 0,
        totalBatches: batchesRes.data.data?.length || 0,
        totalSlots: slotsRes.data.data?.length || 0,
        expiringBatches: expiringRes.data.data?.length || 0,
      });

      setExpiringBatches(expiringRes.data.data || []);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
      setNotification({
        message: 'Failed to load dashboard data',
        type: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const getDaysUntilExpiry = (expiryDate) => {
    const today = new Date();
    const expiry = new Date(expiryDate);
    const diffTime = expiry - today;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays;
  };

  const getExpiryBadgeClass = (daysLeft) => {
    if (daysLeft < 7) return 'badge-danger';
    if (daysLeft < 15) return 'badge-warning';
    return 'badge-info';
  };

  if (loading) {
    return (
      <div className="loading">
        <Loader />
      </div>
    );
  }

  return (
    <div>
      <h1 className="mb-2">Dashboard</h1>

      {notification && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={() => setNotification(null)}
        />
      )}

      <div className="dashboard-grid">
        <div className="stat-card">
          <div className="stat-card-icon">💊</div>
          <div className="stat-card-title">Total Medicines</div>
          <div className="stat-card-value">{stats.totalMedicines}</div>
        </div>

        <div className="stat-card">
          <div className="stat-card-icon">📦</div>
          <div className="stat-card-title">Total Batches</div>
          <div className="stat-card-value">{stats.totalBatches}</div>
        </div>

        <div className="stat-card">
          <div className="stat-card-icon">📍</div>
          <div className="stat-card-title">Storage Slots</div>
          <div className="stat-card-value">{stats.totalSlots}</div>
        </div>

        <div className="stat-card">
          <div className="stat-card-icon">⚠️</div>
          <div className="stat-card-title">Expiring Soon (30 days)</div>
          <div className="stat-card-value" style={{ color: 'var(--danger-color)' }}>
            {stats.expiringBatches}
          </div>
        </div>
      </div>

      {expiringBatches.length > 0 && (
        <div className="card">
          <h2 className="card-header">⚠️ Expiring Batches (Next 30 Days)</h2>
          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th>Batch Number</th>
                  <th>Medicine</th>
                  <th>Expiry Date</th>
                  <th>Days Left</th>
                  <th>Quantity</th>
                </tr>
              </thead>
              <tbody>
                {expiringBatches.map((batch) => {
                  const daysLeft = getDaysUntilExpiry(batch.expiryDate);
                  return (
                    <tr key={batch.batchId}>
                      <td>{batch.batchNumber}</td>
                      <td>{batch.medicineName}</td>
                      <td>{batch.expiryDate}</td>
                      <td>
                        <span className={`badge ${getExpiryBadgeClass(daysLeft)}`}>
                          {daysLeft} days
                        </span>
                      </td>
                      <td>{batch.quantity}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {expiringBatches.length === 0 && (
        <div className="card">
          <div className="notification notification-success">
            ✓ No batches expiring in the next 30 days!
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;
