(function(){
	QuizApp.app.controller('sideNavCtrl', ['$scope', '$mdSidenav', function($scope, $mdSidenav){
		$scope.closeSideNav = function(){
			$mdSidenav("left").close();
		};
	}]);	
})();

