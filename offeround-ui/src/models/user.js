'use strict';

module.exports.extract = function(cursor) {
  var user = cursor.get('user');
  return {
    name: user.get('name'),
    password: user.get('password')
  };
};