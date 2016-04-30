var QuizReadyRoom = {
	sendBtn: $("#sendBtn"),
	socket: io.connect("http://localhost:12345"),
	state: null,
	answerCb: null,
	timeout: null,
	showRivalStatusCb: null,
	init: function($state, $timeout, rivalStatusCb) {
		this.state = $state;
		this.timeout = $timeout;
		this.showRivalStatusCb = rivalStatusCb;
		this.addSocketSendEvent();
		this.addRoomStatusListener();
		this.addBrowserExitEvent();
		this.addRivalStatusListener();
		this.addAnswerCheckListener();
		this.addGameEndListener();
		localStorage["rivalStage"] = "";
	},
	addSocketSendEvent: function() {
		var sendBtn = $("#sendBtn");
		sendBtn.on("click", function() {
			var msg = $("input[name=chat]").val();
			this.socket.emit('msg', {
				type: "ready",
				value: !sendBtn.data("ready")
			});

			this.toggleSendButton(sendBtn);
		}.bind(this));
	},
	addRoomStatusListener: function() {
		this.socket.on('roomStatus', function(msg) {
			if (msg.roomIsFull) {
				console.log("2명의 사용자가 모두 준비 상태가 되었습니다");
				localStorage["rivalStage"] = 1;
				this.state.go('quizQuestion', {
					quizNum: 1
				});
			}
		}.bind(this));
	},
	toggleSendButton: function(button) {
		var userWasReady = button.data("ready");
		if (userWasReady) {
			button.data("ready", false);
			button.css("background-color", "#FAFAFA").css("color", "rgba(0,0,0,0.87)");
			button.text("준비안됨 -> 준비됨 으로 변경");
		} else {
			button.data("ready", true);
			button.css("background-color", "#5145BA").css("color", "#fff");
			button.text("준비됨 -> 준비안됨 으로 변경");
		}
	},
	addBrowserExitEvent: function() {
		window.onbeforeunload = function() {
			this.sendExitSessionId();
			return null;
		}.bind(this);
	},
	sendExitSessionId: function() {
		this.socket.emit("exit", {
			sessionId: this.socket.socket.sessionid
		});
	},
	addRivalStatusListener: function() {
		this.socket.on('rivalStatus', function(msg) {
			if (msg.sessionId != this.socket.socket.sessionid) {
				var stageEle = document.querySelector("#rivalStatus .stage");
				stageEle.style.marginLeft = (msg.rivalStage * 1) * 20 + "%";
				stageEle.innerHTML = (msg.rivalStage * 1 + 1);
				localStorage["rivalStage"]++;
				this.showRivalStatusCb(msg.rivalStage);
			}
		}.bind(this));
	},
	checkAnwser: function(userAnswer, answerCb) {
		var currentStage = location.href.split("quizRace/")[1] * 1;
		this.socket.emit("checkAnswer", {
			answer: userAnswer,
			sessionId: this.socket.socket.sessionid,
			currentStage: currentStage
		});
		this.answerCb = answerCb;
	},
	addAnswerCheckListener: function() {
		this.socket.on('checkAnswer', function(msg) {
			if (msg.sessionId == this.socket.socket.sessionid) {
				if (msg.result == true) {
					var currentStage = location.href.split("quizRace/")[1] * 1;
					this.answerCb("정답!", true);
					this.timeout(function() {
						if(currentStage >= 5) return;
						this.state.go('quizQuestion', {
							quizNum: (currentStage + 1)
						});
					}.bind(this), 1200);
				} else {
					this.scopeCb("오답!, 문자열 '정답'을 선택하세요", false);
					this.timeout(function() {
						this.answerCb("", null);
					}.bind(this), 2000);
				}
			}
		}.bind(this));
	},
	addGameEndListener: function() {
		this.socket.on('gameEnd', function(msg) {
			var stageEle = document.querySelector("#rivalStatus .stage");
			stageEle.style.marginLeft = "0px";
			stageEle.style.width = "100%";

			if (msg.winnerSessionId == this.socket.socket.sessionid) {
				stageEle.innerHTML = "YOU WIN !!";
			} else {
				stageEle.innerHTML = "YOU Lose !!";
				stageEle.style.background = "#FA5153";
			}
		}.bind(this));	
	}
}