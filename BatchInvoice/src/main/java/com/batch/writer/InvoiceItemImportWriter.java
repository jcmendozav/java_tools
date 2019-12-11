package com.batch.writer;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.batch.model.Invoice;

public class InvoiceItemImportWriter implements ItemWriter<Invoice> {
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
		// TODO Auto-generated method stub
		ExecutionContext executionContext = this.stepExecution.getExecutionContext();
		int fileProcCount=executionContext.getInt("fileProcCounter");
		String fileID=executionContext.getString("UUID");
		System.out.println(
				"File Info from writer: "
						+ ",fileName:"+executionContext.getString("fileName")+""
						+ ",UUID:"+fileID+""
						+ ",fileProcCounter:"+fileProcCount+""
						//+ ",filePath:"+executionContext.getString("filePath")+""
						+ ",fileLength:"+executionContext.getLong("fileLength")+""
						+ ",fileLines:"+executionContext.getLong("fileLines")+""
						+ ",items:"+items.size()+""
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
						+ ")"				
				, batch);
		System.out.println("updateCounts: "+updateCounts);
	}

	
	
	
}
