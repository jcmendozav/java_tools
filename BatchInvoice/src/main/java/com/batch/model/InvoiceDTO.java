package com.batch.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Invoice",namespace="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2")
//@XmlRootElement(name="Invoice")

public class InvoiceDTO {

	
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

	
	public InvoiceDTO() {
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
	
	public void setAccountingSupplierParty(AccountingSupplierParty accountingSupplierParty) {
		this.accountingSupplierParty = accountingSupplierParty;
	}
	
    @XmlElement(name="AccountingSupplierParty",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
	public AccountingSupplierParty getAccountingSupplierParty() {
		return accountingSupplierParty;
	}
    
    @XmlElement(name="LegalMonetaryTotal",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    public void setLegalMonetaryTotal(LegalMonetaryTotal legalMonetaryTotal) {
		this.legalMonetaryTotal = legalMonetaryTotal;
	}
    public LegalMonetaryTotal getLegalMonetaryTotal() {
		return legalMonetaryTotal;
	}
    
    @XmlElement(name="TaxTotal",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
    public void setTaxTotal(TaxTotal taxTotal) {
		this.taxTotal = taxTotal;
	}
    
    public TaxTotal getTaxTotal() {
		return taxTotal;
	}
    
    public InvoiceDTO(String ID,String issueDate,String issueTime, String documentCurrencyCode, String invoiceTypeCode) {
		this.ID=ID;
		this.issueDate=issueDate;
		this.issueTime=issueTime;
		this.documentCurrencyCode=documentCurrencyCode;
		this.invoiceTypeCode=invoiceTypeCode;

	}
	
	public InvoiceDTO(String ID,String issueDate,String issueTime) {
		this.ID=ID;
		this.issueDate=issueDate;
		this.issueTime=issueTime;
	}
	
    private String ID;
    private String issueDate;
    private String issueTime;
	private String invoiceTypeCode;
    private String documentCurrencyCode;
    private AccountingSupplierParty accountingSupplierParty;
    private LegalMonetaryTotal legalMonetaryTotal;
    private TaxTotal taxTotal;


	@Override
	public String toString() {
		return "Invoice ["
				+ "ID=" + ID 
				+ ", issueDate=" + issueDate 
				+ ", issueTime=" + issueTime 
				+ ", invoiceTypeCode="+ invoiceTypeCode 
				+ ", documentCurrencyCode=" + documentCurrencyCode 
				+ ", accountingSupplierParty="+ accountingSupplierParty 
				+ ", ruc="+ accountingSupplierParty.getParty().getPartyIdentification().getID()
				+ ", igv="+ taxTotal.getTaxAmount()
				+ ", subtotal="+ legalMonetaryTotal.getLineExtensionAmount()
				+ ", total="+ legalMonetaryTotal.getPayableAmount()
				+ "]";
	}





	}
