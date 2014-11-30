'use strict';

var request      = require('superagent');
var ConfigModel  = require('./models/config');

module.exports.config = function(state) {
  var config = ConfigModel.extract(state.cursor());

  var mkRequest = function(url) {
    return request.post(url)
    .auth(config.user.name, config.user.password)
    .on('error', console.log);
  };
  
  var getToken = function() {
    return mkRequest(config.server.endpoint('token'));
  };
  
  var getOffers = function(callback) {
    return (res) => {
      mkRequest(config.server.endpoint('offers'))
      .query(JSON.parse(res.text))
      .end(callback);
    };
  };

  return {
    offers: function(callback){
      return getToken().end(getOffers(callback));
    }
  };
};