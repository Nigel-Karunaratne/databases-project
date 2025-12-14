import React, { useState } from 'react';
import { Form, Button, Table } from 'react-bootstrap';

const API_BASE_URL = 'http://localhost:8080/api/tasks/user'; 

function TaskSearchPage() {
  const [userId, setUserId] = useState('');
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState('Enter a User ID and click Search.');

  const handleSearch = async (e) => {
    e.preventDefault();
    setError(null);
    setMessage('');

    if (!userId || isNaN(parseInt(userId))) {
      setMessage('Please enter a valid User ID (must be an int)');
      return;
    }

    setLoading(true);
    setTasks([]); // Clear previous results

    try {
      const response = await fetch(`${API_BASE_URL}/${userId}`);
      
      if (response.status === 404) {
          // Assuming your backend returns 404 if the user exists but has no tasks,
          // OR if the user itself doesn't exist. Check your API documentation.
          setTasks([]);
          setMessage(`No tasks found for User ID: ${userId}.`);
          return;
      }
      
      if (!response.ok) {
        throw new Error(`HTTP status ${response.status} when fetching tasks.`);
      }

      const json = await response.json();
      setTasks(json);
      
      if (json.length === 0) {
          setMessage(`User ID ${userId} exists, but has no tasks.`);
      } else {
          setMessage(`Found ${json.length} tasks for User ID: ${userId}.`);
      }

    } catch (err) {
      console.error("Search error:", err);
      setError(err.message || 'An unknown error occurred during search.');
      setMessage('');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5 p-4 border rounded shadow-sm">
      <h2 className="mb-4">Task Search by User ID</h2>

      {/* --- Search Form --- */}
      <Form onSubmit={handleSearch} className="mb-4 d-flex">
        <Form.Group className="me-3 flex-grow-1">
          <Form.Control
            type="number"
            placeholder="Enter User ID (e.g., 1)"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            required
          />
        </Form.Group>
        <Button variant="primary" type="submit" disabled={loading}>
          {loading ? 'Searching...' : 'Search Tasks'}
        </Button>
      </Form>
      
      {/* --- Results Display --- */}
      {error && <div className="alert alert-danger">{error}</div>}
      {message && <div className="alert alert-info">{message}</div>}

      {tasks.length > 0 && (
        <div className="mt-4">
          <h4>Task Results</h4>
          <Table striped bordered hover size="sm">
            <thead>
              <tr>
                <th>Task ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Priority</th>
                <th>Due Date</th>
                {/* Add other relevant task headers here */}
              </tr>
            </thead>
            <tbody>
              {tasks.map(task => (
                <tr key={task.taskID}>
                  <td>{task.taskID}</td>
                  <td>{task.title}</td>
                  <td>{task.description}</td> {/* Adjust property names based on your JSON */}
                  <td>{task.priority}</td>
                  <td>{task.dueDate}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      )}
    </div>
  );
}

export default TaskSearchPage;