import React from 'react';
import { Link } from 'react-router-dom';

import blogImg1 from '../assets/images/from_the_blog_01.jpg';
import blogImg2 from '../assets/images/from_the_blog_02.jpg';
import blogImg3 from '../assets/images/from_the_blog_03.jpg';
import blogImg4 from '../assets/images/from_the_blog_04.jpg';


const LatestArticles = () => {
  const articles = [
    {
      id: 1,
      image: blogImg1,
      title: 'Amazon Raises Threshold for Free Shipping',
      author: 'By Vasterad on May 31, 2014',
      excerpt: 'Pellentesque ultricies vehicula eleifend. Aenean eu nunc semper faucibus sapien viverra.',
      link: '/blog-single-post'
    },
    {
      id: 2,
      image: blogImg2,
      title: 'How To Read The Symbols on Your Clothing Tags',
      author: 'By Vasterad on May 16, 2014',
      excerpt: 'Morbi quis magna nec lacus nunc eratu pharetra lorem sed sapien velit adipiscing.',
      link: '/blog-single-post'
    },
    {
      id: 3,
      image: blogImg3,
      title: 'Online Shopping Hit New Highs on Mobile Sales',
      author: 'By Vasterad on May 10, 2014',
      excerpt: 'Donec non egestas nisl. Aliquam tincidunt sem sed nisl dictum. Fusce gravida arius gravida.',
      link: '/blog-single-post'
    },
    {
      id: 4,
      image: blogImg4,
      title: 'How to Choose Size - Does Your Shirt Fit Properly?',
      author: 'By Vasterad on April 20, 2014',
      excerpt: 'Nullam eget ante cursus, dignissim velit sit amet, tempus massa bibendum venenatis.',
      link: '/blog-single-post'
    }
  ];

  return (
    <div className="container">
      {/* Headline */}
      <div className="sixteen columns">
        <h3 className="headline">Latest Articles</h3>
        <span className="line margin-bottom-30"></span>
      </div>

      {/* Articles */}
      {articles.map((article) => (
        <div key={article.id} className="four columns">
          <article className="from-the-blog">
            <figure className="from-the-blog-image">
              <Link to={article.link}>
                <img src={article.image} alt={article.title} />
              </Link>
              <div className="hover-icon"></div>
            </figure>

            <section className="from-the-blog-content">
              <Link to={article.link}>
                <h5>{article.title}</h5>
              </Link>
              <i>{article.author}</i>
              <span>{article.excerpt}</span>
              <Link to={article.link} className="button gray">
                Read More
              </Link>
            </section>
          </article>
        </div>
      ))}
    </div>
  );
};

export default LatestArticles; 