package com.batch.writer;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.batch.model.Invoice;
import com.batch.model.InvoiceFile;
import com.batch.repository.InvoiceFileRepository;
import com.batch.repository.InvoiceRowRepository;

public class InvoiceItemImportWriter implements ItemWriter<Invoice> {
	
	
	private static final Logger log = LoggerFactory.getLogger(InvoiceItemImportWriter.class);

	
    private NamedParameterJdbcTemplate jdbcTemplate;

	private StepExecution stepExecution;
	
	private InvoiceRowRepository repository;

	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
    public InvoiceItemImportWriter(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.repository= new InvoiceRowRepository(dataSource);
    }
	@Override
	public void write(List<? extends Invoice> items) throws Exception {
		ExecutionContext executionContext = this.stepExecution.getExecutionContext();

		log.info("Wrinting invoice rows: {}",Arrays.toString(items.toArray()));
		
		int[] updateCounts = repository.create(items);
		log.info("updateCounts: {}",Arrays.toString(updateCounts));
	}

	
	
	
}
