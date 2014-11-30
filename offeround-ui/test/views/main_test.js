/* global describe, expect, it, window, document */
'use strict';

var React = require('react/addons');
var state = require('../../src/state').create(window);
var Main  = require('../../src/views/main');

describe('Main Test', () => {
  
  it('Create view from cursor', () => {
    React.render(Main(state.cursor()), document.body);

    expect(document.querySelector('.main')).toBeDefined();
  });

});