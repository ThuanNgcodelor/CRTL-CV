import { useState, useEffect } from "react";
import {login, register, getUserRole, isAuthenticated} from "../api/auth";
import { useNavigate, Link, useLocation  } from "react-router-dom";

export default function AuthForm() {
  const [activeTab, setActiveTab] = useState("login");
  const [loginData, setLoginData] = useState({
    email: "",
    password: "",
  });
  const [registerData, setRegisterData] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/";


  useEffect(() => {
    if (isAuthenticated()) {
      const roles = getUserRole();

      if (Array.isArray(roles)) {
        if (roles.includes("ROLE_ADMIN")) {
          navigate("/admin");
        } else if (roles.includes("ROLE_USER")) {
          navigate(from, { replace: true });
        }
      }
    }
  }, [navigate]);

  useEffect(() => {

    if (location.pathname === "/register") {
      setActiveTab("register");
    } else {
      setActiveTab("login");
    }
  }, [location.pathname]);

  const handleLoginChange = (e) => {
    setLoginData({
      ...loginData,
      [e.target.name]: e.target.value,
    });
  };

  const handleRegisterChange = (e) => {
    setRegisterData({
      ...registerData,
      [e.target.name]: e.target.value,
    });
  };

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await login(loginData);
      const roles = getUserRole();
      if (roles.includes("ROLE_ADMIN")) {
        navigate("/admin");
      } else if (roles.includes("ROLE_USER")) {
        navigate("/user");
      }

    } catch (err) {
      setError(err.response?.data?.message || "Đăng nhập thất bại. Vui lòng kiểm tra email và mật khẩu.");
    } finally {
      setLoading(false);
    }
  };

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (registerData.password !== registerData.confirmPassword) {
      setError("Mật khẩu xác nhận không khớp");
      return;
    }

    if (registerData.password.length < 6) {
      setError("Mật khẩu phải có ít nhất 6 ký tự");
      return;
    }

    setLoading(true);

    try {
      const { username, email, password } = registerData;
      await register({ username, email, password });
      setSuccess("Register successfully! You can login in now.");
      setActiveTab("login");
      setRegisterData({
        username: "",
        email: "",
        password: "",
        confirmPassword: "",
      });
    } catch (err) {
      setError(err.response?.data?.message || "Register error. Please check your registration information.");
    } finally {
      setLoading(false);
    }
  };

  const switchTab = (tab) => {
    setActiveTab(tab);
    setError("");
    setSuccess("");

    if (tab === "register") {
      navigate("/register");
    } else {
      navigate("/login");
    }
  };
  return (
    <>
      <section className="titlebar">
        <div className="container">
          <div className="sixteen columns">
            <h2>My Account</h2>
            
            <nav id="breadcrumbs">
              <ul>
                <li><Link to="/">Home</Link></li>
                <li>My Account</li>
              </ul>
            </nav>
          </div>
        </div>
      </section>

      {/* Content */}
      <div className="container">
        <div className="six columns centered">
          <ul className="tabs-nav my-account">
            <li className={activeTab === "login" ? "active" : ""}>
              <a href="#tab1" onClick={(e) => { e.preventDefault(); switchTab("login"); }}>
                Login
              </a>
            </li>
            <li className={activeTab === "register" ? "active" : ""}>
              <a href="#tab2" onClick={(e) => { e.preventDefault(); switchTab("register"); }}>
                Register
              </a>
            </li>
          </ul>

          <div className="tabs-container">
            {/* Login */}
            <div 
              className="tab-content" 
              id="tab1" 
              style={{ display: activeTab === "login" ? "block" : "none" }}
            >
              <h3 className="headline">Login</h3>
              <span className="line" style={{ marginBottom: "20px" }}></span>
              <div className="clearfix"></div>

              <form onSubmit={handleLoginSubmit} className="login">
                <p className="form-row form-row-wide">
                  <label htmlFor="email">Email Address: <span className="required">*</span></label>
                  <input 
                    type="email" 
                    className="input-text" 
                    name="email" 
                    id="email" 
                    value={loginData.email}
                    onChange={handleLoginChange}
                    required
                  />
                </p>

                <p className="form-row form-row-wide">
                  <label htmlFor="password">Password: <span className="required">*</span></label>
                  <input 
                    className="input-text" 
                    type="password" 
                    name="password" 
                    id="password" 
                    value={loginData.password}
                    onChange={handleLoginChange}
                    required
                  />
                </p>

                <p className="form-row">
                  <input 
                    type="submit" 
                    className="button" 
                    name="login" 
                    value={loading ? "Đang đăng nhập..." : "Login"}
                    disabled={loading}
                  />

                  <label htmlFor="rememberme" className="rememberme">
                    <input name="rememberme" type="checkbox" id="rememberme" value="forever" /> 
                    Remember Me
                  </label>
                </p>

                <p className="lost_password">
                  <a href="#">Lost Your Password?</a>
                </p>
              </form>
              <div style={{ display: "flex", gap: "10px", marginBottom: "10px" }}>
                <button
                    type="button"
                    style={{
                      display: "flex",
                      alignItems: "center",
                      backgroundColor: "#4267B2",
                      color: "white",
                      border: "none",
                      borderRadius: "4px",
                      padding: "8px 18px",
                      cursor: "pointer",
                      fontWeight: "bold"
                    }}
                    onClick={() => window.location.href = "/api/auth/facebook"}
                >
                  <img
                      src="https://upload.wikimedia.org/wikipedia/commons/0/05/Facebook_Logo_%282019%29.png"
                      alt="Facebook"
                      style={{ width: 24, height: 24, marginRight: 13 }}
                  />
                  Login with Facebook
                </button>
                <button
                    type="button"
                    style={{
                      display: "flex",
                      alignItems: "center",
                      backgroundColor: "#fff",
                      color: "#444",
                      border: "1px solid #ddd",
                      borderRadius: "4px",
                      padding: "8px 18px",
                      cursor: "pointer",
                      fontWeight: "bold"
                    }}
                    onClick={() => window.location.href = "/api/auth/google"}
                >
                  <img
                      src="https://upload.wikimedia.org/wikipedia/commons/4/4a/Logo_2013_Google.png"
                      alt="Google"
                      style={{ width: 39, height: 18, marginRight: 13 }}
                  />
                  Login with Google
                </button>
              </div>

              
            </div>

            {/* Register */}
            <div 
              className="tab-content" 
              id="tab2" 
              style={{ display: activeTab === "register" ? "block" : "none" }}
            >
              <h3 className="headline">Register</h3>
              <span className="line" style={{ marginBottom: "20px" }}></span>
              <div className="clearfix"></div>

              <form onSubmit={handleRegisterSubmit} className="register">
                <p className="form-row form-row-wide">
                  <label htmlFor="reg_username">Username: <span className="required">*</span></label>
                  <input 
                    type="text" 
                    className="input-text" 
                    name="username" 
                    id="reg_username" 
                    value={registerData.username}
                    onChange={handleRegisterChange}
                    required
                  />
                </p>
                
                <p className="form-row form-row-wide">
                  <label htmlFor="reg_email">Email Address: <span className="required">*</span></label>
                  <input 
                    type="email" 
                    className="input-text" 
                    name="email" 
                    id="reg_email" 
                    value={registerData.email}
                    onChange={handleRegisterChange}
                    required
                  />
                </p>

                <p className="form-row form-row-wide">
                  <label htmlFor="reg_password">Password: <span className="required">*</span></label>
                  <input 
                    type="password" 
                    className="input-text" 
                    name="password" 
                    id="reg_password" 
                    value={registerData.password}
                    onChange={handleRegisterChange}
                    required
                  />
                </p>

                <p className="form-row form-row-wide">
                  <label htmlFor="reg_password2">Repeat Password: <span className="required">*</span></label>
                  <input 
                    type="password" 
                    className="input-text" 
                    name="confirmPassword" 
                    id="reg_password2" 
                    value={registerData.confirmPassword}
                    onChange={handleRegisterChange}
                    required
                  />
                </p>

                <p className="form-row">
                  <input 
                    type="submit" 
                    className="button" 
                    name="register" 
                    value={loading ? "Đang đăng ký..." : "Register"}
                    disabled={loading}
                  />
                </p>
              </form>
            </div>
          </div>
        </div>
      </div>

      {error && (
        <div className="container">
          <div className="six columns centered">
            <div className="error" style={{ 
              backgroundColor: "#ffebee", 
              color: "#c62828", 
              padding: "10px", 
              borderRadius: "4px", 
              marginTop: "20px",
              textAlign: "center"
            }}>
              {error}
            </div>
          </div>
        </div>
      )}

      {success && (
        <div className="container">
          <div className="six columns centered">
            <div className="success" style={{ 
              backgroundColor: "#e8f5e8", 
              color: "#2e7d32", 
              padding: "10px", 
              borderRadius: "4px", 
              marginTop: "20px",
              textAlign: "center"
            }}>
              {success}
            </div>
          </div>
        </div>
      )}

      <div className="margin-top-50"></div>
    </>
  );
} 