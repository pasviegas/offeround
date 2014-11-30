'use strict';

var immstruct = require('immstruct');

module.exports.create = function(window){
  var GoogleMapsAPI = window.google.maps;
  var LatLng        = GoogleMapsAPI.LatLng;

  return immstruct({ 
    config: {
      server: {
        host: 'http://127.0.0.1:3000/v1'
      },
      user: {
        name: 'locafox', 
        password: 'LocaF#xes!'
      },
    },
    map: {
      center: new LatLng(52.5075419, 13.4251364), //Berlin
      zoom: 12,
      width: window.innerWidth,
      height: window.innerHeight,
      markers: []
    }
  });
};