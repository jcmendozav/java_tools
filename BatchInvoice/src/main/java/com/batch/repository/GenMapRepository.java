package com.batch.repository;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import com.batch.model.InvoiceFile;
import com.batch.model.map.GenModelMap;
import com.batch.model.map.VendorMap;

@Repository
public class GenMapRepository {
	
	
	private static final Logger log = LoggerFactory.getLogger(GenMapRepository.class);


	private  String CREATE = PostKeyConfSQL.CREATE;
	
	private String DELETE_ALL = PostKeyConfSQL.DELETE_ALL;

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private DataSource dataSource;


	private JdbcTemplate simpleJdbc;	
	
	public void setCREATE(String cREATE) {
		CREATE = cREATE;
	}
	
	public void setDELETE_ALL(String dELETE_ALL) {
		DELETE_ALL = dELETE_ALL;
	}


	public GenMapRepository() {
		// TODO Auto-generated constructor stub
	}
	
	public GenMapRepository(DataSource dataSource) {
		this.dataSource=dataSource;
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.simpleJdbc = new JdbcTemplate(dataSource);
	}
	
	public GenMapRepository(DataSource dataSource, String sQL_CREATE, String sQL_DELETE_ALL) {
		// TODO Auto-generated constructor stub
		this.dataSource=dataSource;
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.simpleJdbc = new JdbcTemplate(dataSource);
		setCREATE(sQL_CREATE);
		setDELETE_ALL(sQL_DELETE_ALL);
	}

	public int deleteAll() {
		return simpleJdbc.update(DELETE_ALL);

	}

	public int[] create(List<? extends GenModelMap> items) {
		log.info("Creating generic items: {}, {}",items.size(),items.get(0).getClass().getName());
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(items.toArray());

		return jdbcTemplate.batchUpdate(CREATE, batch);
	}


	

	
	
	
}
