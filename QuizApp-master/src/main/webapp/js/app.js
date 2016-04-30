var QuizApp = {};
QuizApp.ns = {};

(function() {

	QuizApp.gcm = QuizApp.gcm || {};
	QuizApp.gcm.Config = QuizApp.gcm.Config || {
		gcmAPIKey: 'AIzaSyAbaM3Sp9DbNO3hDk2PbU7sAdrmwfjTg78'
	};

	QuizApp.app = angular.module('QuizWebApp', ['ngMaterial', 'ngMessages', 'ui.router', 'ngCookies']);
	QuizApp.app.factory('errorInterceptor', ['$q', '$rootScope', '$location', '$injector', function ($q, $rootScope, $location, $injector) {
			
			return {
				request: function (config) {
					return config || $q.when(config);
				},
				requestError: function(request){
					return $q.reject(request);
				},
				response: function (response) {
					return response || $q.when(response);
				},
				responseError: function (response) {
					var $state = $injector.get('$state');

					if (response && response.status === 403) {
						$state.go("login");
					}
					if (response && response.status === 404) {
					}
					if (response && response.status >= 500) {
					}
					return $q.reject(response);
				}
			};
	}]);

	QuizApp.app.config(['$httpProvider', function ($httpProvider) {
		$httpProvider.interceptors.push('errorInterceptor');    
	}]);
	QuizApp.app.config(function($interpolateProvider) {
		$interpolateProvider.startSymbol('{[{');
		$interpolateProvider.endSymbol('}]}');
	});

	// initialize
	QuizApp.app.run(function($rootScope, $cookies, $cookieStore) {
		if(new Date() < new Date($cookies.get("expiredDate"))) {
			$rootScope.logined = true;
		} else {
			$rootScope.logined = false;
		}
	});
	
})();