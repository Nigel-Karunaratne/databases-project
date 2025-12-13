import { useState } from 'react'
import './App.css'

import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';

import UserEditPage from './pages/UserEditPage';
import HomePage from './pages/HomePage';

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
        </Routes>
      </div>
    </Router>
  )
}

export default App
