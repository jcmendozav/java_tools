package com.batch.model;

public class InvoiceFile {
	
	public InvoiceFile() {
		// TODO Auto-generated constructor stub
	}
	
	public InvoiceFile(String fileName, String filePath)
	{
		this.fileName=fileName;
		this.filePath=filePath;
	}

	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Long getLines() {
		return lines;
	}
	public void setLines(Long lines) {
		this.lines = lines;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Long getJobID() {
		return jobID;
	}
	public void setJobID(Long long1) {
		this.jobID = long1;
	}
	
	public void setID(int i) {
		ID = i;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setObjCounter(int objCounter) {
		this.objCounter = objCounter;
	}
	
	public int getObjCounter() {
		return objCounter;
	}
	
	
	String fileName;
	String filePath;
	Long lines;
	Long size;
	String uuid;
	Long jobID;
	int ID;
	int objCounter;
	@Override
	public String toString() {
		return "InvoiceFile [fileName=" + fileName + ", filePath=" + filePath + ", lines=" + lines + ", size=" + size
				+ ", uuid=" + uuid + ", jobID=" + jobID + ", ID=" + ID + "]";
	}
	
	
}
