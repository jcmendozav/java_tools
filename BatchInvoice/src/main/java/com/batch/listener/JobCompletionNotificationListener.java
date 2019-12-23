package com.batch.listener;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.support.SimpleJvmExitCodeMapper;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ExecutorConfigurationSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.batch.model.InvoiceDTO;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
	
	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private JdbcTemplate jdbcTemplate;

	//private ExecutorConfigurationSupport taskExecutor;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		super.beforeJob(jobExecution);
	}

  @Autowired
  public JobCompletionNotificationListener() {
    //this.jdbcTemplate = jdbcTemplate;
	  //this.taskExecutor=(ExecutorConfigurationSupport) taskExecutor2;
  }
	@Override
	public void afterJob(JobExecution jobExecution) {
		SimpleJvmExitCodeMapper mapper = new SimpleJvmExitCodeMapper();

		// TODO Auto-generated method stub
		if(jobExecution.getStatus()==BatchStatus.COMPLETED ) {
			
			log.info("!!! JOB FINISHED: "
					+ ",job_exec_id: {}"
					+ ",job_id: {}"
					,+jobExecution.getId()
					,jobExecution.getJobId()
					);
//			jdbcTemplate
//			.query("SELECT ID, issue_date,issue_time FROM invoice", 
//					(rs,row) -> new InvoiceDTO(rs.getString("ID"),rs.getString(2),rs.getString(3)) )
//			.forEach(invoice -> log.info("Found <" + invoice.getID() + "> in the database."));
	        //taskExecutor.shutdown();
		}

		String status = jobExecution.getExitStatus().getExitCode();
		Integer returnCode = mapper.intValue(status);
		System.exit(returnCode);

	}
	public void setDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
	    this.jdbcTemplate = new JdbcTemplate(dataSource);

	}

}
