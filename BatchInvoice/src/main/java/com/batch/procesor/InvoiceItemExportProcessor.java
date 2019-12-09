package com.batch.procesor;

import org.springframework.batch.item.ItemProcessor;

import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;

public class InvoiceItemExportProcessor implements ItemProcessor<Invoice, Invoice> {

	@Override
	public Invoice process(Invoice item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}

}
