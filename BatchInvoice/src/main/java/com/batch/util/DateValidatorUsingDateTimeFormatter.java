package com.batch.util;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateValidatorUsingDateTimeFormatter implements DateValidator {
	
	
	private static final Logger log = LoggerFactory.getLogger(DateValidatorUsingDateTimeFormatter.class);


	DateTimeFormatter dateFormatter;


	public DateValidatorUsingDateTimeFormatter(DateTimeFormatter dateFormatter) {
		this.dateFormatter = dateFormatter;
	}

	@Override
	public boolean isValid(String dateStr) {
		try {
			this.dateFormatter.parse(dateStr);
		} catch (DateTimeParseException e) {
			log.error("Error with dateFormatter: {}",dateFormatter.toString());
			log.error(e.getMessage(),e);
			return false;
		}
		return true;
	}
}