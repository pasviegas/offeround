/* global describe, expect, it */
'use strict';

var window = require('./helper/fakes').window();

describe('State Test', () => {
  
  it('Create app state', () => {
  	var state  = require('../src/state').create(window);
    
    expect(state.cursor).toBeDefined();
    expect(state.cursor('config')).toBeDefined();
    expect(state.cursor('map')).toBeDefined();
  });

});