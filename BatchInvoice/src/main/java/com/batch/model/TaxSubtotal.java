package com.batch.model;

import javax.xml.bind.annotation.XmlElement;

public class TaxSubtotal {

	private Double taxableAmount;
	private Double taxAmount;

    @XmlElement(name="TaxableAmount",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setTaxableAmount(Double taxableAmount) {
		this.taxableAmount=taxableAmount;
	}
	
    @XmlElement(name="TaxAmount",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount=taxAmount;
	}
    
    public Double getTaxableAmount() {
		return taxableAmount;
	}
    
    public Double getTaxAmount() {
		return taxAmount;
	} 
    
	
}
