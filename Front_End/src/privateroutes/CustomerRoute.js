import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { isAuthenticated } from "../auth/authapi";

const CustomerRoute = () => {
  const user = isAuthenticated()?.user;

  return user && user.role === "customer" ? <Outlet /> : <Navigate to="/signin" replace />;
};

export default CustomerRoute;