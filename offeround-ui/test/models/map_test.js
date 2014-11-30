/* global describe, expect, it */
'use strict';

var fakes    = require('../helper/fakes');
var state    = require('../../src/state').create(fakes.window());
var MapModel = require('../../src/models/map');

describe('Map Test', () => {
  
  it('Extract from cursor', () => {
    var map = MapModel.extract(state.cursor());

    expect(map.initialZoom).toBeDefined();
    expect(map.center).toBeDefined();
    expect(map.width).toBeDefined();
    expect(map.height).toBeDefined();
    expect(map.markers).toBeDefined();
  });


  it('Update center from map', () => {
    MapModel.updateCenter(state.cursor(), fakes.map());
	var map = MapModel.extract(state.cursor());
    
    expect(map.center.lat).toBe(5);
    expect(map.center.lng).toBe(5);
  });
});