import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap';

const API_URL = 'http://localhost:8080/api/users'; 

function UserCreatePage() {
  const { id } = useParams(); // Get id from URL
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    addr: ''
  });
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const response = await fetch(`${API_URL}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error(`Failed to create user: HTTP status ${response.status}`);
      }

      const newUser = await response.json();
      console.log('New user created:', newUser);

      // Go back to home screen
      navigate('/', { state: { successMessage: `User ${id} created!` } }); 

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
      <h2 className="mb-4">Create User</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>First Name</Form.Label>
          <Form.Control type="text" name="firstName" value={formData.firstName || ''} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Last Name</Form.Label>
          <Form.Control type="text" name="lastName" value={formData.lastName || ''} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Email</Form.Label>
          <Form.Control type="email" name="email" value={formData.email || ''} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Address</Form.Label>
          <Form.Control type="text" name="addr" value={formData.addr || ''} onChange={handleChange} required />
        </Form.Group>
        
        <Button variant="success" type="submit" className="me-2" disabled={submitting}>
          {submitting ? 'Creating...' : 'Create User'}
        </Button>
        <Button variant="secondary" onClick={() => navigate('/')}>Cancel</Button>
      </Form>
    </div>
  );
}

export default UserCreatePage;