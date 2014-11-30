/* global describe, expect, it */
'use strict';

var window = require('../helper/fakes').window();
var state  = require('../../src/state').create(window);
var User = require('../../src/models/user');

describe('User Test', () => {
  
  it('Extract from cursor', () => {
    var user = User.extract(state.cursor().get('config'));

    expect(user.name).toBeDefined();
    expect(user.name).toBe('locafox');
    expect(user.password).toBeDefined();
    expect(user.password).toBe('LocaF#xes!');
  });

});