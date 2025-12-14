import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap';

const API_URL = 'http://localhost:8080/api/tasks'; 

function TaskCreatePage() {
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
    dataToSend.projectID = parseInt(dataToSend.projectID);
    if (isNaN(dataToSend.projectID)) {
      alert("Project ID needs to be an integer");
      return;
    }
    dataToSend.assignedUserID = parseInt(dataToSend.assignedUserID);
    if (isNaN(dataToSend.assignedUserID)) {
      alert("User ID needs to be an integer");
      return;
    }
    dataToSend.priority = parseInt(dataToSend.priority);
    if (isNaN(dataToSend.priority)) {
      alert("Priority needs to be an integer");
      return;
    }

    console.log("SENDING");
    console.log(JSON.stringify(dataToSend));

    try {
      const response = await fetch(`${API_URL}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dataToSend),
      });

      if (!response.ok) {
        if (response.status == 400) { //400 means a bad status was put in.
          alert("ERROR: Status 400.\nPlease make sure the User ID, Project ID, and other fields are valid!");
          return;
        }
        throw new Error(`Failed to create task: HTTP status ${response.status}`);
      }

      // Go back to home screen when done correctly
      navigate('/', { state: { successMessage: `Task ${id} created!` } }); 

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
      <h2 className="mb-4">Create Task</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Title</Form.Label>
          <Form.Control type="text" name="title" value={formData.title || ''} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Project ID</Form.Label>
          <Form.Control type="number" inputMode='numeric' name="projectID" value={formData.projectID || ''} onChange={(e) => {e.target.value = e.target.value.replace(/[^0-9]/g, ''); handleChange(e);}} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Description</Form.Label>
          <Form.Control type="text" name="description" value={formData.description || ''} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Assigned User ID</Form.Label>
          <Form.Control type="number" inputMode='numeric' name="assignedUserID" value={formData.assignedUserID || ''} onChange={(e) => {e.target.value = e.target.value.replace(/[^0-9]/g, ''); handleChange(e);}} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Priority Level</Form.Label>
          <Form.Control type="number" inputMode='numeric' name="priority" value={formData.priority || ''} onChange={(e) => {e.target.value = e.target.value.replace(/[^0-9]/g, ''); handleChange(e);}} required />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Due Date</Form.Label>
          <Form.Control type="date" name="dueDate" value={formData.dueDate || ''} onChange={handleChange} required />
        </Form.Group>
        
        <Button variant="success" type="submit" className="me-2" disabled={submitting}>
          {submitting ? 'Creating...' : 'Create Task'}
        </Button>
        <Button variant="secondary" onClick={() => navigate('/')}>Cancel</Button>
      </Form>
    </div>
  );
}

export default TaskCreatePage;