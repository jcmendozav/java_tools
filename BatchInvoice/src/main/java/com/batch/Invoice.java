package com.batch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name="Invoice",namespace="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2")
@XmlRootElement(name="Invoice")

public class Invoice {

	
	public String getID() {
		return ID;
	}
	@XmlElement(name="ID")
	public void setID(String iD) {
		ID = iD;
	}
	public String getIssueDate() {
		return issueDate;
	}
    @XmlElement(name="IssueDate")
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getIssueTime() {
		return issueTime;
	}
    @XmlElement(name="IssueTime")
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	
	public Invoice() {
		// TODO Auto-generated constructor stub
	}
	
	public Invoice(String ID,String issueDate,String issueTime) {
		this.ID=ID;
		this.issueDate=issueDate;
		this.issueTime=issueTime;
	}
	
    private String ID;
    
    private String issueDate;
    
    private String issueTime;

	@Override
	public String toString() {
		return "Invoice [ID=" + ID + ", issueDate=" + issueDate + ", issueTime=" + issueTime + "]";
	}


	}
