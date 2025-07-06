import { Navigate } from "react-router-dom";
import { getToken } from "../api/auth";

export default function PrivateRoute({ children }) {
  if (!getToken()) {
    return <Navigate to="/login" replace />;
  }
  return children;
}