package com.ssh.batch.partitioner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

public class ImportBatchFilePartitioner implements Partitioner{

	
	private static final Logger log = LoggerFactory.getLogger(ImportBatchFilePartitioner.class);

	
    private static final String DEFAULT_KEY_NAME = "fileName";

    private static final String PARTITION_KEY = "partition";


	private String resourcesPath;


	private Resource[] resources;
    
    
	public void setResourcesPath(String resourcesPath) {
		// TODO Auto-generated method stub
		this.resourcesPath=resourcesPath;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		// TODO Auto-generated method stub
		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();   
		log.info("resourcesPath: {}",this.resourcesPath);
		try {
			this.resources = patternResolver.getResources(resourcesPath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			log.error(e1.getLocalizedMessage());;
		}
		log.info("Resources found: {}",this.resources.length);
		
		
        log.info("gridSize: "+gridSize);
        Map<String, ExecutionContext> map = new HashMap<>(gridSize);
        int i = 0, k = 1;
        String filePath,fileName;
        
        for (Resource resource : resources) {
            ExecutionContext context = new ExecutionContext();
            Assert.state(resource.exists(), "Resource does not exist: "
              + resource);
            fileName=resource.getFilename();
            try {
            	filePath= resource.getFile().getAbsolutePath();
				context.putString("filePath", filePath);
				context.putString("fileName", fileName);		
				context.putInt("writerCounter", 0);		
		        log.info("partitioner, filePath: "+context.getString("filePath"));
				
				
			}
            catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//            context.putString("opFileName", "output"+k+++".xml");
            map.put(PARTITION_KEY + i, context);
            i++;
        }
        
        log.info("map: "+map.size());
        return map;
        }

}
