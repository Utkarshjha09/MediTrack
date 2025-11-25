import React, { useState, useEffect } from 'react';
import MedicineList from '../components/MedicineList';
import Notification from '../components/Notification';
import Loader from '../components/Loader';
import { medicineAPI } from '../api';

const ViewMedicines = () => {
  const [medicines, setMedicines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [notification, setNotification] = useState(null);

  useEffect(() => {
    fetchMedicines();
  }, []);

  const fetchMedicines = async () => {
    try {
      setLoading(true);
      const response = await medicineAPI.getAll();
      setMedicines(response.data.data || []);
    } catch (error) {
      console.error('Error fetching medicines:', error);
      setNotification({
        message: 'Failed to load medicines',
        type: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (medicineId) => {
    if (!window.confirm('Are you sure you want to delete this medicine?')) {
      return;
    }

    try {
      await medicineAPI.delete(medicineId);
      setNotification({
        message: 'Medicine deleted successfully',
        type: 'success',
      });
      fetchMedicines(); // Refresh the list
    } catch (error) {
      console.error('Error deleting medicine:', error);
      setNotification({
        message: error.response?.data?.message || 'Failed to delete medicine',
        type: 'error',
      });
    }
  };

  const handleEdit = (medicine) => {
    console.log('Edit medicine:', medicine);
    setNotification({
      message: 'Edit functionality coming soon!',
      type: 'info',
    });
  };

  return (
    <div>
      <div className="flex-between mb-2">
        <h1>All Medicines</h1>
        <button type="button" className="btn-refresh" onClick={fetchMedicines}>
          <span role="img" aria-label="refresh">
            🔄
          </span>
          <span>Refresh</span>
        </button>
      </div>

      {notification && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={() => setNotification(null)}
        />
      )}

      <MedicineList
        medicines={medicines}
        loading={loading}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default ViewMedicines;
