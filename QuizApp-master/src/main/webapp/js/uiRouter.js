(function() {
    QuizApp.app.config(function($stateProvider, $urlRouterProvider) {

        //For any unmatched url, send to / (home)
        http: $urlRouterProvider.otherwise("/main")

        $stateProvider
            .state('main', {
                url: "/main",
                templateUrl: "/view/main"
            })
            .state('login', {
                url: "/login",
                templateUrl: "/view/login"
            })
            .state('pushRegister', {
                url: "/push",
                templateUrl: "/api/private/push"
            })
            .state('todayQuiz', {
                url: "/todayQuiz",
                templateUrl: "/api/private/quiz/today"
            })
            .state('quizRace', {
                url: "/quizRace",
                templateUrl: "/api/private/quizRace"
            })
            .state('quizQuestion', {
                url: "/quizRace/:quizNum",
                templateUrl: function($stateParams) {
                    return "/api/private/quizRace/" + $stateParams.quizNum;
                }
            });
    });
})();