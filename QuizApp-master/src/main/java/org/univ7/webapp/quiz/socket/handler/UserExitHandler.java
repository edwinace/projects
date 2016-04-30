package org.univ7.webapp.quiz.socket.handler;

import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;

public class UserExitHandler implements Handler<JsonObject> {
	private Map<String, Boolean> readyStatus = null;
	
	public UserExitHandler(Map<String, Boolean> readyStatus) {
		this.readyStatus = readyStatus;
	}
	
	@Override
	public void handle(JsonObject event) {
		String sid = event.getString("sessionId");
		readyStatus.remove(sid);
	}
}
