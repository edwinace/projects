(function(){
	QuizApp.app.controller('pushCtrl', ['$state', '$scope', '$rootScope', '$http', function($state, $scope, $rootScope, $http){
		$scope.init = function() {
			QuizApp.PushService.init();
		}
	}]);	
})();