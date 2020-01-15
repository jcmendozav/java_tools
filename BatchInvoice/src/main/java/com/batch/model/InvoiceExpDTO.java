package com.batch.model;

import java.util.Date;
import java.util.Set;

public class InvoiceExpDTO {
	
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
	public void setDocDate(Date docDate) {
		// TODO Auto-generated method stub
		this.docDate=(docDate);
	}

	public Date getDocDate() {
		return docDate;
	}
	
	public void setInvoiceTypeCode(String invoiceTypeCode) {
		this.invoiceTypeCode = invoiceTypeCode;
	}
	public String getInvoiceTypeCode() {
		return invoiceTypeCode;
	}

	public void setPostKey(String postKey) {
		// TODO Auto-generated method stub
		this.postKey=postKey;
	}
	
	public String getPostKey() {
		return postKey;
	}
	
	public void setPostingDate(java.sql.Date date) {
		// TODO Auto-generated method stub
		this.postingDate=date;
	}
	public void setRefNo(String string) {
		// TODO Auto-generated method stub
		this.refNo=string;
	}
	public void setPeriod(String string) {
		// TODO Auto-generated method stub
		this.period=string;
	}
	public void setDocHdr(String string) {
		this.docHdr=string;
	}
	public void setAccount(String string) {
		this.account=string;
	}
	public void setAmntDocCurr(Double amntDocCurr) {
		this.amntDocCurr=amntDocCurr;
	}
	public void setAmntLocalType(String amntLocalType) {
		this.amntLocalType=amntLocalType;
	}
	public void setFileId(int fileId) {
		this.fileId=fileId;
	}
	
	

	public Date getPostingDate() {
		return postingDate;
	}
	public String getRefNo() {
		return refNo;
	}
	public String getPeriod() {
		return period;
	}
	public String getDocHdr() {
		return docHdr;
	}
	public String getAccount() {
		return account;
	}
	public Double getAmntDocCurr() {
		return amntDocCurr;
	}
	public String getAmntLocalType() {
		return amntLocalType;
	}
	public int getFileId() {
		return fileId;
	}

	public Date getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(Date baseDate) {
		this.baseDate = baseDate;
	}
	public String getTxCode() {
		return txCode;
	}
	public void setTxCode(String txCode) {
		this.txCode = txCode;
	}
	public String getCompCode() {
		return compCode;
	}
	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getPaymTrm() {
		return paymTrm;
	}
	public void setPaymTrm(String paymTrm) {
		this.paymTrm = paymTrm;
	}
	public String getCcntr() {
		return ccntr;
	}
	public void setCcntr(String ccntr) {
		this.ccntr = ccntr;
	}

	public String getAssignNo() {
		return assignNo;
	}
	public void setAssignNo(String assignNo) {
		this.assignNo = assignNo;
	}
	public String getItmTxt() {
		return itmTxt;
	}
	public void setItmTxt(String itmTxt) {
		this.itmTxt = itmTxt;
	}

	public void setPhoneDescItem(String phoneDescItem) {
		this.phoneDescItem = phoneDescItem;
	}
	public String getPhoneDescItem() {
		return phoneDescItem;
	}
	
	public void setUniquePhoneItem(String uniquePhoneItem) {
		this.uniquePhoneItem = uniquePhoneItem;
	}
	public String getUniquePhoneItem() {
		return uniquePhoneItem;
	}
	
	
	

	private Date baseDate;
	private String postKey;
	private String txCode;
	private String compCode;
	private String docType;
	private String paymTrm;
	private String ccntr;





	private String assignNo;
	private String itmTxt;
	private Date postingDate;
	private String refNo;
	private String period;
	private String docHdr;
	private String account;
	private Double amntDocCurr;
	private String amntLocalType;
	private int fileId;
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
	private Date docDate;
	private String uniquePhoneItem;
	private String phoneDescItem;

}
