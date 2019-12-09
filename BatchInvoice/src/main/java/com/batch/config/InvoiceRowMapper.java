package com.batch.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;

public class InvoiceRowMapper implements RowMapper<Invoice> {

	
	private static final Logger log = LoggerFactory.getLogger(InvoiceRowMapper.class);

	@Override
	public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		
		log.debug("Found: rowNum:"+rowNum+",ID:"+rs.getString("ID"));
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
