import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Signup from "./core/Signup";
import Signin from "./core/Signin"
import ManagerDashboard from "./components/ManagerDashboard";
import CustomerDashboard from "./components/CustomerDashboard";
import ManagerRoute from "./privateroutes/ManagerRoute";
import CustomerRoute from "./privateroutes/CustomerRoute";

const App= () => {
  return (
    <BrowserRouter>
      <Routes>
      <Route path="/" element={<Navigate to="/signin" />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/signin" element={<Signin />} />
        
        {/* Role-based Routes */}
        <Route path="/manager/dashboard" element={<ManagerRoute element={ManagerDashboard} />} />
        {/* <Route path="/customer/dashboard" element={<CustomerRoute><CustomerDashboard /></CustomerRoute>} /> */}
       {/* Protected Customer Routes */}
       <Route element={<CustomerRoute />}>
          <Route path="/customer/dashboard" element={<CustomerDashboard />} />
        </Route>
      
      
      </Routes>
    </BrowserRouter>
  );
};

export default App;
