import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap';

const API_URL = 'http://localhost:8080/api/projects'; 

function ProjectEditPage() {
  const { id } = useParams(); // Get id from URL
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // First, get data from specific instance
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`${API_URL}/${id}`); // GET /api/projects/{id}
        if (!response.ok) {
          throw new Error(`HTTP status ${response.status}`);
        }
        const data = await response.json();
        setFormData(data); // Pre populate the form data if available (which it should be)
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]); // Re-run fn if the id in the URL changes

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const dataToSend = { ...formData };
    dataToSend.managerUserID = parseInt(dataToSend.managerUserID);
    if (isNaN(dataToSend.managerUserID)) {
      alert("Manager UID needs to be an integer");
      return;
    }

    console.log("SUMBITTING");
    console.log(JSON.stringify(dataToSend));
    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dataToSend),
      });

      if (!response.ok) {
        throw new Error(`Failed to update: HTTP status ${response.status}`);
      }

      // Go back to home screen when done correctly
      navigate('/', { state: { successMessage: `Project ${id} updated!` } }); 

    } catch (err) {
      console.error('Update error:', err);
      alert(`Update failed: ${err.message}`);
    }
  };

  // -- VISUAL -- //

  if (loading) return <div className="text-center mt-5">Loading Project Data...</div>;
  if (error) return <div className="alert alert-danger mt-5">Error: {error}</div>;

  return (
    <div className="mt-5 p-4 border rounded shadow-sm">
      <h2 className="mb-4">Edit Project (ID: {id})</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Project Name</Form.Label>
          <Form.Control type="text" name="projectName" value={formData.projectName || ''} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Manager's User ID</Form.Label>
          <Form.Control type="number" inputMode='numeric' name="managerUserID" value={formData.managerUserID || ''} onChange={(e) => {e.target.value = e.target.value.replace(/[^0-9]/g, ''); handleChange(e);}} required />
        </Form.Group>
        
        <Button variant="primary" type="submit" className="me-2">Save Changes</Button>
        <Button variant="secondary" onClick={() => navigate('/')}>Cancel</Button>
      </Form>
    </div>
  );
}

export default ProjectEditPage;