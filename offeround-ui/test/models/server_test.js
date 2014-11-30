/* global describe, expect, it */
'use strict';

var window = require('../helper/fakes').window();
var state  = require('../../src/state').create(window);
var Server = require('../../src/models/server');

describe('Server Test', () => {
  
  it('Extract from cursor', () => {
    var server = Server.extract(state.cursor().get('config'));
    
    expect(server.host).toBeDefined();
    expect(server.host).toBe('http://127.0.0.1:3000/v1');
    expect(server.endpoint).toBeDefined();
  });

  it('Endpoint creation', () => {
    var server = Server.extract(state.cursor().get('config'));
  
    expect(server.endpoint('token')).toBe(server.host + '/token');
  });

});