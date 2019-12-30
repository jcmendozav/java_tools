package com.batch.tasklet;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.batch.service.FileUtils;
 
public class FileDeletingTasklet implements Tasklet, InitializingBean, ResourceReloader {
	
	
	private static final Logger log = LoggerFactory.getLogger(FileDeletingTasklet.class);

 
    private Resource[] resources;


	private String locationPattern;


	private FileUtils fileUtils;
 
	public void setLocationPattern(String locationPattern) {
		this.locationPattern = locationPattern;
	}
	public String getLocationPattern() {
		return locationPattern;
	}
	
	
	public FileDeletingTasklet() {
		// TODO Auto-generated constructor stub
		this.fileUtils= new FileUtils();

	}
	
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    	resources = reloadResources(this.locationPattern);
    	log.info("Deleting {} files",resources.length);
        for(Resource r: resources) {
        	fileUtils.deleteFile(r.getFile());
        	
//            File file = r.getFile();
//            boolean deleted = file.delete();
//            if (!deleted) {
//                throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
//            }
        }
        return RepeatStatus.FINISHED;
    }
 
    public void setResources(Resource[] resources) {
        this.resources = resources;
    }
 
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resources, "directory must be set");
    }
}