package com.batch.model;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

public class Invoice {
	
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getCustomSerie() {
		return customSerie;
	}
	public void setCustomSerie(String customSerie) {
		this.customSerie = customSerie;
	}
	public String getNumberSerie() {
		return numberSerie;
	}
	public void setNumberSerie(String numberSerie) {
		this.numberSerie = numberSerie;
	}
	public String getIssueTimeStamp() {
		return issueTimeStamp;
	}
	public void setIssueTimeStamp(String issueTimeStamp) {
		this.issueTimeStamp = issueTimeStamp;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public Double getLineExtensionAmount() {
		return lineExtensionAmount;
	}
	public void setLineExtensionAmount(Double lineExtensionAmount) {
		this.lineExtensionAmount = lineExtensionAmount;
	}
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public Double getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}
	
	public String getIssueDate() {
		return issueDate;
	}
	
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	
	public String getIssueTime() {
		return issueTime;
	}
	
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}
	
	public String getTaxCode() {
		return taxCode;
	}
	
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public void setDocDate(LocalDateTime docDate) {
		// TODO Auto-generated method stub
		this.docDate=(docDate);
	}

	public LocalDateTime getDocDate() {
		return docDate;
	}
	
	public void setInvoiceTypeCode(String invoiceTypeCode) {
		this.invoiceTypeCode = invoiceTypeCode;
	}
	public String getInvoiceTypeCode() {
		return invoiceTypeCode;
	}
	
	public void setJobExecutionID(Long long1) {
		this.jobExecutionID = long1;
	}
	public Long getJobExecutionID() {
		return jobExecutionID;
	}
	
	public void setFileID(int fileID) {
		this.fileID = fileID;
	}
	
	public int getFileID() {
		return fileID;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFilePath() {
		return filePath;
	}
	
	public void setProcDesc(String procDesc) {
		this.procDesc = procDesc;
	}
	
	public String getProcDesc() {
		return procDesc;
	}
	
	public void setProcStatus(int procStatus) {
		this.procStatus = procStatus;
	}
	
	public int getProcStatus() {
		return procStatus;
	}
	
	public void setId(int id) {
		// TODO Auto-generated method stub
		this.ID=id;
	}
	public void setInvoiceId(String invoiceId) {
		// TODO Auto-generated method stub
		this.invoiceId=invoiceId;
	}
	public void setStatus(int status) {
		// TODO Auto-generated method stub
		this.status=status;
	}
	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		// TODO Auto-generated method stub
		this.lastUpdatedDate=lastUpdatedDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public int getStatus() {
		return status;
	}
	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	String partyId;
	String vendorId;
	String customSerie;
	String numberSerie;
	String issueTimeStamp;
	String issueTime;
	String issueDate;
	String currencyCode;
	Double lineExtensionAmount;
	Double taxAmount;
	Double payableAmount;
	String taxCode;
	String invoiceTypeCode;
	private LocalDateTime docDate;
	Long jobExecutionID;
	int fileID;
	String fileName;
	String filePath;
	int procStatus;
	String procDesc;
	private int ID;
	private String invoiceId;
	private int status;
	private LocalDateTime lastUpdatedDate;
	private LocalDateTime creationDate;



}
