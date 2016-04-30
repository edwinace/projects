package org.univ7.webapp.quiz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
@AllArgsConstructor
public class JsonMessageVo {
	private static final String SUCCESS = "Success";
	
	private String message;
	
	public JsonMessageVo() {
		this.message = SUCCESS;
	}
}
