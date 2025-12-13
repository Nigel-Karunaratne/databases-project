import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap';

const API_URL = 'http://localhost:8080/api/users'; 

function UserEditPage() {
  const { id } = useParams(); // Get id from URL
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // First, get data from specific instance
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await fetch(`${API_URL}/${id}`); // GET /api/users/{id}
        if (!response.ok) {
          throw new Error(`HTTP status ${response.status}`);
        }
        const user = await response.json();
        setFormData(user); // Pre populate the form data if available (which it should be)
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchUser();
  }, [id]); // Re-run fn if the id in the URL changes

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error(`Failed to update: HTTP status ${response.status}`);
      }

      // Go back to home screen when done correctly
      navigate('/', { state: { successMessage: `User ${id} updated!` } }); 

    } catch (err) {
      console.error('Update error:', err);
      alert(`Update failed: ${err.message}`);
    }
  };

  // -- VISUAL -- //

  if (loading) return <div className="text-center mt-5">Loading User Data...</div>;
  if (error) return <div className="alert alert-danger mt-5">Error: {error}</div>;

  return (
    <div className="mt-5 p-4 border rounded shadow-sm">
      <h2 className="mb-4">Edit User ID: {id}</h2>
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
        
        <Button variant="primary" type="submit" className="me-2">Save Changes</Button>
        <Button variant="secondary" onClick={() => navigate('/')}>Cancel</Button>
      </Form>
    </div>
  );
}

export default UserEditPage;