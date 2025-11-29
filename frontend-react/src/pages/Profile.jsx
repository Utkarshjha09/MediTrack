import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { doc, getDoc, setDoc } from 'firebase/firestore';
import { db } from '../firebaseConfig';
import Loader from '../components/Loader';

const Profile = () => {
  const { currentUser } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    phone: '',
    role: 'Pharmacist',
    shopName: '',
    shopAddress: '',
    city: '',
    state: '',
    zipCode: '',
    shopPhone: '',
    shopEmail: '',
    licenseNumber: '',
    taxId: '',
    openingTime: '09:00',
    closingTime: '20:00',
    workingDays: 'Monday - Saturday',
  });

  useEffect(() => {
    const fetchUserProfile = async () => {
      if (currentUser) {
        try {
          const userDocRef = doc(db, 'users', currentUser.uid);
          const userDocSnap = await getDoc(userDocRef);
          
          if (userDocSnap.exists()) {
            const userData = userDocSnap.data();
            setFormData({
              fullName: userData.fullName || currentUser.displayName || '',
              email: currentUser.email || '',
              phone: userData.phone || '',
              role: userData.role || 'Pharmacist',
              shopName: userData.shopName || '',
              shopAddress: userData.shopAddress || '',
              city: userData.city || '',
              state: userData.state || '',
              zipCode: userData.zipCode || '',
              shopPhone: userData.shopPhone || '',
              shopEmail: userData.shopEmail || '',
              licenseNumber: userData.licenseNumber || '',
              taxId: userData.taxId || '',
              openingTime: userData.openingTime || '09:00',
              closingTime: userData.closingTime || '20:00',
              workingDays: userData.workingDays || 'Monday - Saturday',
            });
          } else {
            setFormData((prev) => ({
              ...prev,
              fullName: currentUser.displayName || '',
              email: currentUser.email || '',
            }));
          }
        } catch (error) {
          console.error('Error fetching user profile:', error);
        } finally {
          setIsLoading(false);
        }
      }
    };

    fetchUserProfile();
  }, [currentUser]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setIsSaving(true);
    
    try {
      if (currentUser) {
        const userDocRef = doc(db, 'users', currentUser.uid);
        await setDoc(userDocRef, {
          ...formData,
          updatedAt: new Date().toISOString(),
        }, { merge: true });
        
        setIsEditing(false);
        alert('Profile updated successfully!');
      }
    } catch (error) {
      console.error('Error saving profile:', error);
      alert('Failed to update profile. Please try again.');
    } finally {
      setIsSaving(false);
    }
  };

  const handleCancel = () => {
    setIsEditing(false);
  };

  if (isLoading) {
    return (
      <div className="profile-page">
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
          <Loader />
        </div>
      </div>
    );
  }

  return (
    <div className="profile-page">
      <div className="profile-header">
        <div className="profile-avatar">
          <svg viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z"/>
          </svg>
        </div>
        <div className="profile-header-info">
          <h1>{formData.fullName}</h1>
          <p className="profile-role">{formData.role}</p>
        </div>
        {!isEditing && (
          <button type="button" className="btn-edit-profile" onClick={() => setIsEditing(true)}>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
            </svg>
            Edit Profile
          </button>
        )}
      </div>

      <form onSubmit={handleSave} className="profile-form">
        {/* Personal Information Section */}
        <div className="profile-section">
          <h2 className="profile-section-title">Personal Information</h2>
          <div className="profile-grid">
            <div className="profile-field">
              <label htmlFor="fullName">Full Name</label>
              <input
                id="fullName"
                name="fullName"
                type="text"
                value={formData.fullName}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="email">Email Address</label>
              <input
                id="email"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="phone">Phone Number</label>
              <input
                id="phone"
                name="phone"
                type="tel"
                value={formData.phone}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="role">Role</label>
              <select
                id="role"
                name="role"
                value={formData.role}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              >
                <option value="Pharmacist">Pharmacist</option>
                <option value="Pharmacy Manager">Pharmacy Manager</option>
                <option value="Store Manager">Store Manager</option>
                <option value="Owner">Owner</option>
              </select>
            </div>
          </div>
        </div>

        {/* Shop Details Section */}
        <div className="profile-section">
          <h2 className="profile-section-title">Shop Details</h2>
          <div className="profile-grid">
            <div className="profile-field profile-field-full">
              <label htmlFor="shopName">Shop Name</label>
              <input
                id="shopName"
                name="shopName"
                type="text"
                value={formData.shopName}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field profile-field-full">
              <label htmlFor="shopAddress">Shop Address</label>
              <input
                id="shopAddress"
                name="shopAddress"
                type="text"
                value={formData.shopAddress}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="city">City</label>
              <input
                id="city"
                name="city"
                type="text"
                value={formData.city}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="state">State</label>
              <input
                id="state"
                name="state"
                type="text"
                value={formData.state}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="zipCode">ZIP Code</label>
              <input
                id="zipCode"
                name="zipCode"
                type="text"
                value={formData.zipCode}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="shopPhone">Shop Phone</label>
              <input
                id="shopPhone"
                name="shopPhone"
                type="tel"
                value={formData.shopPhone}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="shopEmail">Shop Email</label>
              <input
                id="shopEmail"
                name="shopEmail"
                type="email"
                value={formData.shopEmail}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="licenseNumber">License Number</label>
              <input
                id="licenseNumber"
                name="licenseNumber"
                type="text"
                value={formData.licenseNumber}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="taxId">Tax ID / EIN</label>
              <input
                id="taxId"
                name="taxId"
                type="text"
                value={formData.taxId}
                onChange={handleInputChange}
                disabled={!isEditing}
              />
            </div>
          </div>
        </div>

        {/* Business Hours Section */}
        <div className="profile-section">
          <h2 className="profile-section-title">Business Hours</h2>
          <div className="profile-grid">
            <div className="profile-field">
              <label htmlFor="openingTime">Opening Time</label>
              <input
                id="openingTime"
                name="openingTime"
                type="time"
                value={formData.openingTime}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field">
              <label htmlFor="closingTime">Closing Time</label>
              <input
                id="closingTime"
                name="closingTime"
                type="time"
                value={formData.closingTime}
                onChange={handleInputChange}
                disabled={!isEditing}
                required
              />
            </div>

            <div className="profile-field profile-field-full">
              <label htmlFor="workingDays">Working Days</label>
              <input
                id="workingDays"
                name="workingDays"
                type="text"
                value={formData.workingDays}
                onChange={handleInputChange}
                disabled={!isEditing}
                placeholder="e.g., Monday - Saturday"
                required
              />
            </div>
          </div>
        </div>

        {/* Action Buttons */}
        {isEditing && (
          <div className="profile-actions">
            <button type="button" className="btn-cancel" onClick={handleCancel} disabled={isSaving}>
              Cancel
            </button>
            <button type="submit" className="btn-save" disabled={isSaving}>
              {isSaving ? (
                <>
                  <Loader className="loader-small" />
                  <span>Saving...</span>
                </>
              ) : (
                'Save Changes'
              )}
            </button>
          </div>
        )}
      </form>
    </div>
  );
};

export default Profile;
