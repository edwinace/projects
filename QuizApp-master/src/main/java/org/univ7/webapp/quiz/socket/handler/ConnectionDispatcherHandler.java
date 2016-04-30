package org.univ7.webapp.quiz.socket.handler;

import java.util.HashMap;
import java.util.Map;

import org.vertx.java.core.Handler;

import com.nhncorp.mods.socket.io.SocketIOServer;
import com.nhncorp.mods.socket.io.SocketIOSocket;

public class ConnectionDispatcherHandler implements Handler<SocketIOSocket> {
	private static final String DEFAULT_ROOM = "DEFAULT_ROOM";
	private static Map<String, Boolean> readyStatus = new HashMap<>();
	private SocketIOServer io = null;

	public ConnectionDispatcherHandler(SocketIOServer io) {
		this.io = io;
	}

	@Override
	public void handle(final SocketIOSocket socket) {
		socket.join(DEFAULT_ROOM);
		socket.on("exit", new UserExitHandler(readyStatus));
		socket.on("msg", new MessageHandler(io, socket, readyStatus));
		socket.on("checkAnswer", new AnswerCheckHandler(io));
	}
}
