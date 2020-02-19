package com.ssh.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.support.SimpleJvmExitCodeMapper;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobCompletionNotificationListener  extends JobExecutionListenerSupport{

	
	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	
	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		SimpleJvmExitCodeMapper mapper = new SimpleJvmExitCodeMapper();

		if(jobExecution.getStatus()==BatchStatus.COMPLETED ) {
			
			log.info("!!! JOB FINISHED");
		}

		String status = jobExecution.getExitStatus().getExitCode();
		Integer returnCode = mapper.intValue(status);
		//System.exit(returnCode);
	}
}
