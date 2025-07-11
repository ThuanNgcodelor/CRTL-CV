import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, Pagination, Navigation } from "swiper/modules";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import slider1 from '../assets/images/slider2.jpg';
import slider2 from '../assets/images/slider.jpg';
import slider3 from '../assets/images/slider3.jpg';

const HomeSlider = () => {
    const slides = [
        {
            id: 1,
            image: slider1,
            title: "Urban Style",
            subtitle: "Every cut and colour",
            position: "right"
        },
        {
            id: 2,
            image: slider2,
            title: "Dress Sharp",
            subtitle: "Learn from the classics",
            position: "left"
        },
        {
            id: 3,
            image: slider3,
            title: "New In",
            subtitle: "Pants and T-Shirts",
            position: "right"
        }
    ];

    return (
        <div className="container">
            <div className="tp-banner-container">
                <div className="tp-banner">
                    <Swiper
                        modules={[Autoplay, Pagination, Navigation]}
                        spaceBetween={0}
                        slidesPerView={1}
                        autoplay={{
                            delay: 5000,
                            disableOnInteraction: false,
                        }}
                        pagination={{ clickable: true }}
                        navigation={true}
                        className="mySwiper"
                        loop={true}
                    >
                        {slides.map((slide) => (
                            <SwiperSlide key={slide.id}>
                                <div className="slide-container">
                                    <img 
                                        src={slide.image} 
                                        alt={slide.title} 
                                        className="slide-image"
                                    />
                                    <div className={`caption ${slide.position === 'right' ? 'dark' : ''} sfb fadeout`}>
                                        <h2>{slide.title}</h2>
                                        <h3>{slide.subtitle}</h3>
                                        <a href="/shop-with-sidebar" className="caption-btn">
                                            Shop The Collection
                                        </a>
                                    </div>
                                </div>
                            </SwiperSlide>
                        ))}
                    </Swiper>
                </div>
            </div>
        </div>
    );
};
export default HomeSlider;
