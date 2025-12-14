import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

const API_BASE_URL = 'http://localhost:8080/api/managers'; 

function ManagerTable() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchData = async () => {
    setLoading(true); // Start loading state
    setError(null);   // Clear previous errors

    try {
      const response = await fetch(API_BASE_URL);

      if (!response.ok) {
        throw new Error(`Failed to fetch data: ${response.status}`);
      }

      const json = await response.json();
      setData(json);
    } catch (err) {
      console.error("Fetch error:", err);
      setError(err.message || 'An unknown error occurred.');
    } finally {
      setLoading(false); // Stop loading regardless of success or failure
    }
  };

  // useEffect hook to call the fetch function when the component mounts
  useEffect(() => {
    fetchData();
  }, []); // Empty dependency array ensures this runs only once

  const handleDelete = async (managerId) => {
    if (!window.confirm(`Are you sure you want to delete manager ID: ${managerId}?`)) {
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/${managerId}`, {
        method: 'DELETE',
      });

      if (response.status === 204 || response.ok) { 
        console.log(`Manager ${managerId} deleted successfully.`);
        //reload window
        window.location.reload();
      } else {
        // Handle API errors
        throw new Error(`Failed to delete manager: HTTP status ${response.status}`);
      }
    } catch (err) {
      console.error("Delete error:", err);
      setError(`Deletion failed: ${err.message}`);
    }

  };

  /* --- RENDERING --- */

  if (loading) {
    // spinner
    return (
      <div className="d-flex justify-content-center mt-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error) {
    // alert with error
    return (
      <div className="container mt-5">
        <div className="alert alert-danger" role="alert">
          Error loading data: **{error}**
        </div>
        <button className="btn btn-secondary" onClick={fetchData}>
            Try Again
        </button>
      </div>
    );
  }

  // else, was successful
  return (
    <div className="container mt-5">
      <h2 className="mb-4">Managers</h2>
      <p className="text-muted">There are **{data.length}** registered managers.</p>
      
      <table className="table table-striped table-hover border">
        <thead className="table-dark">
          <tr>
            <th>User ID</th>
            <th>Area of Expertise</th>
            <th>Years of Experience</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {data.map(data => (
            <tr key={data.userID}>
              <td>{data.userID}</td>
              <td>{data.expertiseArea}</td>
              <td>{data.experienceYears}</td>
              <td>
                <Link to={`/managers/edit/${data.userID}`} className="btn btn-sm btn-info me-2">Edit</Link>
                <button className="btn btn-sm btn-danger" onClick={() => handleDelete(data.userID)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      
      {/* Button to refresh the data */}
      <button className="btn btn-success mt-3" onClick={fetchData}>
        Refresh Data
      </button>
      <Link to={`/managers/create`} className="btn btn-success mt-3">Create New Manager</Link>
    </div>
  );
}

export default ManagerTable;