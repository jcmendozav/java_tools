package com.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;

public class InvoiceItemProcessorListener implements ItemProcessListener<InvoiceDTO, Invoice> {

	
	

	
	private static final Logger log = LoggerFactory.getLogger(InvoiceItemProcessorListener.class);

	@Override
	public void beforeProcess(InvoiceDTO item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterProcess(InvoiceDTO item, Invoice result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProcessError(InvoiceDTO item, Exception e) {
		// TODO Auto-generated method stub
		
		log.error("Error for :{}, exc: {}",item,e);
	}

}
