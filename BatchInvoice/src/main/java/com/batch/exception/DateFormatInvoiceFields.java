package com.batch.exception;

import java.time.format.DateTimeFormatter;

import com.batch.model.InvoiceDTO;

public class DateFormatInvoiceFields extends Exception {
	InvoiceDTO invoiceDTO;
	DateTimeFormatter dtf;
	
	public InvoiceDTO getInvoiceDTO() {
		return invoiceDTO;
	}
	public DateFormatInvoiceFields(InvoiceDTO invoiceDTO, DateTimeFormatter dtf) {
		// TODO Auto-generated constructor stub
		this.invoiceDTO=invoiceDTO;
		this.dtf=dtf;
	}
	
	@Override
	public String getMessage() {
		return String.format("Invalid date format: date format expected: %s, input: %s",this.dtf.toFormat().toString(), this.invoiceDTO);
		
	}

}
