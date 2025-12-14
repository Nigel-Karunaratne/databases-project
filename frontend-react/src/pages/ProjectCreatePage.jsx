import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap';

const API_URL = 'http://localhost:8080/api/projects'; 

function ProjectCreatePage() {
  const { id } = useParams(); // Get id from URL
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState({
  });
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    const dataToSend = { ...formData };
    dataToSend.managerUserID = parseInt(dataToSend.managerUserID);
    if (isNaN(dataToSend.managerUserID)) {
      alert("Manager UID needs to be an integer");
      return;
    }

    console.log("SUMBITTING");
    console.log(JSON.stringify(dataToSend));
    try {
      const response = await fetch(`${API_URL}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dataToSend),
      });

      if (!response.ok) {
        if (response.status == 400) { //400 means a bad status was put in.
          alert("ERROR: Status 400.\nPlease make sure the manager's user id is valid!");
          return;
        }
        throw new Error(`Failed to create project: HTTP status ${response.status}`);
      }

      // Go back to home screen when done correctly
      navigate('/', { state: { successMessage: `Project ${id} created!` } }); 

    } catch (err) {
      console.error('Creation error:', err);
      alert(`Create failed: ${err.message}`);
    } finally {
      setSubmitting(false);
    }
  };

  // -- VISUAL -- //

  return (
    <div className="mt-5 p-4 border rounded shadow-sm">
      <h2 className="mb-4">Create Project</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Project Name</Form.Label>
          <Form.Control type="text" name="projectName" value={formData.projectName || ''} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Manager's User ID</Form.Label>
          <Form.Control type="number" inputMode='numeric' name="managerUserID" value={formData.managerUserID || ''} onChange={(e) => {e.target.value = e.target.value.replace(/[^0-9]/g, ''); handleChange(e);}} required />
        </Form.Group>
        
        <Button variant="success" type="submit" className="me-2" disabled={submitting}>
          {submitting ? 'Creating...' : 'Create Manager'}
        </Button>
        <Button variant="secondary" onClick={() => navigate('/')}>Cancel</Button>
      </Form>
    </div>
  );
}

export default ProjectCreatePage;