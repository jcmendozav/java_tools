package com.batch.model;

import javax.xml.bind.annotation.XmlElement;

public class TaxTotal {
	
	


	@XmlElement(name="TaxSubtotal",namespace="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
	public void setTaxSubtotal(TaxSubtotal taxSubtotal) {
		this.taxSubtotal=taxSubtotal;
	}
	
    @XmlElement(name="TaxAmount",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public Double getTaxAmount() {
		return this.taxAmount;
	}
	
	public TaxSubtotal getTaxSubtotal() {
		return taxSubtotal;
	}
	Double taxAmount;
	private TaxSubtotal taxSubtotal;
	
	
	@Override
	public String toString() {
		return "TaxTotal [taxAmount=" + taxAmount + "]";
	}
	
	
}
