package com.batch.model;

import javax.xml.bind.annotation.XmlElement;

public class LegalMonetaryTotal {
	
	
	
	public Double getLineExtensionAmount() {
		return lineExtensionAmount;
	}
	
    @XmlElement(name="LineExtensionAmount",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")

	public void setLineExtensionAmount(Double lineExtensionAmount) {
		this.lineExtensionAmount = lineExtensionAmount;
	}
	public Double getTaxInclusiveAmount() {
		return taxInclusiveAmount;
	}
    @XmlElement(name="TaxInclusiveAmount",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setTaxInclusiveAmount(Double taxInclusiveAmount) {
		this.taxInclusiveAmount = taxInclusiveAmount;
	}
	public Double getAllowanceTotalAmount() {
		return allowanceTotalAmount;
	}
    @XmlElement(name="AllowanceTotalAmount",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setAllowanceTotalAmount(Double allowanceTotalAmount) {
		this.allowanceTotalAmount = allowanceTotalAmount;
	}
	public Double getPayableAmount() {
		return payableAmount;
	}
    @XmlElement(name="PayableAmount",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}
	Double lineExtensionAmount;
	Double taxInclusiveAmount;
	Double allowanceTotalAmount;
	Double payableAmount;
	@Override
	public String toString() {
		return "LegalMonetaryTotal [lineExtensionAmount=" + lineExtensionAmount + ", taxInclusiveAmount="
				+ taxInclusiveAmount + ", allowanceTotalAmount=" + allowanceTotalAmount + ", payableAmount="
				+ payableAmount + "]";
	}
	
	
}
