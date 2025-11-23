import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import MedicineForm from '../components/MedicineForm';
import Notification from '../components/Notification';
import Loader from '../components/Loader';
import { medicineAPI } from '../api';

const AddMedicine = () => {
  const navigate = useNavigate();
  const [notification, setNotification] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (formData) => {
    try {
      setLoading(true);
      const response = await medicineAPI.create(formData);
      
      if (response.data.success) {
        setNotification({
          message: 'Medicine added successfully!',
          type: 'success',
        });
        
        // Redirect to medicines list after 2 seconds
        setTimeout(() => {
          navigate('/medicines');
        }, 2000);
      }
    } catch (error) {
      console.error('Error adding medicine:', error);
      setNotification({
        message: error.response?.data?.message || 'Failed to add medicine',
        type: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1 className="mb-2">Add New Medicine</h1>

      {notification && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={() => setNotification(null)}
        />
      )}

      <div className="card">
        <div className="card-header">Medicine Information</div>
        <MedicineForm onSubmit={handleSubmit} />
        
        {loading && (
          <div className="loading">
            <div className="spinner"></div>
          </div>
        )}
      </div>
    </div>
  );
};

export default AddMedicine;
