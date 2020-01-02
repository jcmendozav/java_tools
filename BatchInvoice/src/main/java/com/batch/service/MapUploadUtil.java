package com.batch.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;

public interface MapUploadUtil {
	
	public int[] create(List list);
	public int deleteAll();
	public List getElementsFromResource();

	public  void upload();

}
