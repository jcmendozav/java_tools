package com.batch.procesor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;

@Configuration
public class InvoiceItemProcessor implements ItemProcessor<InvoiceDTO, Invoice>
{

	
	private static final Logger log = LoggerFactory.getLogger(InvoiceItemProcessor.class);

	
	@Value("I2")
	private String taxCodeWithTax;
	
	@Value("I0")
	private String taxCodeWithNoTax;
	
	@Override
	public Invoice process(InvoiceDTO invoiceDTO) throws Exception {
		// TODO Auto-generated method stub
//		String firstName = person.getFirstName().toUpperCase();
//		String lastName = person.getLastName().toUpperCase();
//		Invoice transformedPerson = new Invoice(firstName, lastName);
//		log.info("Converting (" + person + ") into (" + transformedPerson + ")");
//		
		//Invoice tInvoice = new Invoice();
		log.info("Converting  (" + invoiceDTO.toString() + ")");
		
		Invoice invoice = new Invoice();
		invoice.setCurrencyCode(invoiceDTO.getDocumentCurrencyCode());

		
		invoice.setCustomSerie(invoiceDTO.getInvoiceTypeCode()+"*"+invoiceDTO.getID().split("-")[0]);
		invoice.setNumberSerie(invoiceDTO.getID().split("-")[1]);
		invoice.setIssueDate(invoiceDTO.getIssueDate());
		invoice.setIssueTime(invoiceDTO.getIssueTime());
		invoice.setPartyId(invoiceDTO.getAccountingSupplierParty().getParty().getPartyIdentification().getID());
		invoice.setTaxAmount(invoiceDTO.getTaxTotal().getTaxAmount());
		invoice.setPayableAmount(invoiceDTO.getLegalMonetaryTotal().getPayableAmount());
		invoice.setLineExtensionAmount(invoiceDTO.getLegalMonetaryTotal().getLineExtensionAmount());
		invoice.setPayableAmount(invoiceDTO.getLegalMonetaryTotal().getPayableAmount());
		invoice.setTaxCode((invoiceDTO.getTaxTotal().getTaxAmount()!=0?taxCodeWithTax:taxCodeWithNoTax));
		invoice.setInvoiceTypeCode(invoiceDTO.getInvoiceTypeCode());
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SS");  
		Date docDate = formatter.parse(invoiceDTO.getIssueDate()+invoiceDTO.getIssueTime());
		invoice.setDocDate(docDate);
		return invoice;
	}
	

}
