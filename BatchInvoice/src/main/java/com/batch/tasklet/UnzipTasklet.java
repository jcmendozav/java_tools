package com.batch.tasklet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

 
public class UnzipTasklet implements Tasklet, InitializingBean {
 
	
	private static final Logger log = LoggerFactory.getLogger(UnzipTasklet.class);

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

	private String resourcesStr;
	

	public UnzipTasklet() {
		// TODO Auto-generated constructor stub
	}
	
	public UnzipTasklet(String newPath, String resourcesStr) {
		// TODO Auto-generated constructor stub
		this.newPath=newPath;
		setResourcesStr(resourcesStr);;
	}
	
	public void setResourcesStr(String resourcesStr) {
		this.resourcesStr = resourcesStr;
		try {
			this.resources = new PathMatchingResourcePatternResolver().getResources(this.resourcesStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    	log.info("Unzipping {} files",resources.length);

        for(Resource r: resources) {
        	
            File fileSource = r.getFile();
//            String timeStamp = new SimpleDateFormat(dateFormat).format(new Date());
            log.debug("unzipping: {}",fileSource.getName());
            
            
            if(unzip(fileSource.getAbsolutePath(), newPath)) {
            	log.debug("Sucessfull unzip of: {}",fileSource.getAbsolutePath());
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
	
	public boolean unzip(String fileZip, String destDirStr) {
//	       String fileZip = "src/main/resources/unzipTest/compressed.zip";
	        File destDir = new File(destDirStr);
	        byte[] buffer = new byte[1024];
	        ZipInputStream zis;
	        boolean result=false;
			try {
				zis = new ZipInputStream(new FileInputStream(fileZip));

	        ZipEntry zipEntry = zis.getNextEntry();
	        while (zipEntry != null) {
	            File newFile = newFile(destDir, zipEntry);
	            FileOutputStream fos = new FileOutputStream(newFile);
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	                fos.write(buffer, 0, len);
	            }
	            fos.close();
	            zipEntry = zis.getNextEntry();

		        }
	        zis.closeEntry();
	        zis.close();	
	        result = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(),e);
			}

			return result;
	        
	        
	}

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
         
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
         
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
         
        return destFile;
    }
}