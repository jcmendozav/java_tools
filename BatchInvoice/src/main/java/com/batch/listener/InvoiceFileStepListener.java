package com.batch.listener;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.batch.model.InvoiceFile;
import com.batch.repository.InvoiceFileRepository;

public class InvoiceFileStepListener implements StepExecutionListener {

	DataSource dataSource;
	NamedParameterJdbcTemplate jdbcTemplate;
	List<InvoiceFile> items;
	InvoiceFileRepository repository;
	private ExecutionContext executionContext;
	
	
	public void setItems(List<InvoiceFile> items) {
		this.items = items;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new NamedParameterJdbcTemplate( dataSource);
		//this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		this.repository=new InvoiceFileRepository(jdbcTemplate);
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		
		executionContext=stepExecution.getExecutionContext();
		String filePath=executionContext.getString("filePath");
    	String uuid=UUID.randomUUID().toString();
    	executionContext.putString("uuid", uuid);	
    	executionContext.putLong("jobID", stepExecution.getJobExecution().getId());	
    	InvoiceFile invoiceFile = new InvoiceFile();
    	Resource resource = new FileSystemResource(filePath);
    	int fileID=0;
    	try {
    		
    		invoiceFile.setUuid(uuid);
    		invoiceFile.setFilePath(filePath);
    		invoiceFile.setSize(resource.getFile().length());
    		invoiceFile.setFileName(resource.getFilename());
    		invoiceFile.setLines(Files.lines(resource.getFile().toPath()).count());
    		invoiceFile.setJobID(stepExecution.getJobExecution().getId());
    		fileID=repository.create(invoiceFile);
        	executionContext.putInt("fileID", fileID);	
        	executionContext.put("invoiceFile", invoiceFile);	

    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("invoiceFile fileID: "+fileID);
	}



	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		
		
		int fileProcCount=stepExecution.getExecutionContext().getInt("fileProcCounter");
		InvoiceFile invoiceFile = (InvoiceFile) executionContext.get("invoiceFile"); 
    	invoiceFile.setObjCounter(fileProcCount);
    	repository.updateObjCounter(invoiceFile);
		return ExitStatus.COMPLETED;
	}

}
