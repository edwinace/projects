package org.univ7.webapp.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.univ7.webapp.quiz.enums.DateFormat;
import org.univ7.webapp.quiz.util.DateManager;

public class DateManagerTest {

	private DateManager dateManager = new DateManager();
	
	@Test
	public void getFutreDate() {
		String dateA = "2015-05-15";
		String dateB = "2015-04-15";
		
		assertThat(dateManager.isFuture(dateA, dateB, DateFormat.LocalDate), equalTo(true));
		assertThat(dateManager.isFuture(dateB, dateA, DateFormat.LocalDate), equalTo(false));
	}
}
