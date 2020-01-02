package com.batch.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.batch.model.map.PostKeyConf;
import com.batch.model.map.VendorMap;
import com.batch.repository.PostKeyConfRepository;


public class PostKeyConfUpload implements MapUploadUtil {
	
	
	private static final Logger log = LoggerFactory.getLogger(PostKeyConfUpload.class);

	
	PostKeyConfRepository repository; 
	
	Resource resource;
	
	String delimiter;
	String backupPath;
	
	FileUtils fileUtils;
	
	public PostKeyConfUpload(DataSource dataSource, Resource resource, String delimiter, String backupPath ) {
		this.repository = new PostKeyConfRepository(dataSource);
		this.resource=resource;
		this.delimiter=delimiter;
		this.fileUtils= new FileUtils();
		this.backupPath=backupPath;
	}

	@Override
	public int[] create(List items) {
		// TODO Auto-generated method stub
		return repository.create(items);
	}

	@Override
	public int deleteAll() {
		// TODO Auto-generated method stub
		return repository.deleteAll();
	}

	@Override
	public List getElementsFromResource() {
		// TODO Auto-generated method stub
		Path path=null;
		List<PostKeyConf> items = null;
		try {
			path = Paths.get(this.resource.getURI());
			Stream<String> lines = Files.lines(path,Charset.forName("Cp1252"));
			items = lines.skip(1).map(line -> 
				new PostKeyConf(line,this.delimiter)
			 ).collect(Collectors.toList());
			lines.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("ERROR reading file ",e);
		}
		
		log.info("items: {}",items.size());
		return items;
	}

	@Override
	public void upload() {
		// TODO Auto-generated method stub
		log.info("postkey upload start: {}, {}",resource.getFilename(),resource.exists());
		if(this.resource.exists()) {
			try {
				//fileUtils.backUpFile(this.resource.getFile(), this.backupPath);
				this.deleteAll();
				this.create(this.getElementsFromResource());
				//fileUtils.deleteFile(this.resource.getFile());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Error uploading file: ",e);
			}			
		}
		log.info("postkey upload end");


	}

}
