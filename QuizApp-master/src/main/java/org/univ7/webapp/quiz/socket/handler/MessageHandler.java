package org.univ7.webapp.quiz.socket.handler;

import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;

import com.nhncorp.mods.socket.io.SocketIOServer;
import com.nhncorp.mods.socket.io.SocketIOSocket;

public class MessageHandler implements Handler<JsonObject> {

	private SocketIOServer io = null;
	private SocketIOSocket socket = null;
	private Map<String, Boolean> readyStatus = null;

	public MessageHandler(SocketIOServer io, SocketIOSocket socket, Map<String, Boolean> readyStatus) {
		this.io = io;
		this.socket = socket;
		this.readyStatus = readyStatus;
	}

	@Override
	public void handle(JsonObject event) {
		String type = event.getString("type");
		if ("ready".equals(type)) {
			readyStatus.put(socket.getId(), event.getBoolean("value"));
		}

		if (isRoomIsFull(readyStatus)) {
			event.putBoolean("roomIsFull", true);
		} else {
			event.putBoolean("roomIsFull", false);
		}
		io.sockets().emit("roomStatus", event);
	}

	private Boolean isRoomIsFull(Map<String, Boolean> readyStatus) {
		Integer count = 0;
		for (String key : readyStatus.keySet()) {
			if (readyStatus.get(key)) {
				count++;
			}
		}
		return (count == 2) ? true : false;
	}
}
