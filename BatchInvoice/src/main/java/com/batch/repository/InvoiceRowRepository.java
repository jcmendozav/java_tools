package com.batch.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.batch.model.Invoice;

@Repository
public class InvoiceRowRepository {

	String INSERT_SQL=InvoiceSQL.CREATE_INVOICE_ROW;
	private DataSource dataSource;
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public InvoiceRowRepository(DataSource dataSource) {
		// TODO Auto-generated constructor stub
		this.dataSource=dataSource;
		this.jdbcTemplate =  new NamedParameterJdbcTemplate(dataSource);
	}
	
	public int[] create(List<? extends Invoice> items) {
		// TODO Auto-generated method stub
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(items.toArray());
		int[] updateCounts = jdbcTemplate.batchUpdate(
				INSERT_SQL				
				, batch);
		return updateCounts;
	}

}
