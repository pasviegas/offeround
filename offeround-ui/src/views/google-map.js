'use strict';

var component       = require('omniscient');
var ReactGoogleMaps = require('react-googlemaps');
var MapModel        = require('../models/map');
var ReactMap        = ReactGoogleMaps.Map;
var ReactMarker     = ReactGoogleMaps.Marker;

module.exports = component((props) => {
  var markerToView = (marker, i) => {
    return ReactMarker({position: marker.position, key:i});
  };

  var handleCenterChange = (mapview) => {
    MapModel.updateCenter(props.cursor, mapview);
  };

  var map = MapModel.extract(props.cursor);
  return ReactMap({
    initialZoom: map.initialZoom,
    center: map.center,
    onCenterChange: handleCenterChange,
    width: map.width,
    height: map.height
  }, map.markers.map(markerToView));
});