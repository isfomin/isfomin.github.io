// ==================================================
//  ui-slider module
// ==================================================
(function($) {

  $(function() {
    $('.js-ui-slider').each(function() {
      var config = getConfig( $(this) );
      $(this).slider( config );
      syncValues( $(this) );
    });
  });

  function getConfig( slider ) {
    var config = {};

    /* Используя классы или айди, идентифицируем элементы и их конфигурируем. */
    if ( slider.attr('id') == 'slider-range-price' ) {
      var minValue = parseInt(slider.attr('data-min-value')) || 1000000,
          maxValue = parseInt(slider.attr('data-max-value')) || 10000000,
          leftValue = parseInt(slider.attr('data-left-value')) || minValue + maxValue * 0.1,
          rightValue = parseInt(slider.attr('data-right-value')) || maxValue - maxValue * 0.1;

      config = {
        range: true,
        min: minValue,
        max: maxValue,
        values: [leftValue, rightValue],
        animate: 1,
        step: 1000,
        create: function( event, ui ) {
          drawValues(slider, leftValue, rightValue);
        },
        slide: function( event, ui ) {
          drawValues(slider, ui.values[0], ui.values[1]);
          updateMask();
        }
      };

    }

    if ( slider.attr('id') == 'slider-range-area' ) {
      var minValue = parseInt(slider.attr('data-min-value')) || 40,
          maxValue = parseInt(slider.attr('data-max-value')) || 160,
          leftValue = parseInt(slider.attr('data-left-value')) || minValue + maxValue * 0.1,
          rightValue = parseInt(slider.attr('data-right-value')) || maxValue - maxValue * 0.1;

      config = {
        range: true,
        min: minValue,
        max: maxValue,
        values: [leftValue, rightValue],
        animate: 1,
        step: 10,
        create: function( event, ui ) {
          drawValues(slider, leftValue, rightValue);
        },
        slide: function( event, ui ) {
          drawValues(slider, ui.values[0], ui.values[1]);
        }
      };

    }

    if ( slider.attr('id') == 'slider-range-floor' ) {
      var minValue = parseInt(slider.attr('data-min-value')) || 1,
          maxValue = parseInt(slider.attr('data-max-value')) || 16,
          leftValue = parseInt(slider.attr('data-left-value')) || minValue + maxValue * 0.1,
          rightValue = parseInt(slider.attr('data-right-value')) || maxValue - maxValue * 0.1;

      config = {
        range: true,
        min: minValue,
        max: maxValue,
        values: [leftValue, rightValue],
        animate: 1,
        step: 1,
        create: function( event, ui ) {
          drawValues(slider, leftValue, rightValue);
        },
        slide: function( event, ui ) {
          drawValues(slider, ui.values[0], ui.values[1]);
        }
      };

    }

    return config;
  }
  
  function drawValues( slider, v1, v2 ) {
    slider.closest('.js-ui-slider-item').find('.js-ui-slider-min-value').val( v1 );
    slider.closest('.js-ui-slider-item').find('.js-ui-slider-max-value').val( v2 );
  }

  function updateMask() {
    $('.js-mask-price').unmask();
    $('.js-mask-price').mask(
      '000 000 000 000',
      {
        reverse: true
      }
    );
  }

  function syncValues( slider ) {
    var minValueInput = slider.closest('.js-ui-slider-item').find('.js-ui-slider-min-value'),
        maxValueInput = slider.closest('.js-ui-slider-item').find('.js-ui-slider-max-value');
    
    minValueInput.change( updateSlider );
    maxValueInput.change( updateSlider );

    function updateSlider() {
      var v1 = minValueInput.cleanVal(),
          v2 = maxValueInput.cleanVal();

      slider.slider('option', 'values', [v1, v2]);
    }
  }

})(jQuery);

// ==================================================
//  jquery mask
// ==================================================
(function($) {

  $(function() {
    $('.js-mask-price').mask(
      '000 000 000 000',
      {
        reverse: true
      }
    );
  });

})(jQuery);

// ==================================================
//  ymaps
// ==================================================
(function($) {

  $(function() {
    loadMap('ymap-main', {
      center: [57.10199232, 65.58104439],
      zoom: 13,
      controls: ['zoomControl']
    }, function(map) {
      map.behaviors.disable('scrollZoom');

      var objectContentLayout = ymaps.templateLayoutFactory.createClass(
        '<div class="h-card h-card-placemark">' +
          '<div class="card card-label shadow shadow-card" style="top: {{properties.offsetY}}px; left: {{properties.offsetX}}px;">' +
            '<div class="card-view">' +
              '<img src="{{ properties.imgPath }}" alt="" class="card-img">' +
            '</div>' +
            '<div class="card-title">' +
              '<div class="card-button button button-thin button-quail button-rounded button-block">{{ properties.name }}</div>' +
            '</div>' +
          '</div>' +
        '</div>');

      var objectCollection = new ymaps.GeoObjectCollection({}, {
          iconLayout: objectContentLayout,
          // активная область
          iconShape: {
            type: 'Rectangle',
            coordinates: [
                [-68, -105], [69, 1]
            ]
          }
        }
      );

      var pm1 = new ymaps.Placemark([57.08916883, 65.61517123], 
        {
          imgPath: 'images/temp/pic-object4.jpg',
          name: 'ЖК Соло'
        }
      );

      var pm2 = new ymaps.Placemark([57.10702608, 65.61851863], 
        {
          imgPath: 'images/temp/pic-object1.jpg',
          name: 'ЖК Словцово'
        }
      );

      var pm3 = new ymaps.Placemark([57.09824452, 65.62984828], 
        {
          imgPath: 'images/temp/pic-object2.jpg',
          name: 'ЖК Квартет'
        }
      );

      var pm4 = new ymaps.Placemark([57.09076921, 65.64203624], 
        {
          imgPath: 'images/temp/pic-object3.jpg',
          name: 'ЖК Серебрянные ключи'
        },
        {
          iconShape: {
            type: 'Rectangle',
            // Текст разбит на 2 строки, поэтому активная область немного больше чем у других меток
            coordinates: [
                [-68, -105], [69, 12]
            ]
          }
        }
      );

      objectCollection.add(pm1);
      objectCollection.add(pm2);
      objectCollection.add(pm3);
      objectCollection.add(pm4);
      
      map.geoObjects.add( objectCollection );
    });
  });

})(jQuery);

// ==================================================
//  ui-tooltip
// ==================================================
(function($) {

  $( document ).tooltip({
    position: {
      my: "center bottom-10",
      at: "right+20 top"
    }
  });

})(jQuery);

// ==================================================
//  ...
// ==================================================
(function($) {

  $(function() {

  });

})(jQuery);