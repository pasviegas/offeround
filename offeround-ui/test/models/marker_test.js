/* global describe, expect, it */
'use strict';

var fakes    = require('../helper/fakes');
var state    = require('../../src/state').create(fakes.window());
var Marker   = require('../../src/models/marker');
var MapModel = require('../../src/models/map');

describe('Marker Test', () => {
  
  it('Extract from json', () => {
    var marker = Marker.fromJson(fakes.window(), {lat: 5, long: 5});

    expect(marker.position).toBeDefined();
  });

  it('Update markers in map', () => {
    Marker.updateCursor(state.cursor(), [{lat: 5, lng: 5}]);
    var map = MapModel.extract(state.cursor());
    
    expect(map.markers.length).toBe(1);
  });
});