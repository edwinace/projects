package org.univ7.webapp.quiz.socket.handler;

import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;

import com.nhncorp.mods.socket.io.SocketIOServer;

public class AnswerCheckHandler implements Handler<JsonObject> {

	private SocketIOServer io = null;

	public AnswerCheckHandler(SocketIOServer io) {
		this.io = io;
	}

	public void handle(JsonObject event) {
		String sessionId = event.getString("sessionId");
		Boolean isCorrect = answerCheck(event.getString("answer"));
		event.putBoolean("result", isCorrect);
		event.putString("socketId", sessionId);

		io.sockets().emit("checkAnswer", event);

		Integer currentStage = event.getInteger("currentStage");
		if(gameEnd(currentStage)) {
			broadCastGameEnd(sessionId);
			return;
		}
		
		if (isCorrect) {
			broadCastRivalStatus(sessionId, currentStage);
		}
	}

	private boolean gameEnd(Integer currentStage) {
		return currentStage >= 5;
	}

	private void broadCastGameEnd(String winnerSessionId) {
		JsonObject obj = new JsonObject().putString("winnerSessionId", winnerSessionId);
		io.sockets().emit("gameEnd", obj);
	}

	private void broadCastRivalStatus(String sessionId, Integer rivalStage) {
		JsonObject obj = new JsonObject().putString("sessionId", sessionId).putString("rivalStage", rivalStage + "");
		io.sockets().emit("rivalStatus", obj);
	}

	private Boolean answerCheck(String answer) {
		return ("정답".equals(answer)) ? true : false;
	}
}
