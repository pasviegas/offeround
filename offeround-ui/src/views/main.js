'use strict';

var React     = require('react/addons');
var component = require('omniscient');
var GoogleMap = require('./google-map');

module.exports = component((props) => {
  return React.DOM.section({className: 'main'},
          React.DOM.div({}, GoogleMap(props.cursor)));
});