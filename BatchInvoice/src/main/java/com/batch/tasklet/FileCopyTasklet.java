package com.batch.tasklet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
 
public class FileCopyTasklet implements Tasklet, InitializingBean {
 
	
	private static final Logger log = LoggerFactory.getLogger(FileCopyTasklet.class);

    private Resource[] resources;
    

	//@Value("backup")
	private String newPath;
	
	public String getNewPath() {
		return newPath;
	}

	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	private String ext;

	private String dateFormat;
	

 
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
         
        for(Resource r: resources) {
        	
            File file = r.getFile();
            String timeStamp = new SimpleDateFormat(dateFormat).format(new Date());
            String newFileName=file.getName()+"_"+timeStamp+ext;
            log.debug("Copying:"+file.getPath());
            System.out.println("Copying:"+file.getPath());
            if(Files.copy(file.toPath()
            		, (new File(newPath+"/"+newFileName).toPath())
            		, StandardCopyOption.REPLACE_EXISTING
            				) != null) {
            	log.debug("Sucessfull copy of:"+file.getAbsolutePath());
            }
  
        }
        return RepeatStatus.FINISHED;
    }
 
    public void setResources(Resource[] resources) {
        this.resources = resources;
    }
 
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resources, "directory must be set");
    }

	public void setDateFormat(String dateFormat) {
		// TODO Auto-generated method stub
		this.dateFormat=dateFormat;
		
	}
}