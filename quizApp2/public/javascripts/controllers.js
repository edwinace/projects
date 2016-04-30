'use strict';

(function () {

  var appControllers = angular.module('appControllers', []);

  //show teams, controls for points and delete
  appControllers.controller('HomeCtrl', function ($rootScope, $scope, $route, $location, socket) {
    //handle team socket events
    $scope.$on('socket:teams update', function (event, teams) {
      $rootScope.teams = teams;
    });
    
    //handle points and deletion
    $scope.addPoint = function (id, score) {
      score += 1;
      var data  = { id: id, score: score };
      socket.emit('point added', data);
    };

    //handle keyboard events
    // document.onkeydown = checkKey;

    // function checkKey (e) {
    //   e = e || window.event;

    //   if (e.keyCode === 27) {
    //     e.preventDefault();
    //     console.log('escape');
    //   };
    // }
  });

  appControllers.controller('MasterCtrl', function ($rootScope, $scope, socket) {
    $scope.pagename = 'Hail Quizmaster';

    $scope.$on('socket:teams update', function (event, teams) {
      $rootScope.teams = teams;
    });

    //handle points and deletion
    $scope.addPoint = function (id, score) {
      score += 1;
      var data  = { id: id, score: score };
      socket.emit('point added', data);
    };
  });

  appControllers.controller('ChatCtrl', function ($scope, socket) {
    //team chat
    $scope.messages = [],
    $scope.msgForm = {},
    $scope.teamEvents = [];


    $scope.sendMsg = function () {
      socket.emit('message', $scope.msgForm.msg);
      $scope.msgForm = {};
    };

    $scope.$on('socket:broadcast', function (event, message) {
      if ( $scope.messages.length >= 8 ) {  
        $scope.messages.splice(0, 1);
        $scope.messages.push(message);
      } else {
        $scope.messages.push(message);
      }
    });

    $scope.$on('socket:team left', function (event, team) {
      $scope.teamEvents.push(team + ' wuzzed out!');
    });
  });

  //add new team, set team score to 0
  appControllers.controller('NewTeamCtrl', function ($rootScope, $scope, $location, socket) {
    $scope.pagename = 'Choose a team name';
    $scope.teamForm = {};
    $rootScope.teams = [];

    //set team id for deletion on disconnect
    $scope.submitTeam = function () {
      var team = {
        name: $scope.teamForm.name
      }

      $rootScope.teams.push(team);
      socket.emit('new team', team);
      $location.url('/quiz');
    };

    $scope.$on('socket:teams update', function (event, teams) {
      $rootScope.teams = teams;
    });
  });

}());