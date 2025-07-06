import React from 'react';
import { Link } from 'react-router-dom';
import featuredImg1 from '../assets/images/featured_img_1.jpg';
import featuredImg2 from '../assets/images/featured_img_2.jpg';
import featuredImg3 from '../assets/images/featured_img_3.jpg';

const FeaturedSection = () => {
  const featuredItems = [
    {
      id: 1,
      image: featuredImg1,
      title: "Men's Shirts",
      subtitle: "25% Off Summer Styles",
      link: "/shop-with-sidebar"
    },
    {
      id: 2,
      image: featuredImg2,
      title: "Running Shoes",
      subtitle: "Sports Discount",
      link: "/shop-with-sidebar"
    },
    {
      id: 3,
      image: featuredImg3,
      title: "Winter Jackets",
      subtitle: "End-of Season Sales",
      link: "/shop-with-sidebar"
    }
  ];

  return (
    <div className="container">
      {featuredItems.map((item) => (
        <div key={item.id} className="one-third column">
          <Link to={item.link} className="img-caption">
            <figure>
              <img src={item.image} alt={item.title} />
              <figcaption>
                <h3>{item.title}</h3>
                <span>{item.subtitle}</span>
              </figcaption>
            </figure>
          </Link>
        </div>
      ))}
      <div className="clearfix"></div>
    </div>
  );
};

export default FeaturedSection; 