import React, { useState } from 'react';

const MedicineForm = ({ onSubmit, initialData = null, isEdit = false }) => {
  const [formData, setFormData] = useState(
    initialData || {
      medicineName: '',
      type: '',
      manufacturer: '',
      description: '',
      pricePerUnit: '',
      stockQuantity: '',
      reorderLevel: '',
    }
  );

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
      pricePerUnit: parseFloat(formData.pricePerUnit),
      stockQuantity: parseInt(formData.stockQuantity, 10),
      reorderLevel: parseInt(formData.reorderLevel, 10),
    };

    onSubmit(submitData);
  };

  return (
    <form className="form" onSubmit={handleSubmit}>
      <div className="form-group">
        <label className="form-label">Medicine Name *</label>
        <input
          type="text"
          name="medicineName"
          className="form-input"
          value={formData.medicineName}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label">Type *</label>
        <select
          name="type"
          className="form-select"
          value={formData.type}
          onChange={handleChange}
          required
        >
          <option value="">Select Type</option>
          <option value="Tablet">Tablet</option>
          <option value="Capsule">Capsule</option>
          <option value="Syrup">Syrup</option>
          <option value="Injection">Injection</option>
          <option value="Cream">Cream</option>
          <option value="Drops">Drops</option>
        </select>
      </div>

      <div className="form-group">
        <label className="form-label">Manufacturer *</label>
        <input
          type="text"
          name="manufacturer"
          className="form-input"
          value={formData.manufacturer}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label">Description</label>
        <textarea
          name="description"
          className="form-textarea"
          value={formData.description}
          onChange={handleChange}
        />
      </div>

      <div className="form-group">
        <label className="form-label">Price Per Unit *</label>
        <input
          type="number"
          step="0.01"
          name="pricePerUnit"
          className="form-input"
          value={formData.pricePerUnit}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label">Stock Quantity *</label>
        <input
          type="number"
          name="stockQuantity"
          className="form-input"
          value={formData.stockQuantity}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label">Reorder Level *</label>
        <input
          type="number"
          name="reorderLevel"
          className="form-input"
          value={formData.reorderLevel}
          onChange={handleChange}
          required
        />
      </div>

      <button type="submit" className="btn btn-primary">
        {isEdit ? 'Update Medicine' : 'Add Medicine'}
      </button>
    </form>
  );
};

export default MedicineForm;
