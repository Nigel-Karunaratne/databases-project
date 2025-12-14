import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

const API_BASE_URL = 'http://localhost:8080/api/tasks'; 

function TaskTable() {
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

  const handleDelete = async (taskID, taskTitle) => {
    // 1. Confirmation Step
    if (!window.confirm(`Are you sure you want to delete task: ${taskTitle} (ID: ${taskID})?`)) {
      return; // Stop if the user cancels
    }

    try {
      const response = await fetch(`${API_BASE_URL}/${taskID}`, {
        method: 'DELETE', // 2. Specify the DELETE method
      });

      if (response.status === 204 || response.ok) { 
        console.log(`task ${taskID} deleted successfully.`);
        // Optional: Show a success message to the user (e.g., a Bootstrap Toast)

        window.location.reload();
      } else {
        // Handle API errors (e.g., if the user doesn't exist)
        throw new Error(`Failed to delete task: HTTP status ${response.status}`);
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
      <h2 className="mb-4">Tasks</h2>
      <p className="text-muted">Across all projects, there are **{data.length}** tasks.</p>
      
      <table className="table table-striped table-hover border">
        <thead className="table-dark">
          <tr>
            <th>Task ID</th>
            <th>Title</th>
            <th>Project</th>
            <th>Description</th>
            <th>Assigned User ID</th>
            <th>Priority Level</th>
            <th>Due Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {data.map(data => (
            <tr key={data.taskID}>
              <td>{data.taskID}</td>
              <td>{data.title}</td>
              <td>{data.projectID}</td>
              <td>{data.description}</td>
              <td>{data.assignedUserID}</td>
              <td>{data.priority}</td>
              <td>{data.dueDate}</td>
              <td>
                <Link to={`/tasks/edit/${data.taskID}`} className="btn btn-sm btn-info me-2">Edit</Link>
                <button className="btn btn-sm btn-danger" onClick={() => handleDelete(data.taskID, data.title)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      
      {/* Button to refresh the data */}
      <button className="btn btn-success mt-3" onClick={fetchData}>
        Refresh Data
      </button>
      <Link to={`/tasks/create`} className="btn btn-success mt-3">Create New Task</Link>
    </div>
  );
}

export default TaskTable;