package com.batch.listener;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.batch.model.InvoiceDTO;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
	
	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private JdbcTemplate jdbcTemplate;

  @Autowired
  public JobCompletionNotificationListener() {
    //this.jdbcTemplate = jdbcTemplate;
  }
	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		if(jobExecution.getStatus()==BatchStatus.COMPLETED) {
			
			log.info("!!! JOB FINISHED");
//			jdbcTemplate
//			.query("SELECT ID, issue_date,issue_time FROM invoice", 
//					(rs,row) -> new InvoiceDTO(rs.getString("ID"),rs.getString(2),rs.getString(3)) )
//			.forEach(invoice -> log.info("Found <" + invoice.getID() + "> in the database."));
		}

	}
	public void setDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
	    this.jdbcTemplate = new JdbcTemplate(dataSource);

	}

}
