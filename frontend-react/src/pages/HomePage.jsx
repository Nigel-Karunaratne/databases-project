import React, { useState, useEffect } from 'react';

import UserTable from '../components/UserTable';
import ManagerTable from '../components/ManagerTable';
import ProjectTable from '../components/ProjectTable';
import TaskTable from '../components/TaskTable';

function HomePage() {
    return (
    <div className="container-fluid p-4">
      <h2 className="text-center mb-5">Dashboard</h2>
      
      <div className="mb-4">
        <UserTable />
      </div>

      <div className="mb-4">
        <ManagerTable />
      </div>

      <div className="mb-4">
        <ProjectTable />
      </div>

      <div className="mb-4">
        <TaskTable />
      </div>
      
    </div>
    );
}

export default HomePage;