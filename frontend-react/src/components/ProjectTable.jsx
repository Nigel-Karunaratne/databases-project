import React, { useState, useEffect } from 'react';

const API_BASE_URL = 'http://localhost:8080/api/projects'; 

function ProjectTable() {
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
        <button className="btn btn-secondary" onClick={fetchUsers}>
            Try Again
        </button>
      </div>
    );
  }

  // else, was successful
  return (
    <div className="container mt-5">
      <h2 className="mb-4">Projects</h2>
      <p className="text-muted">There are **{data.length}** projects.</p>
      
      <table className="table table-striped table-hover border">
        <thead className="table-dark">
          <tr>
            <th>Project ID</th>
            <th>Name</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {data.map(data => (
            <tr key={data.projectID}>
              <td>{data.projectID}</td>
              <td>{data.projectName}</td>
              <td>
                {/* Placeholder buttons for future operations */}
                <button className="btn btn-sm btn-info me-2">Edit</button>
                <button className="btn btn-sm btn-danger">Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      
      {/* Button to refresh the data */}
      <button className="btn btn-success mt-3" onClick={fetchData}>
        Refresh Data
      </button>
    </div>
  );
}

export default ProjectTable;