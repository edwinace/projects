/**
 * Module dependencies.
 */

var express = require('express'),
    io = require('socket.io')(http),
    app = express(),
    http = require('http').createServer(app),
    io = io.listen(http),
    routes = require('./routes');

var path = require('path');

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');
app.use(express.json());       // to support JSON-encoded bodies
app.use(express.urlencoded()); // to support URL-encoded bodies
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' === app.get('env')) {
  app.use(express.errorHandler());
}

app.get('/', routes.index);

//socket
var teams = [],
    id = 0;

io.on('connection', function(socket) {
  console.log('new connection');

  //quiz
  function updateTeams(teams) {
    io.sockets.emit('teams update', teams);
  };

  socket.on('new team', function(team) {
    id++;
    socket.team = {
      id: id,
      name: team.name,
      score: 0
    }
    teams.push(socket.team);
    updateTeams(teams);
  });

  socket.on('point added', function(data) {
    for (var i = 0; i < teams.length; i++) {
      if (teams[i].id === data.id)
        teams[i].score = data.score;
    }
    updateTeams(teams);
  });

  //chat
  socket.on('message', function(msg) {
    var message = {
      team: socket.team.name,
      content: msg
    };

    io.emit('broadcast', message);
  });

  socket.on('disconnect', function() {
    if ( typeof(socket.team) !== 'undefined' ) {
      for (var i = 0; i < teams.length; i++) {
        if(teams[i].name === socket.team.name) {
          teams.splice(i, 1);
        }
      };

      updateTeams(teams);
      io.sockets.emit('team left', socket.team.name);
    }
  });
});

http.listen(app.get('port'), function () {
  console.log('Express server listening on port ' + app.get('port'))
});