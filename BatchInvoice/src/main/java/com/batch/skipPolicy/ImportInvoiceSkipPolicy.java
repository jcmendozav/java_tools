package com.batch.skipPolicy;

import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

import com.batch.exception.DateFormatInvoiceFields;
import com.batch.exception.MissingMandadoryInvoiceFields;
import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;

public class ImportInvoiceSkipPolicy implements SkipPolicy {

	private static final Logger log = LoggerFactory.getLogger(ImportInvoiceSkipPolicy.class);

	private static final int MAX_SKIP_COUNT = 2;

	@Override
	public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
		// TODO Auto-generated method stub

		log.error("skipCount:{},MAX_SKIP_COUNT:{}",skipCount,MAX_SKIP_COUNT);
		if ((t instanceof MissingMandadoryInvoiceFields) && skipCount <= MAX_SKIP_COUNT) {
			InvoiceDTO dto = ((MissingMandadoryInvoiceFields) t).getInvoiceDTO();

			log.error("Skiped due to MissingMandadoryInvoiceFields: {}", dto.toString());

			return true;
		}
		if (t instanceof DateFormatInvoiceFields && skipCount <= MAX_SKIP_COUNT) {
			InvoiceDTO dto = ((DateFormatInvoiceFields) t).getInvoiceDTO();
			log.error("Skiped due to DateTimeParseException: {}", dto.toString());
			return true;
		} else {
			log.trace("Skiped due to generic error: {}", t);
			return false;

		}

	}

}
