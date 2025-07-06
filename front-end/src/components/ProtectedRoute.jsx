import { Navigate } from "react-router-dom";
import { isAuthenticated, getUserRole } from "../api/auth";

export default function ProtectedRoute({ children, requiredRole = null }) {
  const authenticated = isAuthenticated();
  
  if (!authenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRole) {
    const userRole = getUserRole();
    if (userRole !== requiredRole) {
      if (userRole === "ROLE_ADMIN") {
        return <Navigate to="/admin" replace />;
      } else {
        return <Navigate to="/user" replace />;
      }
    }
  }

  return children;
} 