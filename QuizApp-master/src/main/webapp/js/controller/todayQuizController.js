(function(){
	QuizApp.app.controller('todayQuizCtrl', ['$state', '$scope', '$rootScope', '$http', '$timeout', '$cookies', '$cookieStore', '$mdDialog', function($state, $scope, $rootScope, $http, $timeout, $cookies, $cookieStore, $mdDialog){
		
	}]);
	QuizApp.app.controller('todayQuizFormCtrl', ['$state', '$scope', '$rootScope', '$http', '$timeout', '$cookies', '$cookieStore', '$mdDialog', function($state, $scope, $rootScope, $http, $timeout, $cookies, $cookieStore, $mdDialog){
		$scope.data = {
			group1 : 'Banana',
		};
		
		$scope.submit = function() {
			$.post("/api/private/quiz/today", {answer : $scope.data.group1}, function(data){
				console.log(data.message);
				if(data.message == "true") {
					alert("정답");
				} else {
					alert("땡");
				}
			});
		};
	}]);
})();


// (function(){
// 	QuizApp.app.controller('todayQuizCtrl', ['$state', '$scope', '$rootScope', '$http', function($state, $scope, $rootScope, $http){
// 		$scope.submit = function() {
// 		  alert('submit');
// 		};
// 	}]);
// })();