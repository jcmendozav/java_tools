package com.batch.tasklet;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

public class uploadFileInfo implements Tasklet, InitializingBean{

	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		return RepeatStatus.FINISHED;
	}

}
