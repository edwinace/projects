package org.univ7.webapp.quiz.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.stereotype.Component;
import org.univ7.webapp.quiz.enums.DateFormat;

@Component
public class DateManager {

	/**
	 * date1이 date2보다 미래인가?
	 * 
	 * @param date1
	 *                yyyy-mm-dd format date
	 * @param date2
	 *                yyyy-mm-dd format date
	 * @return date1이 date2보다 미래이면 true, 과거면 false
	 */
	public boolean isFuture(String date1, String date2, DateFormat format) {
		if (format == DateFormat.LocalDate) {
			return isFuture(parseStringToLocalDate(date1), parseStringToLocalDate(date2));
		}
		return isFuture(parseStringToLocalDateTime(date1), parseStringToLocalDateTime(date2));
	}

	public boolean isFuture(String date, DateFormat format) {
		if (format == DateFormat.LocalDate) {
			return isFuture(parseStringToLocalDate(date), LocalDate.now());
		}
		return isFuture(parseStringToLocalDateTime(date), LocalDateTime.now());
	}

	public String getFutureDate(int amountToAdd) {
		return LocalDate.now().plus(Period.ofDays(amountToAdd)).toString();
	}

	public String getPastDate(int amountToMinus) {
		return getFutureDate(-amountToMinus);
	}

	public String getFutureDateTime(int amountToAdd) {
		return LocalDateTime.now().plus(Period.ofDays(amountToAdd)).toString();
	}

	public String getPastDateTime(int amountToMinus) {
		return getFutureDateTime(-amountToMinus);
	}

	private boolean isFuture(LocalDate date1, LocalDate date2) {
		return date1.isAfter(date2);
	}

	private boolean isFuture(LocalDateTime date1, LocalDateTime date2) {
		return date1.isAfter(date2);
	}

	private LocalDate parseStringToLocalDate(String date) {
		return LocalDate.parse(date);
	}

	private LocalDateTime parseStringToLocalDateTime(String date) {
		return LocalDateTime.parse(date);
	}
}
