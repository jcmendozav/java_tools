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
import com.batch.model.map.VendorMap;

@Repository
public class PostKeyConfRepository {
	
	
	private static final Logger log = LoggerFactory.getLogger(PostKeyConfRepository.class);


	private final String CREATE = PostKeyConfSQL.CREATE;
	
	private final String DELETE_ALL = PostKeyConfSQL.DELETE_ALL;

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private DataSource dataSource;


	private JdbcTemplate simpleJdbc;	

	
	public PostKeyConfRepository(DataSource dataSource) {
		this.dataSource=dataSource;
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.simpleJdbc = new JdbcTemplate(dataSource);
	}
	
	public int deleteAll() {
		return simpleJdbc.update(DELETE_ALL);

	}

	public int[] create(List<? extends VendorMap> items) {
		log.info("Creating post key conf: {}",items.toString());
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(items.toArray());

		return jdbcTemplate.batchUpdate(CREATE, batch);
	}


	

	
	
	
}
