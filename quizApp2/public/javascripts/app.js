'use strict';

(function() {

	var app = angular.module('Quizmaster', [
		'ngRoute',
		'appControllers',
		'btford.socket-io'
	]);

	app.config(function($routeProvider, $locationProvider) {
		$routeProvider.
			when('/quiz', {
				templateUrl: 'templates/home.html',
				controller: 'HomeCtrl'
			}).
			when('/quizmaster', {
				templateUrl: 'templates/quizmaster.html',
				controller: 'MasterCtrl'
			}).
			when('/chat', {
				templateUrl: 'templates/chat.html',
				controller: 'ChatCtrl'
			}).
			otherwise({
				templateUrl: 'templates/addTeam.html',
				controller: 'NewTeamCtrl'
			});
	});

	app.factory('socket', function (socketFactory) {
		var socket = socketFactory();
    socket.forward(['broadcast', 'team joined', 'teams update', 'team left']);
    return socket;
	});

}());
