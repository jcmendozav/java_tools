package com.batch.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import com.batch.model.InvoiceFile;

@Repository
public class InvoiceFileRepository {
	
	
	private static final Logger log = LoggerFactory.getLogger(InvoiceFileRepository.class);


	private final String INSERT_SQL = invoiceSQL.INSERT_SQL;
	
	private final String UPDATE_SQL = invoiceSQL.UPDATE_SQL;

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private DataSource dataSource;	
	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public InvoiceFileRepository(DataSource dataSource) {
		this.dataSource=dataSource;
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public int create(InvoiceFile invoiceFile) {
		// TODO Auto-generated method stub
		log.info("Creating InvoiceFile: {}",invoiceFile.toString());
		KeyHolder holder = new GeneratedKeyHolder();
		SqlParameterSource parameters = new MapSqlParameterSource()
		.addValue("fileName", invoiceFile.getFileName())
		.addValue("filePath", invoiceFile.getFilePath())
		.addValue("uuid", invoiceFile.getUuid())
		.addValue("jobID", invoiceFile.getJobID())
		.addValue("lines", invoiceFile.getLines())
		.addValue("size", invoiceFile.getSize());
		jdbcTemplate.update(INSERT_SQL, parameters, holder, new String[] { "id" });
		log.debug("holder.getKey(): {} ",holder.getKey());
		invoiceFile.setID(holder.getKey().intValue());
		return holder.getKey().intValue();
	}
	
	public InvoiceFile updateObjCounter(InvoiceFile invoiceFile) {
		// TODO Auto-generated method stub
		log.info("Updating InvoiceFile: {}",invoiceFile.toString());
		SqlParameterSource parameters = new MapSqlParameterSource()
		.addValue("id", invoiceFile.getID())
		.addValue("objCounter", invoiceFile.getObjCounter());
		jdbcTemplate.update(UPDATE_SQL, parameters);
		return invoiceFile;
	}
	

	
	
	
}
