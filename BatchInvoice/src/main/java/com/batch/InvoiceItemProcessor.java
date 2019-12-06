package com.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class InvoiceItemProcessor implements ItemProcessor<Invoice, Invoice>
{

	
	private static final Logger log = LoggerFactory.getLogger(InvoiceItemProcessor.class);

	@Override
	public Invoice process(Invoice invoice) throws Exception {
		// TODO Auto-generated method stub
//		String firstName = person.getFirstName().toUpperCase();
//		String lastName = person.getLastName().toUpperCase();
//		Invoice transformedPerson = new Invoice(firstName, lastName);
//		log.info("Converting (" + person + ") into (" + transformedPerson + ")");
//		
		//Invoice tInvoice = new Invoice();
		log.info("Converting  (" + invoice.toString() + ")");
		return invoice;
	}
	

}
