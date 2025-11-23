import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';
const OTP_BASE_URL = process.env.REACT_APP_OTP_BASE_URL || 'http://localhost:4000';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const otpApi = axios.create({
  baseURL: OTP_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Medicine API calls
export const medicineAPI = {
  getAll: () => api.get('/medicines'),
  getById: (id) => api.get(`/medicines/${id}`),
  getByName: (name) => api.get(`/medicines/name/${name}`),
  create: (data) => api.post('/medicines', data),
  update: (id, data) => api.put(`/medicines/${id}`, data),
  delete: (id) => api.delete(`/medicines/${id}`),
};

// Batch API calls
export const batchAPI = {
  getAll: () => api.get('/batches'),
  getById: (id) => api.get(`/batches/${id}`),
  getByBatchNumber: (batchNumber) => api.get(`/batches/number/${batchNumber}`),
  getByMedicine: (medicineId) => api.get(`/batches/medicine/${medicineId}`),
  getExpiring: (date) => api.get(`/batches/expiring?date=${date}`),
  getExpiringBetween: (startDate, endDate) => 
    api.get(`/batches/expiring-between?startDate=${startDate}&endDate=${endDate}`),
  create: (data) => api.post('/batches', data),
  update: (id, data) => api.put(`/batches/${id}`, data),
  delete: (id) => api.delete(`/batches/${id}`),
};

// Slot API calls
export const slotAPI = {
  getAll: () => api.get('/slots'),
  getById: (id) => api.get(`/slots/${id}`),
  getByNumber: (slotNumber) => api.get(`/slots/number/${slotNumber}`),
  getAvailable: () => api.get('/slots/available'),
  getByLocation: (location) => api.get(`/slots/location/${location}`),
  create: (data) => api.post('/slots', data),
  update: (id, data) => api.put(`/slots/${id}`, data),
  delete: (id) => api.delete(`/slots/${id}`),
};

export const otpAPI = {
  sendCode: (email) => otpApi.post('/send-code', { email }),
  verifyCode: (email, code) => otpApi.post('/verify-code', { email, code }),
};

export default api;
