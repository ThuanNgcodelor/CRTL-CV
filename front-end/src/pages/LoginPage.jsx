import LoginForm from "@components/LoginForm";
import Navbar from "@components/Navbar.jsx";
import Footer from "@components/Footer.jsx";
import React from "react";

export default function LoginPage() {
  return (
      <div className="boxed">

        <div id="wrapper">
          <Navbar />

          <LoginForm/>
          <div className="margin-top-50"></div>

          <Footer />
        </div>
      </div>
  );
} 