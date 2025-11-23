import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import AddMedicine from './pages/AddMedicine';
import ViewMedicines from './pages/ViewMedicines';
import ViewSlots from './pages/ViewSlots';
import AddBatch from './pages/AddBatch';
import Login from './pages/Login';
import Signup from './pages/Signup';
import TwoStep from './pages/TwoStep';
import SignupVerification from './pages/SignupVerification';
import ForgotPassword from './pages/ForgotPassword';
import Profile from './pages/Profile';
import { useAuth } from './context/AuthContext';

function App() {
  const { isAuthenticated } = useAuth();

  const ProtectedRoute = ({ children }) => {
    if (!isAuthenticated) {
      return <Navigate to="/login" replace />;
    }
    return children;
  };

  const PublicRoute = ({ children }) => {
    if (isAuthenticated) {
      return <Navigate to="/" replace />;
    }
    return children;
  };

  return (
    <Router>
      <div className="app-frame">
        <div className="background-grid" aria-hidden="true" />
        <div className="app-inner">
          {isAuthenticated && <Navbar />}
          <div className="container">
            <Routes>
              <Route
                path="/"
                element={(
                  <ProtectedRoute>
                    <Dashboard />
                  </ProtectedRoute>
                )}
              />
              <Route
                path="/add-medicine"
                element={(
                  <ProtectedRoute>
                    <AddMedicine />
                  </ProtectedRoute>
                )}
              />
              <Route
                path="/medicines"
                element={(
                  <ProtectedRoute>
                    <ViewMedicines />
                  </ProtectedRoute>
                )}
              />
              <Route
                path="/slots"
                element={(
                  <ProtectedRoute>
                    <ViewSlots />
                  </ProtectedRoute>
                )}
              />
              <Route
                path="/add-batch"
                element={(
                  <ProtectedRoute>
                    <AddBatch />
                  </ProtectedRoute>
                )}
              />
              <Route
                path="/profile"
                element={(
                  <ProtectedRoute>
                    <Profile />
                  </ProtectedRoute>
                )}
              />
              <Route
                path="/login"
                element={(
                  <PublicRoute>
                    <Login />
                  </PublicRoute>
                )}
              />
              <Route
                path="/signup"
                element={(
                  <PublicRoute>
                    <Signup />
                  </PublicRoute>
                )}
              />
              <Route
                path="/forgot-password"
                element={(
                  <PublicRoute>
                    <ForgotPassword />
                  </PublicRoute>
                )}
              />
              <Route
                path="/verify"
                element={(
                  <PublicRoute>
                    <TwoStep />
                  </PublicRoute>
                )}
              />
              <Route
                path="/verify-signup"
                element={(
                  <PublicRoute>
                    <SignupVerification />
                  </PublicRoute>
                )}
              />
              <Route
                path="*"
                element={
                  isAuthenticated ? (
                    <Navigate to="/" replace />
                  ) : (
                    <Navigate to="/login" replace />
                  )
                }
              />
            </Routes>
          </div>
        </div>
      </div>
    </Router>
  );
}

export default App;
