package com.batch.model.map;

public class PostKeyConf {
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
	
	public PostKeyConf(String[] fields) {
		// TODO Auto-generated constructor stub
		init(fields);

	}
	
	

	public PostKeyConf(String line, String delimiter) {
		// TODO Auto-generated constructor stub
		
		String[] fields = line.split(delimiter);
		init(fields);
	}
	
	public void init(String[] fields){

		this.amntLocalType=fields[0];
		this.docTypeCode=fields[1];
		this.postKey=fields[2];
		
	}
	
}
