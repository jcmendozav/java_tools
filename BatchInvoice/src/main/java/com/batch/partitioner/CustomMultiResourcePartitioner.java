package com.batch.partitioner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import com.batch.model.InvoiceFile;
import com.batch.tasklet.ResourceReloader;

import java.nio.file.Files;
import java.util.UUID;
public class CustomMultiResourcePartitioner implements Partitioner,ResourceReloader {
	
	
	private static final Logger log = LoggerFactory.getLogger(CustomMultiResourcePartitioner.class);

	  
    private static final String DEFAULT_KEY_NAME = "fileName";

    private static final String PARTITION_KEY = "partition";

    private Resource[] resources = new Resource[0];

    private String keyName = DEFAULT_KEY_NAME;


	private String locationPattern;
	
	public CustomMultiResourcePartitioner(Resource[] resources) {
		this.resources=resources;
	}
	
	
	public CustomMultiResourcePartitioner(String locationPattern) {
		this.locationPattern=locationPattern;
	}
	
    
    public void setResources(Resource[] resources) {
		this.resources = resources;
	}
    
    public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
    
	@Override
    public Map<String, ExecutionContext> partition(int gridSize) {
		resources=reloadResources(this.locationPattern);
        log.info("gridSize: {}",gridSize);
        log.info("resources: {}",Arrays.toString(resources));
        Map<String, ExecutionContext> map = new HashMap<>(gridSize);
        int i = 0, k = 1;
        String filePath,fileID,fileName;
        for (Resource resource : resources) {
            ExecutionContext context = new ExecutionContext();
            Assert.state(resource.exists(), "Resource does not exist: "
              + resource);
            fileName=resource.getFilename();
            context.putString(keyName, resource.getFilename());
            try {
            	filePath= resource.getFile().getAbsolutePath();
            	fileID=UUID.randomUUID().toString();

				context.putString("filePath", filePath);
				context.putString("fileName", fileName);		
				context.putString("UUID", fileID);				
				context.putInt("fileProcCounter", 0);		
		        log.info("filePath: {}",context.getString("filePath"));

				
				
			}
            catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(),e);
			}
            context.putString("opFileName", "output"+k+++".xml");
            map.put(PARTITION_KEY + i, context);
            i++;
        }
        
        log.info("map: {}",map.size());
        return map;
    }

}