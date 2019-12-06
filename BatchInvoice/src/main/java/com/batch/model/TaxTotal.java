package com.batch.model;

import javax.xml.bind.annotation.XmlElement;

public class TaxTotal {
	
	
    @XmlElement(name="TaxAmount",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public Double getTaxAmount() {
		return this.taxAmount;
	}
	Double taxAmount;
	
	
}
