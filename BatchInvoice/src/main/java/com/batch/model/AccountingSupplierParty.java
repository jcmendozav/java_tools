package com.batch.model;

import javax.xml.bind.annotation.XmlElement;

class AccountingSupplierParty{
	Party party;
	
    @XmlElement(name="Party",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
	public void setParty(Party party) {
		this.party = party;
	}
	public Party getParty() {
		return party;
	}
}