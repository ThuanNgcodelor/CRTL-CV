import { Link } from 'react-router-dom';
import img8 from '@assets/images/small_product_list_08.jpg';
import img9 from '@assets/images/small_product_list_09.jpg';

export default function Cart() {
    return (
        <>
            <section className="titlebar">
                <div className="container">
                    <div className="sixteen columns">
                        <h2>Shopping Cart</h2>
                        <nav id="breadcrumbs">
                            <ul>
                                <li><Link to="/">Home</Link></li>
                                <li>Shopping Cart</li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </section>

            <div className="container cart">
                <div className="sixteen columns">
                    {/* Cart Table */}
                    <table className="cart-table responsive-table">
                        <thead>
                        <tr>
                            <th>Item</th>
                            <th>Description</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Total</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {/* Item 1 */}
                        <tr>
                            <td><img src={img8} alt="Converse All Star Trainers" /></td>
                            <td className="cart-title"><Link to="#">Converse All Star Trainers</Link></td>
                            <td>$79.00</td>
                            <td>
                                <form>
                                    <div className="qtyminus"></div>
                                    <input type="text" name="quantity" defaultValue="1" className="qty" />
                                    <div className="qtyplus"></div>
                                </form>
                            </td>
                            <td className="cart-total">$79.00</td>
                            <td>
                                <Link to="/checkout-billing-details" className="cart-remove" aria-label="Remove item">
                                </Link></td>
                        </tr>

                        {/* Item 2 */}
                        <tr>
                            <td><img src={img9} alt="Wool Two-Piece Suit" /></td>
                            <td className="cart-title"><Link to="#">Wool Two-Piece Suit</Link></td>
                            <td>$99.00</td>
                            <td>
                                <form>
                                    <div className="qtyminus"></div>
                                    <input type="text" name="quantity" defaultValue="1" className="qty" />
                                    <div className="qtyplus"></div>
                                </form>
                            </td>
                            <td className="cart-total">$99.00</td>
                            <td>
                                <Link to="/checkout-billing-details" className="cart-remove" aria-label="Remove item">
                                </Link></td>
                            </tr>
                        </tbody>
                    </table>

                    {/* Coupon + Buttons */}
                    <table className="cart-table bottom">
                        <tbody>
                        <tr>
                            <th>
                                <form className="apply-coupon">
                                    <input className="search-field" type="text" placeholder="Coupon Code" />
                                    <Link to="/checkout-billing-details" className="button gray">
                                        Apply Coupon
                                    </Link>
                                </form>

                                <div className="cart-btns">
                                    <Link to="/checkout-billing-details" className="button color cart-btns proceed">
                                        Proceed to Checkout
                                    </Link>
                                    <Link to="/checkout-billing-details" className="button gray cart-btns">
                                        Update Cart
                                    </Link>
                                </div>
                            </th>
                        </tr>
                        </tbody>
                    </table>
                </div>

                {/* Cart Totals */}
                <div className="eight columns cart-totals">
                    <h3 className="headline">Cart Totals</h3>
                    <span className="line"></span>
                    <div className="clearfix"></div>

                    <table className="cart-table margin-top-5">
                        <tbody>
                        <tr>
                            <th>Cart Subtotal</th>
                            <td><strong>$178.00</strong></td>
                        </tr>
                        <tr>
                            <th>Shipping and Handling</th>
                            <td>Free Shipping</td>
                        </tr>
                        <tr>
                            <th>Order Total</th>
                            <td><strong>$178.00</strong></td>
                        </tr>
                        </tbody>
                    </table>
                    <br />
                </div>
            </div>
        </>
    );
}
