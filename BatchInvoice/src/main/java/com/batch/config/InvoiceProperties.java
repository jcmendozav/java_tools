package com.batch.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

//@Component
@ConfigurationProperties(prefix = "batch.invoice")
@Validated

public class InvoiceProperties {

	@Override
	public String toString() {
		return "InvoiceProperties [dateFormat=" + dateFormat + ", taxPercent=" + taxPercent + ", importData="
				+ importData + ", exportData=" + exportData + ", map=" + map + "]";
	}

	String dateFormat;
	Double taxPercent;

	public final ImportData importData = new ImportData();
	public final ExportData exportData = new ExportData();
	public final Map map = new Map();

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public ExportData getExportData() {
		return exportData;
	}

	public ImportData getImportData() {
		return importData;
	}

	public Map getMap() {
		return map;
	}

	public static class ImportData {

		@Override
		public String toString() {
			return "ImportData [msisdnRegex=" + msisdnRegex + ", inputResources=" + Arrays.toString(inputResources)
					+ ", inputResourcesStr=" + inputResourcesStr + ", zipInputResources=" + zipInputResources
					+ ", backupPath=" + backupPath + ", inputPath=" + inputPath + ", outputPath=" + outputPath
					+ ", filesToBackupStr=" + filesToBackupStr + ", filesToDeleteStr=" + filesToDeleteStr
					+ ", maxPoolSize=" + maxPoolSize + ", chunk=" + chunk + ", gridSize=" + gridSize + "]";
		}

		public String getMsisdnRegex() {
			return msisdnRegex;
		}

		public void setMsisdnRegex(String msisdnRegex) {
			this.msisdnRegex = msisdnRegex;
		}

		public  String msisdnRegex;

//		@Value("${batch.invoice.import.inputResources}")
		private Resource[] inputResources;

		public Resource[] getInputResources() {
			return inputResources;
		}

		public void setInputResources(Resource[] inputResources) {
			this.inputResources = inputResources;
		}

		public String getInputResourcesStr() {
			return inputResourcesStr;
		}

		public void setInputResourcesStr(String inputResourcesStr) {
			this.inputResourcesStr = inputResourcesStr;
		}

		public String getZipInputResources() {
			return zipInputResources;
		}

		public void setZipInputResources(String zipInputResources) {
			this.zipInputResources = zipInputResources;
		}

		public String getBackupPath() {
			return backupPath;
		}

		public void setBackupPath(String backupPath) {
			this.backupPath = backupPath;
		}

		public String getInputPath() {
			return inputPath;
		}

		public void setInputPath(String inputPath) {
			this.inputPath = inputPath;
		}

		public String getOutputPath() {
			return outputPath;
		}

		public void setOutputPath(String outputPath) {
			this.outputPath = outputPath;
		}

		public String getFilesToBackupStr() {
			return filesToBackupStr;
		}

		public void setFilesToBackupStr(String filesToBackupStr) {
			this.filesToBackupStr = filesToBackupStr;
		}

		public String getFilesToDeleteStr() {
			return filesToDeleteStr;
		}

		public void setFilesToDeleteStr(String filesToDeleteStr) {
			this.filesToDeleteStr = filesToDeleteStr;
		}

		public Integer getMaxPoolSize() {
			return maxPoolSize;
		}

		public void setMaxPoolSize(Integer maxPoolSize) {
			this.maxPoolSize = maxPoolSize;
		}

		public int getChunk() {
			return chunk;
		}

		public void setChunk(int importChunk) {
			this.chunk = importChunk;
		}

		public int getGridSize() {
			return gridSize;
		}

		public void setGridSize(int gridSize) {
			this.gridSize = gridSize;
		}

		// @Value("${batch.invoice.import.inputResources}")
		private String inputResourcesStr;

//		@Value("${batch.invoice.import.zipInputResources}")
		private String zipInputResources;

		// @Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/backup")
//		@Value("${batch.invoice.import.backupPath}")
		private String backupPath;

//		@Value("${batch.invoice.import.inputPath}")
		private String inputPath;

//		@Value("${batch.invoice.import.outputPath}")
		private String outputPath;

//		@Value("${batch.invoice.import.filesToBackup}")
		private String filesToBackupStr;

//		@Value("${batch.invoice.import.filesToDelete}")
		private String filesToDeleteStr;

//		@Value("${batch.invoice.import.parallelFile.int}")
		private Integer maxPoolSize;

//		@Value("${batch.invoice.import.chunk}")
		private int chunk;

//		@Value("${batch.invoice.import.gridSize}")
		private int gridSize;
		
		

	}

