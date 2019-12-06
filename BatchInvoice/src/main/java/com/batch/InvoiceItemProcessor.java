package com.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.batch.model.InvoiceDTO;

public class InvoiceItemProcessor implements ItemProcessor<InvoiceDTO, InvoiceDTO>
{

	
	private static final Logger log = LoggerFactory.getLogger(InvoiceItemProcessor.class);

	@Override
	public InvoiceDTO process(InvoiceDTO invoice) throws Exception {
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
