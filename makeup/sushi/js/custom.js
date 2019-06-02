// ==================================================
//  jCarousel module
// ==================================================
(function($) {

  $(function() {
    $(".js-jcarousel").each(function() {
      var config = initConfiguration( $(this) );

      $(this).children('.carousel-scene').jcarousel( config );

      controlConfiguration( $(this) );
    });
  });

  /* Используя классы или айди, идентифицируем элементы и их конфигурируем. */
  function initConfiguration( node ) {
    var config = {};

    if ( node.hasClass('js-jcarousel-front') ) {
      
    }
    if ( node.hasClass('js-jcarousel-basic') ) {
    } 
    // инициализация для других слайдров

    return config;
  }

  /* Используя классы или айди, идентифицируем элементы и их конфигурируем. */
  function controlConfiguration( node ) {
    if ( node.hasClass('js-jcarousel-front') ) {
      attachButtons( node );
      attachNavigation( node );
    }
    if ( node.hasClass('js-jcarousel-basic') ) {
      attachButtons( node );
    }
    
    // контролы для других слайдеров
  }

  function connectSliders( navigationCarousel, controlingCarousel ) {

    var connector = function(itemNavigation, controlingCarousel) {
      return controlingCarousel.jcarousel('items').eq(itemNavigation.index());
    };

    navigationCarousel.jcarousel('items').each(function() {
      var item = $(this);

      // This is where we actually connect to items.
      var target = connector(item, controlingCarousel);

      item
        .on('jcarouselcontrol:active', function() {
          navigationCarousel.jcarousel('scrollIntoView', this);
          item.addClass('active');
        })
        .on('jcarouselcontrol:inactive', function() {
          item.removeClass('active');
        })
        .jcarouselControl({
          target: target,
          carousel: controlingCarousel
        });
    });
  }

  function attachButtons( node ) {
    node.find('.js-carousel-control-prev')
      .on('jcarouselcontrol:active', function() {
        $(this).removeClass('inactive');
      })
      .on('jcarouselcontrol:inactive', function() {
        $(this).addClass('inactive');
      })
      .jcarouselControl({
        target: '-=1'
      });

    node.find('.js-carousel-control-next')
      .on('jcarouselcontrol:active', function() {
        $(this).removeClass('inactive');
      })
      .on('jcarouselcontrol:inactive', function() {
        $(this).addClass('inactive');
      })
      .jcarouselControl({
        target: '+=1'
      });
  }

  function attachNavigation( node ) {
    node.find('.js-carousel-nav')
      .on('jcarouselpagination:active', 'div', function() {
          $(this).addClass('active');
      })
      .on('jcarouselpagination:inactive', 'div', function() {
          $(this).removeClass('active');
      })
      .jcarouselPagination({
        item: function(page, carouselItems) {
          return '<div class="control-i"></div>';
        }
      });
  }

  function updateCarouselWidth( node ) {
    var itemWidth = document.body.offsetWidth;
    node.find('.carousel-item').css('width', itemWidth);
    node.find('.carousel-scene').css('width', itemWidth);
  }

})(jQuery);

// ==================================================
//  js-counter
// ==================================================
(function($) {

  $(function() {

    $('.js-counter').each(function() {
      var input = $(this).find('.js-counter-input'),
          up = $(this).find('.js-counter-up'),
          down = $(this).find('.js-counter-down'),
          self = $(this);

      var config = initConfiguration( $(this) );
      
      up.on('click', function() {
        var data = { up: true }
        $(this).trigger('change:counter', [ data ]);
        $(this).trigger('afterChange:counter', [ { node: self, val: config.value } ]);
      });

      down.on('click', function() {
        var data = { down: true }
        $(this).trigger('change:counter', [ data ]);
        $(this).trigger('afterChange:counter', [ { node: self, val: config.value } ]);
      });

      $(this).on('change:counter', function(e, data) {
        if (data.up) {
          if (config.max == config.value) return false;
          var i = increment(config.value, config);
          draw(input, i);
          config.value = i;
        } 
        if (data.down) {
          if (config.min == config.value) return false;
          var i = decrement(config.value, config);
          draw(input, i);
          config.value = i;
        }
      });

      $(this).on('afterChange:counter', config.onChange);

      otherScripts( self, config );
    });

    function initConfiguration( node ) {
      var config = {};

      config = {
        min: 1,
        max: 10000,
        value: 1,
        onChange: function(e, data) {
          var el = $(this).closest('.js-item').find('.js-price');
          updatePrice(el, config.value);
        }
      }

      return config;
    }

    function otherScripts( node, config ) {
      var input = node.find('.js-counter-input');

      input.on('change', function() {
        var value = $(this).val(),
            el = $(this).closest('.js-item').find('.js-price');

        config.value = value;
        updatePrice(el, value);
      });
    }

    function updatePrice(el, k) {
      el.each(function() {
        setPrice($(this), k);
      });
    }

    function setPrice(el, k) {
      var price = el.data('price');

      if ( el.is('[data-discount]') ) {
        var discount = el.data('discount');
        price = price + price * discount;
      }

      price = price * k;
      el.text(price);
    }

    function increment(i, config) {
      if (i != config.max) {
        i++;
      }
      return i;
    }

    function decrement(i, config) {
      if (i != config.min) {
        i--;
      }
      return i;
    }

    function draw(input, value) {
      input.val( value );
    }
  });

})(jQuery);

// ==================================================
//  ymap
// ==================================================
(function($) {

  $(function() {
    loadMap('ymap-contacts', {
      center: [57.13647851, 65.53780896],
      zoom: 13,
      controls: ['zoomControl']
    }, function(map) {
      var placemark = new ymaps.Placemark([57.13729008, 65.55953149], {
      }, {
        iconLayout: 'default#image',
        iconImageHref: 'images/icon-placemark.png',
        iconImageSize: [19, 28],
        iconImageOffset: [-9, -28]
      });

      map.geoObjects.add(placemark);
    });
  });

})(jQuery);

// ==================================================
//  ...
// ==================================================
(function($) {

  $(function() {

  });

})(jQuery);