import React from 'react';
import { Link } from 'react-router-dom';
import logoFooter from '../assets/images/logo-footer.png';

const Footer = () => {
  return (
    <>
      <div id="footer">
        {/* Container */}
        <div className="container">
          <div className="four columns">
            <img src={logoFooter} alt="" className="margin-top-10"/>
            <p className="margin-top-15">
              Nulla facilisis feugiat magna, ut molestie metus hendrerit vitae. 
              Vivamus tristique lectus at varius rutrum. Integer lobortis mauris 
              non consectetur eleifend.
            </p>
          </div>

          <div className="four columns">
            {/* Headline */}
            <h3 className="headline footer">Customer Service</h3>
            <span className="line"></span>
            <div className="clearfix"></div>

            <ul className="footer-links">
              <li><Link to="#">Order Status</Link></li>
              <li><Link to="#">Payment Methods</Link></li>
              <li><Link to="#">Delivery & Returns</Link></li>
              <li><Link to="#">Privacy Policy</Link></li>
              <li><Link to="#">Terms & Conditions</Link></li>
            </ul>
          </div>

          <div className="four columns">
            {/* Headline */}
            <h3 className="headline footer">My Account</h3>
            <span className="line"></span>
            <div className="clearfix"></div>

            <ul className="footer-links">
              <li><Link to="/my-account">My Account</Link></li>
              <li><Link to="#">Order History</Link></li>
              <li><Link to="/wishlist">Wish List</Link></li>
            </ul>
          </div>

          <div className="four columns">
            {/* Headline */}
            <h3 className="headline footer">Newsletter</h3>
            <span className="line"></span>
            <div className="clearfix"></div>
            <p>
              Sign up to receive email updates on new product announcements, 
              gift ideas, special promotions, sales and more.
            </p>

            <form onSubmit={(e) => e.preventDefault()}>
              <button className="newsletter-btn" type="submit">Join</button>
              <input 
                className="newsletter" 
                type="text" 
                placeholder="mail@example.com" 
                defaultValue=""
              />
            </form>
          </div>
        </div>
        {/* Container / End */}
      </div>
      {/* Footer / End */}

      {/* Footer Bottom / Start */}
      <div id="footer-bottom">
        {/* Container */}
        <div className="container">
          <div className="eight columns">
            © Copyright 2024 by <Link to="#">CutLayout</Link>. All Rights Reserved.
          </div>
          <div className="eight columns">
            <ul className="payment-icons">
              <li><img src="/images/visa.png" alt="" /></li>
              <li><img src="/images/mastercard.png" alt="" /></li>
              <li><img src="/images/skrill.png" alt="" /></li>
              <li><img src="/images/moneybookers.png" alt="" /></li>
              <li><img src="/images/paypal.png" alt="" /></li>
            </ul>
          </div>
        </div>
        {/* Container / End */}
      </div>
      {/* Footer Bottom / End */}

      {/* Back To Top Button */}
      <div id="backtotop">
        <Link to="#"></Link>
      </div>
    </>
  );
};

export default Footer; 