package com.batch.model.map;

import java.util.regex.Pattern;

public class PostKeyConf implements GenModelMap {
	public String getAmntLocalType() {
		return amntLocalType;
	}

	public void setAmntLocalType(String amntLocalType) {
		this.amntLocalType = amntLocalType;
	}

	public String getDocTypeCode() {
		return docTypeCode;
	}

	public void setDocTypeCode(String docTypeCode) {
		this.docTypeCode = docTypeCode;
	}

	public String getPostKey() {
		return postKey;
	}

	public void setPostKey(String postKey) {
		this.postKey = postKey;
	}

	String amntLocalType;
	String docTypeCode;
	String postKey;
	
	
	@Override
	public void init(String line, String delimiter) {
		// TODO Auto-generated constructor stub
		
		String[] fields = line.split(Pattern.quote(delimiter),-1);
		init(fields);
	}
	
	@Override
	public void init(String[] fields){

		this.amntLocalType=fields[0];
		this.docTypeCode=fields[1];
		this.postKey=fields[2];
		
	}
	
}
