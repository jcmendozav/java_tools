package com.ssh.batch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "ssh.batch")
@Component

public class BatchAppProperties {

	@Override
	public String toString() {
		return "BatchAppProperties [chunk=" + chunk + ", inputPattern=" + inputPattern + ", delimiter=" + delimiter
				+ ", fieldNamesInput=" + fieldNamesInput + ", dateFormat=" + dateFormat + ", fieldNamesResult="
				+ fieldNamesResult + ", exportFileFormat=" + exportFileFormat + ", exportHeader=" + exportHeader
				+ ", threadPoolSize=" + threadPoolSize + ", gridSize=" + gridSize + ", newLineDelimiter="
				+ newLineDelimiter + "]";
	}
	public int getChunk() {
		return chunk;
	}
	public void setChunk(int chunk) {
		this.chunk = chunk;
	}
	public String getInputPattern() {
		return inputPattern;
	}
	public void setInputPattern(String inputPattern) {
		this.inputPattern = inputPattern;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String getFieldNamesInput() {
		return fieldNamesInput;
	}
	public void setFieldNamesInput(String fieldNamesInput) {
		this.fieldNamesInput = fieldNamesInput;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getFieldNamesResult() {
		return fieldNamesResult;
	}
	public void setFieldNamesResult(String fieldNamesResult) {
		this.fieldNamesResult = fieldNamesResult;
	}
	public String getExportFileFormat() {
		return exportFileFormat;
	}
	public void setExportFileFormat(String exportFileFormat) {
		this.exportFileFormat = exportFileFormat;
	}
	public String getExportHeader() {
		return exportHeader;
	}
	public void setExportHeader(String exportHeader) {
		this.exportHeader = exportHeader;
	}
	public int getThreadPoolSize() {
		return threadPoolSize;
	}
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	public int getGridSize() {
		return gridSize;
	}
	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}
	
	public void setNewLineDelimiter(String newLineDelimiter) {
		this.newLineDelimiter = newLineDelimiter;
	}
	
	public String getNewLineDelimiter() {
		return newLineDelimiter;
	}
	
	
	


	public int chunk;
	
    @NonNull
	public String inputPattern;
    @NonNull
	public String delimiter;
	public String fieldNamesInput;
	public String dateFormat;
	public String fieldNamesResult;
	public String exportFileFormat;
	public String exportHeader;
	public int threadPoolSize;
	public int gridSize;
	public String newLineDelimiter;
	
	
	

}
