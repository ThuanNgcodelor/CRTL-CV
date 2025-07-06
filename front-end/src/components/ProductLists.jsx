import React from 'react';
import { Link } from 'react-router-dom';

import img01 from '../assets/images/small_product_list_01.jpg';
import img02 from '../assets/images/small_product_list_02.jpg';
import img03 from '../assets/images/small_product_list_03.jpg';
import img04 from '../assets/images/small_product_list_04.jpg';
import img05 from '../assets/images/small_product_list_05.jpg';
import img06 from '../assets/images/small_product_list_06.jpg';
import img07 from '../assets/images/small_product_list_07.jpg';
import img08 from '../assets/images/small_product_list_08.jpg';
import img09 from '../assets/images/small_product_list_09.jpg';


const ProductLists = () => {
  const bestSellers = [
    {
      id: 1,
      image: img01,
      name: 'Canvas Backpack',
      price: '$59.00'
    },
    {
      id: 2,
      image: img02,
      name: 'Long Sleeve Shirt',
      price: '$29.00'
    },
    {
      id: 3,
      image: img03,
      name: 'Tommy Hilfiger Shirt Beat',
      price: '$89.00'
    }
  ];

  const topRated = [
    {
      id: 1,
      image: img04,
      name: 'Brogue Boots in Leather',
      price: '$99.00',
      rating: 5
    },
    {
      id: 2,
      image: img05,
      name: 'Slim Jeans With Blue Tint',
      price: '$79.00',
      rating: 4
    },
    {
      id: 3,
      image: img06,
      name: 'New Look Fairisle Scarf',
      price: '$19.00',
      rating: 3
    }
  ];

  const weeklySales = [
    {
      id: 1,
      image: img07,
      name: 'Short Sleeve Polo Shirt',
      price: '$19.00',
      originalPrice: '$29.00'
    },
    {
      id: 2,
      image: img08,
      name: 'Long Sleeve Shirt',
      price: '$79.00',
      originalPrice: '$99.00'
    },
    {
      id: 3,
      image: img09,
      name: 'Tommy Hilfiger Shirt Beat',
      price: '$399.00',
      originalPrice: '$499.00'
    }
  ];

  const renderStars = (rating) => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(
        <div key={i} className={`star ${i <= rating ? 'filled' : ''}`}></div>
      );
    }
    return stars;
  };

  return (
    <div className="container margin-bottom-25">
      {/* Best Sellers */}
      <div className="one-third column">
        <h3 className="headline">Best Sellers</h3>
        <span className="line margin-bottom-0"></span>
        <div className="clearfix"></div>

        <ul className="product-list">
          {bestSellers.map((product) => (
            <li key={product.id}>
              <Link to="#">
                <img src={product.image} alt={product.name} />
                <div className="product-list-desc">
                  {product.name} <i>{product.price}</i>
                </div>
              </Link>
            </li>
          ))}
          <li><div className="clearfix"></div></li>
        </ul>
      </div>

      {/* Top Rated */}
      <div className="one-third column">
        <h3 className="headline">Top Rated</h3>
        <span className="line margin-bottom-0"></span>
        <div className="clearfix"></div>

        <ul className="product-list top-rated">
          {topRated.map((product) => (
            <li key={product.id}>
              <Link to="#">
                <img src={product.image} alt={product.name} />
                <div className="product-list-desc with-rating">
                  {product.name} <i>{product.price}</i>
                  <div className={`rating ${product.rating}-stars`}>
                    <div className="star-rating">
                      {renderStars(product.rating)}
                    </div>
                    <div className="star-bg"></div>
                  </div>
                </div>
              </Link>
            </li>
          ))}
          <li><div className="clearfix"></div></li>
        </ul>
      </div>

      {/* Weekly Sales */}
      <div className="one-third column">
        <h3 className="headline">Weekly Sales</h3>
        <span className="line margin-bottom-0"></span>
        <div className="clearfix"></div>

        <ul className="product-list discount">
          {weeklySales.map((product) => (
            <li key={product.id}>
              <Link to="#">
                <img src={product.image} alt={product.name} />
                <div className="product-list-desc">
                  {product.name} <i>{product.originalPrice}<b>{product.price}</b></i>
                </div>
              </Link>
            </li>
          ))}
          <li><div className="clearfix"></div></li>
        </ul>
      </div>
    </div>
  );
};

export default ProductLists; 