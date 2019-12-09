package com.batch.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;

public class InvoiceRowMapper implements RowMapper<Invoice> {

	@Override
	public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Invoice invoice = new Invoice();
		
		invoice.setCurrencyCode(rs.getString("currency_code"));
		invoice.setCustomSerie(rs.getString("custom_serie"));
		invoice.setNumberSerie(rs.getString("number_serie"));
		invoice.setLineExtensionAmount(rs.getDouble("lea"));
		invoice.setTaxAmount(rs.getDouble("ta"));
		invoice.setPayableAmount(rs.getDouble("pa"));
		
		
		return invoice ;
	}

}
