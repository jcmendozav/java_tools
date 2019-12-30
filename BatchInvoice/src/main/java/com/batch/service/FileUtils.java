package com.batch.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileUtils {

	
	private static final Logger log = LoggerFactory.getLogger(FileUtils.class);
	
	public boolean backUpFile(File fileSource, String newPath) throws IOException {
		boolean backup=false;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        backup=copyFile(fileSource,newPath,timeStamp);
		return backup;
	}

	public boolean copyFile(File fileSource, String newPath, String tag) throws IOException
	{
		boolean copied=false;
        log.debug("Copying: {}",fileSource.getName());

        String oldFileName=fileSource.getName();
        int extIndex = oldFileName.lastIndexOf('.');
        String newFileName = oldFileName.substring(0,extIndex)+
        		"_"+
        		tag+
        		"_"+
        		oldFileName.substring(extIndex);
        
        String newFilePath=newPath+"/"+newFileName;
        
        if(Files.copy(fileSource.toPath()
        		, (new File(newFilePath).toPath())
        		, StandardCopyOption.REPLACE_EXISTING
        				) != null) {
        	copied = true;
        	log.debug("Sucessfull copy of: \n"
        			+ "{} \n"
        			+ "to new path: \n"
        			+ "{}",fileSource.getAbsolutePath(),newFilePath);
        }
        return copied;
	}
	
	public boolean deleteFile(File file) {
		// TODO Auto-generated method stub
		boolean deleted=false;
        if(file.delete()) {
        	log.debug("Sucessfull delete of: {}",file.getAbsolutePath());
        	deleted = true;
        };
        return deleted;
	}
}