	public static class ExportData {

		@Override
		public String toString() {
			return "ExportData [fieldNames=" + fieldNames + ", rowStart=" + rowStart + ", fileTemplatePath="
					+ fileTemplatePath + ", daysAgo=" + daysAgo + ", exportChunk=" + exportChunk + ", exportfilePath="
					+ exportfilePath + ", delimiter=" + delimiter + ", outputPath=" + outputPath + "]";
		}

		private List<String> fieldNames;

//	@Value("${batch.invoice.export.fileTemplate.row.indexStart}")
		private Integer rowStart;

		// @Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/template/invoice_exp_template.xlsx")
//	@Value("${batch.invoice.export.outputFileTemplatePath}")
		private String fileTemplatePath;

//	@Value("${batch.invoice.export.fromDaysAgo}")
		private int daysAgo;

//	@Value("${batch.invoice.export.chunk}")
		private int exportChunk;

//	@Value("${batch.invoice.export.filePath}")
		private String exportfilePath;

//	@Value("${batch.invoice.export.delimiter}")
		private String delimiter;

		private String outputPath;

		public String getOutputPath() {
			return outputPath;
		}

		public void setOutputPath(String outputPath) {
			this.outputPath = outputPath;
		}

		public void setFieldNames(List<String> fieldNames) {
			this.fieldNames = fieldNames;
		}

		public List<String> getFieldNames() {
			return fieldNames;
		}

		public Integer getRowStart() {
			return rowStart;
		}

		public void setRowStart(Integer rowStart) {
			this.rowStart = rowStart;
		}

		public String getFileTemplatePath() {
			return fileTemplatePath;
		}

		public void setFileTemplatePath(String fileTemplatePath) {
			this.fileTemplatePath = fileTemplatePath;
		}

		public int getDaysAgo() {
			return daysAgo;
		}

		public void setDaysAgo(int daysAgo) {
			this.daysAgo = daysAgo;
		}

		public int getExportChunk() {
			return exportChunk;
		}

		public void setExportChunk(int exportChunk) {
			this.exportChunk = exportChunk;
		}

		public String getExportfilePath() {
			return exportfilePath;
		}

		public void setExportfilePath(String exportfilePath) {
			this.exportfilePath = exportfilePath;
		}

		public String getDelimiter() {
			return delimiter;
		}

		public void setDelimiter(String delimiter) {
			this.delimiter = delimiter;
		}
		
		

	}

	public static class Map {
		@Override
		public String toString() {
			return "Map [postKeyConf=" + postKeyConf + ", Vendor=" + Vendor + ", PhoneCcnrt=" + PhoneCcnrt
					+ ", delimiter=" + delimiter + "]";
		}

		public Resource getPostKeyConf() {
			return postKeyConf;
		}

		public void setPostKeyConf(Resource postKeyConf) {
			this.postKeyConf = postKeyConf;
		}

		public Resource getVendor() {
			return Vendor;
		}

		public void setVendor(Resource vendor) {
			Vendor = vendor;
		}

		public Resource getPhoneCcnrt() {
			return PhoneCcnrt;
		}

		public void setPhoneCcnrt(Resource phoneCcnrt) {
			PhoneCcnrt = phoneCcnrt;
		}

		public String getDelimiter() {
			return delimiter;
		}

		public void setDelimiter(String delimiter) {
			this.delimiter = delimiter;
		}

		// @Value("${batch.invoice.map.postKeyConf}")
		private Resource postKeyConf;

//		@Value("${batch.invoice.map.Vendor}")
		private Resource Vendor;

//		@Value("${batch.invoice.map.PhoneCcnrt}")
		private Resource PhoneCcnrt;

//		@Value("${batch.invoice.map.delimiter}")
		private String delimiter;

		
	}
	
	public Double getTaxPercent() {
		return taxPercent;
	}
	
	public void setTaxPercent(Double taxPercent) {
		this.taxPercent = taxPercent;
	}


	

}