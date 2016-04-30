package org.univ7.webapp.quiz.socket;

import org.springframework.stereotype.Service;
import org.univ7.webapp.quiz.socket.handler.ConnectionDispatcherHandler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServer;

import com.nhncorp.mods.socket.io.SocketIOServer;
import com.nhncorp.mods.socket.io.impl.DefaultSocketIOServer;
import com.nhncorp.mods.socket.io.spring.DefaultEmbeddableVerticle;

@Service
public class QuizRace extends DefaultEmbeddableVerticle {
	private static final int WEB_SOCKET_PORT = 12345;
	private static SocketIOServer io = null;

	@Override
	public void start(Vertx vertx) {
		HttpServer server = vertx.createHttpServer();
		io = new DefaultSocketIOServer(vertx, server);
		io.sockets().onConnection(new ConnectionDispatcherHandler(io));
		server.listen(WEB_SOCKET_PORT);
	}
}
