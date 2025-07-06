import Navbar from "@components/Navbar.jsx";
import Footer from "@components/Footer.jsx";
import Cart from "@components/Cart";
import React from "react";

export default function CartPage() {
    return (
        <div className="boxed">

            <div id="wrapper">
                <Navbar />
                <Cart/>
                <div className="margin-top-50"></div>
                <Footer />
            </div>
        </div>
    );
}