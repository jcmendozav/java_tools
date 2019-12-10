package com.batch.skipPolicy;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

import com.batch.exception.MissingMandadoryInvoiceFields;

public class ImportInvoiceSkipPolicy implements SkipPolicy {

    private static final int MAX_SKIP_COUNT = 2;

	@Override
	public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
		// TODO Auto-generated method stub
		if(t instanceof MissingMandadoryInvoiceFields && skipCount<=MAX_SKIP_COUNT) {
			return true;
		}
		
		return false;
	}

}
