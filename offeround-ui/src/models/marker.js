'use strict';

module.exports.updateCursor = function(cursor, vals) {
  cursor.get('map').update('markers', () => {
    return vals;
  });
};

module.exports.fromJson = function(window, json) { 
  var GoogleMapsAPI = window.google.maps;
  var LatLng        = GoogleMapsAPI.LatLng;

  return {position: new LatLng(json.lat, json.long)};
};