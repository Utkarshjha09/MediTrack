import { useState, useEffect } from 'react';

/**
 * Custom hook for fetching data from API
 * @param {Function} apiCall - The API function to call
 * @param {Array} dependencies - Dependencies array for useEffect
 * @returns {Object} - { data, loading, error, refetch }
 */
const useFetch = (apiCall, dependencies = []) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await apiCall();
      setData(response.data.data || response.data);
    } catch (err) {
      console.error('Fetch error:', err);
      setError(err.response?.data?.message || err.message || 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, dependencies);

  const refetch = () => {
    fetchData();
  };

  return { data, loading, error, refetch };
};

export default useFetch;
