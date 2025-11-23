import React from 'react';
import { useNavigate } from 'react-router-dom';
import NavTabs from './NavTabs';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
  const { logout } = useAuth();
  const navigate = useNavigate();
  
  const handleProfileClick = () => {
    console.log('Profile button clicked, navigating to /profile');
    navigate('/profile');
  };
  
  return (
    <nav className="navbar">
      <div className="navbar-content">
        <div className="navbar-brand">
          <span role="img" aria-label="pill">💊</span>
          <span>MediTrack</span>
        </div>
        <div className="navbar-actions">
          <NavTabs />
          <button 
            type="button" 
            className="nav-profile-btn" 
            onClick={handleProfileClick}
            aria-label="View Profile"
          >
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z"/>
            </svg>
          </button>
          <button type="button" className="nav-logout" onClick={logout}>
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
