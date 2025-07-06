import { useState, useEffect } from "react";
import Navbar from "../components/Navbar";
import { getUserRole } from "../api/auth";

export default function AdminPage() {
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    const role = getUserRole();
    setUserInfo({ role });
  }, []);

  return (
    <div>
      <Navbar />
      <div className="container">
        <div className="page-container">
          <h1>Trang Quản trị Admin</h1>
          <p>Chào mừng bạn đến với trang quản trị!</p>

          {userInfo && (
            <div style={{ marginTop: "20px", padding: "15px", backgroundColor: "#f8f9fa", borderRadius: "5px" }}>
              <h3>Thông tin người dùng:</h3>
              <p><strong>Vai trò:</strong> {userInfo.role}</p>
            </div>
          )}

        </div>
      </div>
    </div>
  );
} 