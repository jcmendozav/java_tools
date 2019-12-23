package com.batch.exception;

import com.batch.model.InvoiceDTO;

public class MissingMandadoryInvoiceFields extends Exception {
	InvoiceDTO invoiceDTO;
	
	public InvoiceDTO getInvoiceDTO() {
		return invoiceDTO;
	}
	public MissingMandadoryInvoiceFields(InvoiceDTO invoiceDTO) {
		// TODO Auto-generated constructor stub
		this.invoiceDTO=invoiceDTO;
	}
	@Override
	public String getMessage() {
		return String.format("Missing Mandadory Invoice Fields: input: %s", this.invoiceDTO.toString());
		
	}
}
