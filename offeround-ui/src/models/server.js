'use strict';

module.exports.extract = function(cursor) {
  var host = cursor.get('server').get('host');
  return {
    host: host,
    endpoint: (api) => {
      return host + '/' + api;
    }
  };
};