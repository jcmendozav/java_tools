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

import com.batch.model.map.GenModelMap;
import com.batch.repository.GenMapRepository;


public class GenMapUpload<T extends GenModelMap> implements MapUploadUtil  {
	
    private Class<T> genModelMap;

	private static final Logger log = LoggerFactory.getLogger(GenMapUpload.class);

	
	GenMapRepository repository; 
	
	Resource resource;
	
	String delimiter;
	String backupPath;
	
	FileUtils fileUtils;
	
	public GenMapUpload(DataSource dataSource
			,Class<T> genModelMap
			, Resource resource
			, String delimiter
			, String backupPath
			, String SQL_CREATE
			, String SQL_DELETE_ALL ) {
		this.repository = new GenMapRepository(dataSource,SQL_CREATE,SQL_DELETE_ALL);
		this.resource=resource;
		this.delimiter=delimiter;
		this.fileUtils= new FileUtils();
		this.backupPath=backupPath;
		this.genModelMap=genModelMap;
		
		log.info("Class type: {}",genModelMap.getClass().getName());
	}

	public int[] create(List items) {
		// TODO Auto-generated method stub
		return repository.create(items);
	}

	public int deleteAll() {
		// TODO Auto-generated method stub
		return repository.deleteAll();
	}

	public List<T> getElementsFromResource() {
		// TODO Auto-generated method stub
		Path path=null;
		List<T> items = null;
		try {
			path = Paths.get(this.resource.getURI());
			Stream<String> lines = Files.lines(path,Charset.forName("Cp1252"));
			items = lines.skip(1).map(line -> {
				T t=null;
				try {
					t = genModelMap.newInstance();
					t.init(line,delimiter);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("Problem procesing line: {}, ERROR: {}",line,e);
				}
				return t;
			}
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
		log.info("Generic upload start: {}, {}",resource.getFilename(),resource.exists());
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
		log.info("Generic Upload end, {}",genModelMap.getClass().getName());


	}

}
