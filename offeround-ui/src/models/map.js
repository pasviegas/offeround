'use strict';

module.exports.extract = function(cursor) {
  var map = cursor.get('map');
  return {
    initialZoom: map.get('zoom'),
    center: map.get('center'),
    width: map.get('width'),
    height: map.get('height'),
    markers: map.get('markers')
  };
};

module.exports.updateCenter = function(cursor, map) {
  cursor.get('map').update('center', () => {
    return map.getCenter();
  });
};