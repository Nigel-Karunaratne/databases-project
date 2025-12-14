import { useState } from 'react'
import './App.css'

import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';

import HomePage from './pages/HomePage';
import UserEditPage from './pages/UserEditPage';
import ManagerEditPage from './pages/ManagerEditPage';
import ProjectEditPage from './pages/ProjectEditPage';
import TaskEditPage from './pages/TaskEditPage';

function App() {
  const [count, setCount] = useState(0)

  return (
    <Router>
      <div className="container p-4">
        <h1 className='text-center mb-5'>Task Management System (Admin View)</h1>

        <nav className='nav nav-tabs justify-content-center mb-4'>

        </nav>
        <Routes>
          <Route path="/" element={<HomePage />} />

          <Route path="/users/edit/:id" element={<UserEditPage />} />
          <Route path="/managers/edit/:id" element={<ManagerEditPage />} />
          <Route path="/projects/edit/:id" element={<ProjectEditPage />} />
          <Route path="/tasks/edit/:id" element={<TaskEditPage />} />
        </Routes>
      </div>
    </Router>
  )
}

export default App
