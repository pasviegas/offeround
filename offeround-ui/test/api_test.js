/* global describe, expect, it */
'use strict';

var window = require('./helper/fakes').window();
var state  = require('../src/state').create(window);
var Api    = require('../src/api');

describe('Api Test', () => {
  
  it('Config Api', () => {
    expect(Api.config(state).offers).toBeDefined();
  });

});