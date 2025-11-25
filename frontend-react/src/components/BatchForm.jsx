import React, { useState, useEffect } from 'react';
import { medicineAPI, slotAPI } from '../api';

const BatchForm = ({ onSubmit, initialData = null, isEdit = false }) => {
  const [formData, setFormData] = useState(
    initialData || {
      batchNumber: '',
      medicineId: '',
      slotId: '',
      manufacturingDate: '',
      expiryDate: '',
      quantity: '',
      costPrice: '',
      sellingPrice: '',
    }
  );

  const [medicines, setMedicines] = useState([]);
  const [slots, setSlots] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [medicinesRes, slotsRes] = await Promise.all([
        medicineAPI.getAll(),
        slotAPI.getAll(),
      ]);
      setMedicines(medicinesRes.data.data || []);
      setSlots(slotsRes.data.data || []);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const submitData = {
      ...formData,
      medicineId: parseInt(formData.medicineId, 10),
      slotId: formData.slotId ? parseInt(formData.slotId, 10) : null,
      quantity: parseInt(formData.quantity, 10),
      costPrice: parseFloat(formData.costPrice),
      sellingPrice: parseFloat(formData.sellingPrice),
    };

    onSubmit(submitData);
  };

  if (loading) {
    return <div className="loading"><div className="spinner"></div></div>;
  }

  return (
    <form className="form" onSubmit={handleSubmit}>
      <div className="form-group">
        <label className="form-label">Batch Number *</label>
        <input
          type="text"
          name="batchNumber"
          className="form-input"
          value={formData.batchNumber}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label">Medicine *</label>
        <select
          name="medicineId"
          className="form-select"
          value={formData.medicineId}
          onChange={handleChange}
          required
        >
          <option value="">Select Medicine</option>
          {medicines.map((medicine) => (
            <option key={medicine.medicineId} value={medicine.medicineId}>
              {medicine.medicineName}
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label className="form-label">Storage Slot</label>
        <select
          name="slotId"
          className="form-select"
          value={formData.slotId}
          onChange={handleChange}
        >
          <option value="">Select Slot (Optional)</option>
          {slots.map((slot) => (
            <option key={slot.slotId} value={slot.slotId}>
              {slot.slotNumber} - {slot.location}
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label className="form-label">Manufacturing Date *</label>
        <input
          type="date"
          name="manufacturingDate"
          className="form-input"
          value={formData.manufacturingDate}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label">Expiry Date *</label>
        <input
          type="date"
          name="expiryDate"
          className="form-input"
          value={formData.expiryDate}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label">Quantity *</label>
        <input
          type="number"
          name="quantity"
          className="form-input"
          value={formData.quantity}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label">Cost Price *</label>
        <input
          type="number"
          step="0.01"
          name="costPrice"
          className="form-input"
          value={formData.costPrice}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label">Selling Price *</label>
        <input
          type="number"
          step="0.01"
          name="sellingPrice"
          className="form-input"
          value={formData.sellingPrice}
          onChange={handleChange}
          required
        />
      </div>

      <button type="submit" className="btn btn-primary">
        {isEdit ? 'Update Batch' : 'Add Batch'}
      </button>
    </form>
  );
};

export default BatchForm;
