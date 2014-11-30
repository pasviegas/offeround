/* global describe, expect, it, window, document */
'use strict';

var React     = require('react/addons');
var state     = require('../../src/state').create(window);
var GoogleMap = require('../../src/views/google-map');

describe('GoogleMap Test', () => {
  
  it('Create view from cursor', () => {
    React.render(GoogleMap(state.cursor()), document.body);

    expect(document.querySelector('.gm-style')).toBeDefined();
  });

});