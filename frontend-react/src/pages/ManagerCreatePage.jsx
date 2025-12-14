import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap';

const API_URL = 'http://localhost:8080/api/managers'; 

function ManagerCreatePage() {
  const { id } = useParams(); // Get id from URL
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState({
    expertiseArea: '',
    experienceYears: ''
  });
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const dataToSend = { ...formData };
    dataToSend.userID = parseInt(dataToSend.userID);
    if (isNaN(dataToSend.userID)) {
      alert("User ID needs to be an integer");
      return;
    }
    dataToSend.experienceYears = parseInt(dataToSend.experienceYears);
    if (isNaN(dataToSend.experienceYears)) {
      alert("Years of Experience needs to be an integer");
      return;
    }

    console.log(JSON.stringify(dataToSend));
    setSubmitting(true);

    try {
      const response = await fetch(`${API_URL}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dataToSend),
      });

      if (!response.ok) {
        if (response.status == 400) {
          alert("Failed to create manager (status 400). Make sure the User ID is valid!");  
        }
        throw new Error(`Failed to create manager: HTTP status ${response.status}`);
      }

      // Go back to home screen
      navigate('/', { state: { successMessage: `Manager ${id} updated!` } }); 

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
      <h2 className="mb-4">Create Manager</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>User ID</Form.Label>
          <Form.Control type="text" inputMode='numeric' name="userID" value={formData.userID || ''} onChange={(e) => {e.target.value = e.target.value.replace(/[^0-9]/g, ''); handleChange(e);}} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Area of Expertise</Form.Label>
          <Form.Control type="text" name="expertiseArea" value={formData.expertiseArea || ''} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Years of Experience</Form.Label>
          <Form.Control type="text" inputMode='numeric' name="experienceYears" value={formData.experienceYears || ''} onChange={(e) => {e.target.value = e.target.value.replace(/[^0-9]/g, ''); handleChange(e);}} required />
        </Form.Group>
        
        <Button variant="success" type="submit" className="me-2" disabled={submitting}>
          {submitting ? 'Creating...' : 'Create Manager'}
        </Button>
        <Button variant="secondary" onClick={() => navigate('/')}>Cancel</Button>
      </Form>
    </div>
  );
}

export default ManagerCreatePage;