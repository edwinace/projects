package org.univ7.webapp.quiz.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JSONResponseUtil {
	public static ResponseEntity<Object> getJSONResponse(Object obj, HttpStatus status) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<Object>(obj, responseHeaders, status);
	}
}
