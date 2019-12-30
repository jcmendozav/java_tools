package com.batch.listener;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.core.io.Resource;

import com.batch.service.FileUtils;

public class UnzipListener implements StepExecutionListener {

	
	private static final Logger log = LoggerFactory.getLogger(UnzipListener.class);

	
	private Resource[] resources;
	FileUtils fileUtils;
	private String backupPath;
	
	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}
	
	

	public UnzipListener() {
		// TODO Auto-generated constructor stub
		fileUtils = new FileUtils();
	}
	
	public void setResources(Resource[] resources){
		this.resources=resources;
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		log.info("Backup files: {}",resources.length);
		for(Resource r : resources) {
			try {
				fileUtils.backUpFile(r.getFile(), this.backupPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(),e);
			}
		}

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		log.info("Delete files: {}",resources.length);
		for(Resource r : resources) {
			try {
				fileUtils.deleteFile(r.getFile());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(),e);
			}
		}		return ExitStatus.COMPLETED;
	}

}
