package com.batch.procesor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;

public class InvoiceItemExportProcessor implements ItemProcessor<Invoice, Invoice> {

	
	private static final Logger log = LoggerFactory.getLogger(InvoiceItemExportProcessor.class);

	
	@Override
	public Invoice process(Invoice item) throws Exception {
		// TODO Auto-generated method stub
		log.debug("Found:"+item);
		return item;
	}

}
