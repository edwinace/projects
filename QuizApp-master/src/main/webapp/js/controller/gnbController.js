(function(){
	QuizApp.app.controller('gnbCtrl', ['$scope', '$mdSidenav', function($scope, $mdSidenav){
		$scope.toggleSidenav = function() {
			$mdSidenav("left").toggle();
		};
	}]);	
})();