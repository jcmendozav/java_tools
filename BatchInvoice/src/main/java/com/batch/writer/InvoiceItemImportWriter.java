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

public class InvoiceItemImportWriter implements ItemWriter<Invoice> {
	
	
	private static final Logger log = LoggerFactory.getLogger(InvoiceItemImportWriter.class);

	
    private NamedParameterJdbcTemplate jdbcTemplate;

	private StepExecution stepExecution;

	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
    public InvoiceItemImportWriter(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
	@Override
	public void write(List<? extends Invoice> items) throws Exception {
		ExecutionContext executionContext = this.stepExecution.getExecutionContext();
		int fileProcCount=executionContext.getInt("fileProcCounter");
		log.debug(
				"File Info from writer: "
						+ ",fileName:{}"
						+ ",fileProcCounter:{}"
						+ ",items:{}"
						,executionContext.getString("fileName")
						,fileProcCount
						,items.size()
				//+ ", processing item = " + invoiceDTO.toString() 
				);
		
		
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(items.toArray());
		int[] updateCounts = jdbcTemplate.batchUpdate(
				"INSERT INTO invoice"
						+ "("
						+ "custom_serie"
						+ ",issue_date"
						+ ",issue_time"
						+ ",number_serie"
						+ ",ta"
						+ ",pa"
						+ ",lea"
						+ ",party_id"
						+ ",tax_code"
						+ ",invoice_type_code"
						+ ",doc_date"
						+ ",job_execution_id"
						+ ",file_id"
						+ ",currency_code"
						+ ") "
						+ "values "
						+ "("
						+ ":customSerie"
						+ ",:issueDate"
						+ ",:issueTime"
						+ ",:numberSerie"
						+ ",:taxAmount"
						+ ",:payableAmount"
						+ ",:lineExtensionAmount"
						+ ",:partyId"
						+ ",:taxCode"
						+ ",:invoiceTypeCode"
						+ ",:docDate"
						+ ",:jobExecutionID"
						+ ",:fileID"
						+ ",:currencyCode"
						+ ")"				
				, batch);
		log.debug("updateCounts: {}",Arrays.toString(updateCounts));
	}

	
	
	
}
