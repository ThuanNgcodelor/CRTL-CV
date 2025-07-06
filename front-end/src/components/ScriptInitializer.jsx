import { useEffect } from 'react';

export default function ScriptInitializer() {
  useEffect(() => {
    const timer = setTimeout(() => {
      if (typeof window !== 'undefined' && window.jQuery) {
        const $ = window.jQuery;
        
        if ($.fn.superfish) {
          $('ul.menu').superfish({
            delay: 400,
            speed: 200,
            speedOut: 100,
            autoArrows: true
          });
        }

        // Khởi tạo lại Revolution Slider
        if ($.fn.revolution) {
          $('.tp-banner').revolution({
            delay: 9000,
            startwidth: 1290,
            startheight: 480,
            hideThumbs: 10,
            hideTimerBar: "on",
            onHoverStop: "on",
            navigationType: "none",
            soloArrowLeftHOffset: 0,
            soloArrowLeftVOffset: 0,
            soloArrowRightHOffset: 0,
            soloArrowRightVOffset: 0
          });
        }

        // Khởi tạo lại ShowBiz Carousel
        if ($.fn.showbizpro) {
          $('#new-arrivals').showbizpro({
            dragAndScroll: "off",
            visibleElementsArray: [4, 4, 3, 1],
            carousel: "off",
            entrySizeOffset: 0,
            allEntryAtOnce: "off",
            rewindFromEnd: "off",
            autoPlay: "off",
            delay: 2000,
            speed: 400,
            easing: 'Back.easeOut'
          });

          $('#happy-clients').showbizpro({
            dragAndScroll: "off",
            visibleElementsArray: [1, 1, 1, 1],
            carousel: "off",
            entrySizeOffset: 0,
            allEntryAtOnce: "off"
          });

          $('#our-clients').showbizpro({
            dragAndScroll: "off",
            visibleElementsArray: [5, 4, 3, 1],
            carousel: "off",
            entrySizeOffset: 0,
            allEntryAtOnce: "off"
          });
        }

        // Khởi tạo lại Parallax
        if ($.fn.pureparallax) {
          $(".parallax-banner").pureparallax({
            overlayBackgroundColor: '#000',
            overlayOpacity: '0.45',
            timeout: 200
          });

          $(".parallax-titlebar").pureparallax({
            timeout: 100
          });
        }

        // Khởi tạo lại Magnific Popup
        if ($.fn.magnificPopup) {
          $('.mfp-image').magnificPopup({
            type: 'image',
            closeOnContentClick: true,
            closeBtnInside: false,
            fixedContentPos: true,
            mainClass: 'mfp-no-margins mfp-with-zoom',
            gallery: {
              enabled: true,
              navigateByImgClick: true,
              preload: [0, 1]
            },
            image: {
              verticalFit: true
            },
            zoom: {
              enabled: true,
              duration: 300
            }
          });
        }

        // Khởi tạo lại Flexslider
        if ($.fn.flexslider) {
          $('.flexslider').flexslider({
            animation: "slide",
            animationLoop: false,
            itemWidth: 210,
            itemMargin: 5,
            minItems: 2,
            maxItems: 4
          });
        }

        // Khởi tạo lại Counter Up
        if ($.fn.counterUp) {
          $('.counter').counterUp({
            delay: 10,
            time: 1000
          });
        }

        // Khởi tạo lại Isotope
        if ($.fn.isotope) {
          $('.isotope').isotope({
            itemSelector: '.element',
            layoutMode: 'fitRows'
          });
        }

        // Khởi tạo lại Selectric
        if ($.fn.selectric) {
          $('select').selectric();
        }

        // Khởi tạo lại Royal Slider
        if ($.fn.royalSlider) {
          $('.royalSlider').royalSlider({
            arrowsNav: true,
            arrowsNavAutoHide: false,
            fadeinLoadedSlide: false,
            controlNavigation: 'bullets',
            autoHeight: false,
            autoScaleSlider: true,
            autoScaleSliderWidth: 800,
            autoScaleSliderHeight: 400,
            loop: false,
            numImagesToPreload: 4,
            navigateByClick: true,
            startSlideId: 0,
            randomizeSlides: false,
            imageScaleMode: 'fill',
            imageAlignCenter: true,
            block: {
              moveEffect: 'none'
            },
            thumbs: {
              appendSpan: true,
              firstMargin: true,
              paddingBottom: 24
            },
            thumbsFitInViewport: true,
            keyboardNavEnabled: true,
            visibleNearby: {
              enabled: true,
              centerArea: 0.5,
              center: true,
              breakpoint: 650,
              breakpointCenterArea: 0.64,
              topSpacing: 0,
              bottomSpacing: 0
            }
          });
        }

        // Khởi tạo lại Tooltips
        if ($.fn.tooltip) {
          $('[data-tooltip]').tooltip();
        }

        // Khởi tạo lại Price Filter
        if ($.fn.priceFilter) {
          $('.price-filter').priceFilter();
        }

        // Chạy lại custom.js nếu có function init
        if (typeof window.initCustomScripts === 'function') {
          window.initCustomScripts();
        }
      }
    }, 100);

    return () => clearTimeout(timer);
  }, []);

  return null; // Component này không render gì
} 