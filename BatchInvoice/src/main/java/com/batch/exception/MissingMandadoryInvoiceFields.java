package com.batch.exception;

import com.batch.model.InvoiceDTO;

public class MissingMandadoryInvoiceFields extends Exception {
	InvoiceDTO invoiceDTO;
	public MissingMandadoryInvoiceFields(InvoiceDTO invoiceDTO) {
		// TODO Auto-generated constructor stub
		this.invoiceDTO=invoiceDTO;
	}

}