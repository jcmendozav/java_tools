package com.batch.tasklet;

import java.io.IOException;


import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public interface ResourceReloader {
	
	public default  Resource[] reloadResources(String locationPattern) {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
		Resource[] result=null;
				try {
					result= (resolver.getResources(locationPattern));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				return result;
    }

}
