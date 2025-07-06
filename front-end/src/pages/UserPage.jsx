import React, { useState, useEffect } from "react";
import Navbar from "../components/Navbar";
import {getUserRole, isAuthenticated} from "../api/auth";
import Footer from "@components/Footer.jsx";
import {useNavigate} from "react-router-dom";


export default function UserPage() {
  const [userInfo, setUserInfo] = useState(null);
  const navigate = useNavigate();

    useEffect(() => {
        if (!isAuthenticated()) {
            navigate("/login");
        } else {
            const role = getUserRole();
            setUserInfo({ role });
        }
    }, [navigate]);

  return (
    <div className="boxed">

        <div id="wrapper">
            <Navbar />
            <center>
            <h1>User</h1>
            <p>Chào mừng bạn đến với trang quản trị!</p>
            <h1>User</h1>
            <p>Chào mừng bạn đến với trang quản trị!</p>
            <h1>User</h1>
            <p>Chào mừng bạn đến với trang quản trị!</p>

            {userInfo && (
                <div style={{ marginTop: "20px", padding: "15px", backgroundColor: "#f8f9fa", borderRadius: "5px" }}>
                    <h3>Thông tin người dùng:</h3>
                    <p><strong>Vai trò:</strong> {userInfo.role}</p>
                </div>
            )}
            </center>
            <Footer />
        </div>
    </div>
  );
} 