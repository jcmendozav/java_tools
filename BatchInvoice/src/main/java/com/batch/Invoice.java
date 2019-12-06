package com.batch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Invoice",namespace="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2")
//@XmlRootElement(name="Invoice")

public class Invoice {

	
	public String getID() {
		return ID;
	}
	@XmlElement(name="ID", namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setID(String iD) {
		ID = iD;
	}
	public String getIssueDate() {
		return issueDate;
	}
    @XmlElement(name="IssueDate",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getIssueTime() {
		return issueTime;
	}
    @XmlElement(name="IssueTime",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	
	public Invoice() {
		// TODO Auto-generated constructor stub
	}

    public String getInvoiceTypeCode() {
		return invoiceTypeCode;
	}
    @XmlElement(name="InvoiceTypeCode",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setInvoiceTypeCode(String invoiceTypeCode) {
		this.invoiceTypeCode = invoiceTypeCode;
	}
	public String getDocumentCurrencyCode() {
		return documentCurrencyCode;
	}
    @XmlElement(name="DocumentCurrencyCode",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setDocumentCurrencyCode(String documentCurrencyCode) {
		this.documentCurrencyCode = documentCurrencyCode;
	}
	
	public Invoice(String ID,String issueDate,String issueTime, String documentCurrencyCode, String invoiceTypeCode) {
		this.ID=ID;
		this.issueDate=issueDate;
		this.issueTime=issueTime;
		this.documentCurrencyCode=documentCurrencyCode;
		this.invoiceTypeCode=invoiceTypeCode;
	}
	
	public Invoice(String ID,String issueDate,String issueTime) {
		this.ID=ID;
		this.issueDate=issueDate;
		this.issueTime=issueTime;
	}
	
    private String ID;
    
    private String issueDate;
    
    private String issueTime;
    


	private String invoiceTypeCode;
    
    private String documentCurrencyCode;

	@Override
	public String toString() {
		return "Invoice [ID=" + ID + ", issueDate=" + issueDate + ", issueTime=" + issueTime + ", invoiceTypeCode="
				+ invoiceTypeCode + ", documentCurrencyCode=" + documentCurrencyCode + "]";
	}



	}
