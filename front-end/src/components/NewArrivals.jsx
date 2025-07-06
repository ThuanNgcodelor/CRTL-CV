// ... existing code ...
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation, Autoplay } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';
import { fetchProducts } from '../api/product'; // <-- import API
import imgDefault from '../assets/images/shop_item_01.jpg'; // fallback image

const NewArrivals = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchProducts()
      .then(res => {
        setProducts(res.data.content || []);
      })
      .catch(err => {
        setProducts([]);
        console.error('Error fetching products:', err);
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="container">
      {/* Headline */}
      <div className="sixteen columns">
        <h3 className="headline">New Arrivals</h3>
        <span className="line margin-bottom-0"></span>
      </div>

      {/* Carousel */}
      <div id="new-arrivas" className="showbiz-container sixteen columns">
        {/* Navigation */}
        <div className="showbiz-navigation">
          <div id="showbiz_left_1" className="sb-navigation-left">
            <i className="fa fa-angle-left"></i>
          </div>
          <div id="showbiz_right_1" className="sb-navigation-right">
            <i className="fa fa-angle-right"></i>
          </div>
        </div>
        <div className="clearfix"></div>

        {/* Products */}
        {loading ? (
          <div>Loading...</div>
        ) : products.length === 0 ? (
          <div>No products found.</div>
        ) : (
          <Swiper
            modules={[Navigation, Autoplay]}
            spaceBetween={30}
            slidesPerView={1}
            navigation={{
              prevEl: '#showbiz_left_1',
              nextEl: '#showbiz_right_1',
            }}
            autoplay={{
              delay: 3000,
              disableOnInteraction: false,
            }}
            breakpoints={{
              640: { slidesPerView: 2 },
              768: { slidesPerView: 3 },
              1024: { slidesPerView: 4 },
            }}
            className="showbiz"
          >
            {products.map((product) => (
              <SwiperSlide key={product.id}>
                <figure className="product">
                  {/* Nếu có trường isSale thì show SALE */}
                  {product.isSale && (
                    <div className="product-discount">SALE</div>
                  )}
                  <div className="mediaholder">
                    <Link to={`/product/${product.id}`}>
                      <img
                        alt={product.name}
                        src={product.image || imgDefault}
                        onError={e => { e.target.src = imgDefault; }}
                      />
                      <div className="cover">
                        <img
                          alt={product.name}
                          src={product.hoverImage || product.image || imgDefault}
                          onError={e => { e.target.src = imgDefault; }}
                        />
                      </div>
                    </Link>
                  </div>
                  <Link to={`/product/${product.id}`}>
                    <section>
                      <span className="product-category">{product.category || ''}</span>
                      <h5>{product.name}</h5>
                      {product.isSale ? (
                        <span className="product-price-discount">
                          {product.price}
                          <i>{product.originalPrice}</i>
                        </span>
                      ) : (
                        <span className="product-price">{'$'+product.price.toLocaleString('us')}</span>
                      )}
                      <Link to="/checkout-billing-details" className="button gray cart-btns w-100">
                        Add To Cart
                      </Link>
                    </section>
                  </Link>
                </figure>
              </SwiperSlide>
            ))}
          </Swiper>
        )}
      </div>
    </div>
  );
};

export default NewArrivals;