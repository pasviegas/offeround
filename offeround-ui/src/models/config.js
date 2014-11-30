'use strict';

var User   = require('./user');
var Server = require('./server');

module.exports.extract = function(cursor) {
  var config = cursor.get('config');
  return {
    user: User.extract(config),
    server: Server.extract(config)
  };
};