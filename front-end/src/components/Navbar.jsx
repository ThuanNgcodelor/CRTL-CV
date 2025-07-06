import { useNavigate, Link } from "react-router-dom";
import { logout, getUserRole } from "../api/auth";
import logo from '../assets/images/logo.png';

export default function Navbar() {
  const navigate = useNavigate();
  const userRole = getUserRole();

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
              <Link to="/">
                <img src={logo} alt="Trizzy" />
              </Link>
            </h1>
          </div>
        </div>

        {/* Additional Menu */}
        <div className="twelve columns">
          <div id="additional-menu">
            <ul>
              <li>
                <Link to="/shopping-cart">Shopping Cart</Link>
              </li>
              <li>
                <Link to="/wishlist">
                  WishList <span>(2)</span>
                </Link>
              </li>
              <li>
                <Link to="/checkout-billing-details">Checkout</Link>
              </li>
              <li>
                <Link to="/auth">My Account</Link>
              </li>
            </ul>
          </div>
        </div>

        {/* Shopping Cart */}
        <div className="twelve columns">
          <div id="cart">
            {/* Button */}
            <div className="cart-btn">
              <Link to="/cart" className="button adc">$178.00</Link>
            </div>

          </div>

          {/* Search */}
          <nav className="top-search">
            <form action="#" method="get">
              <button type="submit">
                <i className="fa fa-search"></i>
              </button>
              <input
                className="search-field"
                type="text"
                placeholder="Search"
                defaultValue=""
              />
            </form>
          </nav>
        </div>
      </div>

      {/* Navigation */}
      <div className="container">
        <div className="sixteen columns">
          <a href="#menu" className="menu-trigger">
            <i className="fa fa-bars"></i> Menu
          </a>

          <nav id="navigation">
            <ul className="menu" id="responsive">
              <li>
                <Link to="/" className="current homepage" id="current">Home</Link>
              </li>

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
                        <li><Link to="/index-2">Business Homepage</Link></li>
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
                          <img src="/images/menu-banner-01.jpg" alt="" />
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
                          <img src="/images/menu-banner-02.jpg" alt="" />
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
                <a href="#">Blog</a>
                <ul>
                  <li><Link to="/blog-standard">Blog Standard</Link></li>
                  <li><Link to="/blog-masonry">Blog Masonry</Link></li>
                  <li><Link to="/blog-single-post">Single Post</Link></li>
                </ul>
              </li>
            </ul>
          </nav>
        </div>
      </div>

      {userRole && (
        <div className="container">
          <div className="twelve columns navbar-nav">
            {userRole === "ROLE_ADMIN" && (
              <>
                <Link to="/admin">Trang Admin</Link>
                <Link to="/user">Trang User</Link>
              </>
            )}
            {userRole === "ROLE_USER" && <Link to="/user">Trang User</Link>}
            <button onClick={handleLogout} className="logout-btn">Logout</button>
          </div>
        </div>
      )}


    </>
  );
}