import React from "react";
import { Route, Navigate } from "react-router-dom";
import { isAuthenticated } from "../auth/authapi";

const ManagerRoute = ({ element: Element, ...rest }) => {
  return isAuthenticated() && isAuthenticated().user?.role === "manager" ? (
    <Element {...rest} />
  ) : (
    <Navigate to="/signin" replace />
  );
};

export default ManagerRoute;