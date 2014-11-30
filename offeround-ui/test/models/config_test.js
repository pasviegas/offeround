/* global describe, expect, it */
'use strict';

var window = require('../helper/fakes').window();
var state  = require('../../src/state').create(window);
var Config = require('../../src/models/config');

describe('Config Test', () => {
  
  it('Extract from cursor', () => {
    var config = Config.extract(state.cursor());
    
    expect(config.user).toBeDefined();
    expect(config.server).toBeDefined();
  });

});