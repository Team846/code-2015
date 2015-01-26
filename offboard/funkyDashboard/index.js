var express = require("express");
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);

app.use(express.static(__dirname + '/client'));

io.on('connection', function(socket){

});

var counter = 10;
var prev = false;

setInterval(function() {
  function random (low, high) {
    return Math.random() * (high - low) + low;
  }

  if (counter == 0) {
    prev = !prev;
    counter = 10;
  }

  counter--;

  io.emit('data-update', { type: "motor-speed", value: random(0, 5)});
  io.emit('data-update', { type: "robot-on", value: prev });
}, 40)

http.listen(3000, function(){
  console.log('listening on *:3000');
});
