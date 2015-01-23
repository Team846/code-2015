var express = require("express");
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);

app.use(express.static(__dirname + '/client'));

io.on('connection', function(socket){
  console.log('a user connected');
});

io.on('connection', function(socket){
  console.log('a user connected');
});

io.on('connection', function(socket){
  socket.on('chat message', function(msg){
    console.log('message: ' + msg);
  });
});

setInterval(function() {
  function random (low, high) {
    return Math.random() * (high - low) + low;
  }

  io.emit('data-update', { type: "motor-speed", value: random(0, 5)});
}, 100)

http.listen(3000, function(){
  console.log('listening on *:3000');
});
