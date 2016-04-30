(function() {
    QuizApp.app.controller('quizBattleCtrl', ['$state', '$scope', '$rootScope', '$http', '$timeout', '$cookies', '$cookieStore', '$mdDialog',
        function($state, $scope, $rootScope, $http, $timeout, $cookies, $cookieStore, $mdDialog) {
            $scope.init = function() {
                QuizReadyRoom.init($state, $timeout, $scope.showRivalStatusCb);
            }.bind(this);

            $scope.data = {
                group1: '정답',
                currentStage: null,
            };

            $scope.showRivalStatusCb = function(rivalStage) {
                console.log("상대방이 " + rivalStage + "번 문제를 풀었습니다");
            };

            $scope.answerCb = function(sMessage, theme) {
                $scope.displayMessage = sMessage;
                $scope.success = theme;
            };

            $scope.submit = function() {
                var userAnswer = $scope.data.group1;
                QuizReadyRoom.checkAnwser(userAnswer, $scope.answerCb);
                var btn = $(document.querySelector("form button")).remove();
            };

            $scope.$on('$stateChangeSuccess', function() {
                var statusBlock = document.querySelector("#rivalStatus .stage");
                if (statusBlock) {
                    var rivalStage = localStorage["rivalStage"];
                    statusBlock.style.marginLeft = (rivalStage * 1 - 1) * 20 + "%";
                    statusBlock.innerHTML = (rivalStage * 1);
                }
            });
        }
    ]);
})();