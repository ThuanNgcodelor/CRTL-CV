import React from 'react';
import { Helmet } from 'react-helmet-async';
import Navbar from "../components/Navbar";
import SliderPage from "./SliderPage.jsx";
import FeaturedSection from "../components/FeaturedSection";
import NewArrivals from "../components/NewArrivals";
import ProductLists from "../components/ProductLists";
import LatestArticles from "../components/LatestArticles";
import Footer from "../components/Footer";

export default function HomePage() {
    return (
        <div className="boxed">
            <Helmet>
                <title>CutLayout - Fashion & Style</title>
                <meta name="description" content="Discover the latest fashion trends and styles at CutLayout. Shop for men's and women's clothing, accessories, and more." />
                <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
            </Helmet>
                <Navbar />
                <FeaturedSection />
                <ProductLists />
                <NewArrivals />
                <LatestArticles />
                <div className="margin-top-50"></div>
            <Footer />

        </div>


    );
}