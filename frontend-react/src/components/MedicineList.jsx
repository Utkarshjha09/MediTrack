import React from 'react';

const MedicineList = ({ medicines, onEdit, onDelete, loading }) => {
  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  if (!medicines || medicines.length === 0) {
    return (
      <div className="card">
        <p className="text-center">No medicines found. Add some medicines to get started!</p>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="table-container">
        <table className="table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Type</th>
              <th>Manufacturer</th>
              <th>Price</th>
              <th>Stock</th>
              <th>Reorder Level</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {medicines.map((medicine) => (
              <tr key={medicine.medicineId}>
                <td>{medicine.medicineName}</td>
                <td>{medicine.type}</td>
                <td>{medicine.manufacturer}</td>
                <td>${medicine.pricePerUnit.toFixed(2)}</td>
                <td>
                  <span
                    className={`badge ${
                      medicine.stockQuantity <= medicine.reorderLevel
                        ? 'badge-danger'
                        : 'badge-success'
                    }`}
                  >
                    {medicine.stockQuantity}
                  </span>
                </td>
                <td>{medicine.reorderLevel}</td>
                <td>
                  <div className="table-actions">
                    <button
                      className="btn btn-secondary"
                      onClick={() => onEdit && onEdit(medicine)}
                    >
                      Edit
                    </button>
                    <button
                      className="btn btn-danger"
                      onClick={() => onDelete && onDelete(medicine.medicineId)}
                    >
                      Delete
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default MedicineList;
