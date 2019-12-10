package com.batch.tasklet;

import javax.sql.DataSource;


import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.*;
import org.springframework.beans.factory.InitializingBean;



public class ReplaceTableInfo implements Tasklet, InitializingBean{

	DataSource dataSource;
	
	String fileName;
	
	String sql;
	
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		return;
		
		
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		

	 		
		return RepeatStatus.FINISHED;
	}

}
