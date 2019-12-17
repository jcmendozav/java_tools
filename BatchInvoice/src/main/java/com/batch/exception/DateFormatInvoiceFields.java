package com.batch.exception;

import com.batch.model.InvoiceDTO;

public class DateFormatInvoiceFields extends Exception {
	InvoiceDTO invoiceDTO;
	
	public InvoiceDTO getInvoiceDTO() {
		return invoiceDTO;
	}
	public DateFormatInvoiceFields(InvoiceDTO invoiceDTO) {
		// TODO Auto-generated constructor stub
		this.invoiceDTO=invoiceDTO;
	}

}
