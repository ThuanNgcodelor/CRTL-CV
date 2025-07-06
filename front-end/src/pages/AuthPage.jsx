import AuthForm from "../components/AuthForm";
import Navbar from "@components/Navbar.jsx";
import Footer from "@components/Footer.jsx";
import React from "react";

export default function AuthPage() {
  return (
    <div className="boxed">
      <div id="wrapper">
        <Navbar />
        
        <AuthForm />
        
        <Footer />
      </div>
    </div>
  );
} 