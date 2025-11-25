import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BatchForm from '../components/BatchForm';
import Notification from '../components/Notification';
import Loader from '../components/Loader';
import { batchAPI } from '../api';

const AddBatch = () => {
  const navigate = useNavigate();
  const [notification, setNotification] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (formData) => {
    try {
      setLoading(true);
      const response = await batchAPI.create(formData);
      
      if (response.data.success) {
        setNotification({
          message: 'Batch added successfully!',
          type: 'success',
        });
        setTimeout(() => {
          navigate('/');
        }, 2000);
      }
    } catch (error) {
      console.error('Error adding batch:', error);
      setNotification({
        message: error.response?.data?.message || 'Failed to add batch',
        type: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1 className="mb-2">Add New Batch</h1>

      {notification && (
        <Notification
          message={notification.message}
          type={notification.type}
          onClose={() => setNotification(null)}
        />
      )}

      <div className="card">
        <div className="card-header">Batch Information</div>
        <BatchForm onSubmit={handleSubmit} />
        
        {loading && (
          <div className="loading">
            <Loader />
          </div>
        )}
      </div>
    </div>
  );
};

export default AddBatch;
