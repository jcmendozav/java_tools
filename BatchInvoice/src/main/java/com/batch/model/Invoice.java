package com.batch.model;

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

}
