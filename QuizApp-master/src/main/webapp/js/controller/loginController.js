(function(){
	QuizApp.app.controller('loginCtrl', ['$state', '$scope', '$rootScope', '$http', '$timeout', '$cookies', '$cookieStore', function($state, $scope, $rootScope, $http, $timeout, $cookies, $cookieStore){
		$scope.login = function() {
			var data = $scope.loginForm;
			$http.defaults.useXDomain = true;
			$http.post('/view/login', {userId:data.id,password:data.pw})
				.success(function(data, status, headers, config){
					$scope.displayMessage = "로그인 성공! 반갑습니다";
					$scope.success = true;
					$rootScope.logined = true;

					var expireDate = new Date();
					expireDate.setDate(expireDate.getDate() + data.tokenDuration*1);

					var expireStrategy = {"expires": expireDate};

					$cookies.put("token", data.token, expireStrategy);
					$cookies.put("pubKey", data.pubKey, expireStrategy);
					$cookies.put("expiredDate", data.expiredDate, expireStrategy);

					$timeout(function(){
						$state.go("main");
					}, 2000);
				})
				.error(function(data, status, headers, config){
					$scope.displayMessage = data.message;
					$scope.success = false;
				})
				.finally(function(){
					data.pw = "";
					$timeout(function(){
						$scope.displayMessage = "";
					}, 3000);
				});
		};
	}]);	
})();