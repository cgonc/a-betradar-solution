"use strict";

global.__base = __dirname + '/';

var data_container = require(__base + './data/data_container.js')();
var protocol_constants = require('./resources/protocol_constants.json');
var app = require('express')();

var rest = require('./restful/rest.js')({
    "protocol_constants": protocol_constants,
    "app": app,
    "data_container" : data_container
});
var http = require('http').Server(app);
var io = require('socket.io')(http);

var connection = require('./io/connection.js')({
    "io": io,
    "data_container" : data_container
});

http.listen(8880, function () {
    console.log('listening on *:8880');
});