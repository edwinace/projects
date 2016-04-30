package org.univ7.webapp.quiz.constant;

public class UriResources {
	public static class Main {
		public static final String LAYOUT = "/";
	}
	public static class View {
		public static final String MAIN_VIEW = "/view/main";
		public static final String LOGIN = "/view/login";
	}
	public static class Api {
		public static final String PUSH_MESSAGE_DATA = "/api/push/data";
	}
	// /api/private/**
	public static class PrivateApi {
		public static final String PUSH_REGISTER = "/api/private/push";
		public static final String PUSH_SEND_TEST = "/api/private/push/forceSend";
		
		public static final String TODAY_QUIZ = "/api/private/quiz/today";
		
		public static final String QUIZ_RACE = "/api/private/quizRace";
	}
}
