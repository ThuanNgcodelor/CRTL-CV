import React, { useEffect, useState } from 'react';
import {getCart} from "@api/user.js";
import {Link} from "react-router-dom";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import {fetchImageById} from "@api/image.js";

export default function Cart() {
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [imageUrls, setImageUrls] = useState({});
    const navigate = useNavigate();
    const token = Cookies.get('token');

    useEffect(() => {
        if (!token) {
            navigate("/auth");
            return;
        }
        async function fetchCart() {
            try {
                const data = await getCart();
                setCart(data);
            } catch (err) {
                setError('Failed to load cart');
            } finally {
                setLoading(false);
            }
        }
        fetchCart();
    }, []);


    useEffect(() => {
        const fetchImages = async () => {
            if (!Array.isArray(cart?.items)) return;

            const urls = {};

            await Promise.all(cart.items.map(async (item) => {
                if (item.imageId) {
                    try {
                        const res = await fetchImageById(item.imageId);
                        const blob = new Blob([res.data], { type: res.headers['content-type'] });
                        urls[item.id] = URL.createObjectURL(blob);
                        console.log(item.id);
                    } catch (err) {
                        urls[item.id] = null;
                    }
                }
            }));

            setImageUrls(urls);
        };

        fetchImages();
    }, [cart]);


    if (loading) return <div className="container cart"><p>Loading cart...</p></div>;
    if (error) return <div className="container cart"><p>{error}</p></div>;
    if (!cart || !cart.items || cart.items.length === 0) {
        return <div className="container cart"><p>Your cart is empty.</p></div>;
    }

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
                            <th>Unit Price</th>
                            <th>Quantity</th>
                            <th>Total</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        {cart.items.map((item) => (
                            <tr key={item.id}>
                                <input value={item.productId} type="hidden" />
                                <td>
                                    <img
                                        src={imageUrls[item.id] }
                                        alt={item.description}
                                        style={{ width: '150px', height: '150px' }}
                                    />
                                </td>
                                <td className="cart-title">{item.description}</td>
                                <td>{item.unitPrice.toLocaleString()}₫</td>
                                <td>{item.quantity}</td>
                                <td className="cart-total">{item.totalPrice.toLocaleString()}₫</td>
                                <td>
                                <Link to="/checkout-billing-details" className="cart-remove" aria-label="Remove item">
                                </Link></td>
                            </tr>

                        ))}
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
                            <td><strong>{cart.totalAmount.toLocaleString()}</strong></td>
                        </tr>
                        <tr>
                            <th>Shipping and Handling</th>
                            <td>Free Shipping</td>
                        </tr>
                        <tr>
                            <th>Order Total</th>
                            <td><strong>{cart.totalAmount.toLocaleString()}</strong></td>
                        </tr>
                        </tbody>
                    </table>
                    <br />
                </div>
            </div>
        </>
    );
}
