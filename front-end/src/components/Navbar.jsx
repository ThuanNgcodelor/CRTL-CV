import { useNavigate, Link } from "react-router-dom";
import { logout, getUserRole } from "../api/auth";
import logo from '../assets/images/logo.png';
import {useEffect, useState} from "react";
import {getCart} from "@api/user.js";
import Cookies from "js-cookie";
import { useCart } from "../context/CartContext";

export default function Navbar() {
  const navigate = useNavigate();
  const { cart, setCart } = useCart();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const token = Cookies.get("token");

  useEffect(() => {
    if(!token){
      setCart(null);
      setLoading(false);
      setError(null);
      return;
    }
    async function fetchTotalCart() {
      try{
        setLoading(true);
        const data = await getCart();
        setCart(data);
      }catch(e){
        setError(e);
      }finally {
        setLoading(false);
      }
    }
    fetchTotalCart();
  }, [token, setCart]);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <>
         {/* Header */}
         <div className="container">
        {/* Logo */}
        <div className="four columns">
          <div id="logo">
            <h1>
              <Link to="/index-2">
                <img src={logo} alt="Trizzy" />
              </Link>
            </h1>
          </div>
        </div>

        {/* Additional Menu */}
        <div className="twelve columns">
          <div id="additional-menu">
            <ul>
              <li><Link to="/shopping-cart">Shopping Cart</Link></li>
              <li><Link to="/wishlist">WishList <span>(2)</span></Link></li>
              <li><Link to="/checkout-billing-details">Checkout</Link></li>
              <li><Link to="/user">My Account</Link></li>
            </ul>
          </div>
        </div>

        {/* Shopping Cart */}
        <div className="twelve columns">
          <div id="cart">
            {/* Button */}
            <div className="cart-btn">
              <Link to="/cart" className="button adc">
                {cart?.totalAmount !== undefined
                    ? `${cart.totalAmount.toLocaleString()}`
                    : "Cart"}
              </Link>
            </div>
            <div className="cart-list">
              <div className="arrow"></div>
              <div className="cart-amount">
                <span>2 items in the shopping cart</span>
              </div>
              <ul>
                <li>
                  <Link to="#"><img src="/src/assets/images/small_product_list_08.jpg" alt="" /></Link>
                  <Link to="#">Converse All Star Trainers</Link>
                  <span>1 x $79.00</span>
                  <div className="clearfix"></div>
                </li>
                <li>
                  <Link to="#"><img src="/src/assets/images/small_product_list_09.jpg" alt="" /></Link>
                  <Link to="#">Tommy Hilfiger <br /> Shirt Beat</Link>
                  <span>1 x $99.00</span>
                  <div className="clearfix"></div>
                </li>
              </ul>
              <div className="cart-buttons button">
                <Link to="/shopping-cart" className="view-cart"><span data-hover="View Cart"><span>View Cart</span></span></Link>
                <Link to="/checkout-billing-details" className="checkout"><span data-hover="Checkout">Checkout</span></Link>
              </div>
              <div className="clearfix"></div>
            </div>
          </div>
          {/* Search */}
          <nav className="top-search">
            <form action="#" method="get">
              <button type="submit"><i className="fa fa-search"></i></button>
              <input
                  className="search-field"
                  type="text"
                  placeholder="Search"
                  defaultValue=""
                  style={{ width: "391px" }}
              />

            </form>
          </nav>


        </div>
      </div>

      {/* Navigation */}
      <div className="container">
        <div className="sixteen columns">
          <a href="#menu" className="menu-trigger"><i className="fa fa-bars"></i> Menu</a>
          <nav id="navigation">
            <ul className="menu" id="responsive">
              <li><Link to="/index-2" className="current homepage" id="current">Home</Link></li>
              <li className="dropdown">
                <a href="#">Shop</a>
                <ul>
                  <li><Link to="/shop-with-sidebar">Shop With Sidebar</Link></li>
                  <li><Link to="/shop-with-adv-search">Shop With Adv. Search</Link></li>
                  <li><Link to="/shop-full-width">Shop Full Width</Link></li>
                  <li><Link to="/checkout-billing-details">Checkout Pages</Link></li>
                  <li><Link to="/shop-categories-grid">Categories Grid</Link></li>
                  <li><Link to="/single-product-page">Single Product Page</Link></li>
                  <li><Link to="/variable-product-page">Variable Product Page</Link></li>
                  <li><Link to="/wishlist">Wishlist Page</Link></li>
                  <li><Link to="/shopping-cart">Shopping Cart</Link></li>
                </ul>
              </li>
              <li>
                <a href="#">Features</a>
                <div className="mega">
                  <div className="mega-container">
                    <div className="one-column">
                      <ul>
                        <li><span className="mega-headline">Example Pages</span></li>
                        <li><Link to="/contact">Contact</Link></li>
                        <li><Link to="/about">About Us</Link></li>
                        <li><Link to="/services">Services</Link></li>
                        <li><Link to="/faq">FAQ</Link></li>
                        <li><Link to="/404-page">404 Page</Link></li>
                      </ul>
                    </div>
                    <div className="one-column">
                      <ul>
                        <li><span className="mega-headline">Featured Pages</span></li>
                        <li><Link to="/index-3">Business Homepage</Link></li>
                        <li><Link to="/shop-with-sidebar">Default Shop</Link></li>
                        <li><Link to="/blog-masonry">Masonry Blog</Link></li>
                        <li><Link to="/variable-product-page">Variable Product</Link></li>
                        <li><Link to="/portfolio-dynamic-grid">Dynamic Grid</Link></li>
                      </ul>
                    </div>
                    <div className="one-column hidden-on-mobile">
                      <ul>
                        <li><span className="mega-headline">Paragraph</span></li>
                        <li><p>This <a href="#">Mega Menu</a> can handle everything. Lists, paragraphs, forms...</p></li>
                      </ul>
                    </div>
                    <div className="one-fourth-column hidden-on-mobile">
                      <a href="#" className="img-caption margin-reset">
                        <figure>
                          <img src="/src/assets/images/menu-banner-01.jpg" alt="" />
                          <figcaption>
                            <h3>Jeans</h3>
                            <span>Pack for Style</span>
                          </figcaption>
                        </figure>
                      </a>
                    </div>
                    <div className="one-fourth-column hidden-on-mobile">
                      <a href="#" className="img-caption margin-reset">
                        <figure>
                          <img src="/src/assets/images/menu-banner-02.jpg" alt="" />
                          <figcaption>
                            <h3>Sunglasses</h3>
                            <span>Nail the Basics</span>
                          </figcaption>
                        </figure>
                      </a>
                    </div>
                    <div className="clearfix"></div>
                  </div>
                </div>
              </li>
              <li className="dropdown">
                <a href="#">Shortcodes</a>
                <ul>
                  <li><Link to="/elements">Elements</Link></li>
                  <li><Link to="/typography">Typography</Link></li>
                  <li><Link to="/pricing-tables">Pricing Tables</Link></li>
                  <li><Link to="/icons">Icons</Link></li>
                </ul>
              </li>
              <li className="dropdown">
                <a href="#">Portfolio</a>
                <ul>
                  <li><Link to="/portfolio-3-columns">3 Columns</Link></li>
                  <li><Link to="/portfolio-4-columns">4 Columns</Link></li>
                  <li><Link to="/portfolio-dynamic-grid">Dynamic Grid</Link></li>
                  <li><Link to="/single-project">Single Project</Link></li>
                </ul>
              </li>
              <li className="dropdown">
                <a href="#">Blog</a>
                <ul>
                  <li><Link to="/blog-standard">Blog Standard</Link></li>
                  <li><Link to="/blog-masonry">Blog Masonry</Link></li>
                  <li><Link to="/blog-single-post">Single Post</Link></li>
                </ul>
              </li>
              <li className="demo-button">
                <a href="#">Get This Theme</a>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </>
  );
}