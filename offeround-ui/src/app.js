/* global document, window */
'use strict';

var React   = require('react/addons');
var state   = require('./state').create(window);
var Api     = require('./api');
var Main    = require('./views/main');
var Marker  = require('./models/marker');

function render () {
  React.render(
    Main(state.cursor()), 
    document.getElementById('container')
  );
}

Api.config(state).offers((response) => {
  Marker.updateCursor(
    state.cursor(), 
    JSON.parse(response.text).map(Marker.fromJson.bind(this, window))
  );
});

state.on('swap', render);
render();