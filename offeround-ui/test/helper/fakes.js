'use strict';

module.exports.window = function(){
  return {
    google: {
      maps: {
        LatLng: function(latitude, longitude){
          return {lat: latitude, lng: longitude};
        }
      }
    },
    innerWidth: 700,
    innerHeight: 700
  };
};

module.exports.map = function(){
  return {
    getCenter: function(){
      return {lat: 5, lng: 5};
    }
  };
};