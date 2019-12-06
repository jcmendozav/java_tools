package com.batch.model;

import javax.xml.bind.annotation.XmlElement;

class Party{
	PartyIdentification partyIdentification;
	
    @XmlElement(name="PartyIdentification",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
	public void setPartyIdentification(PartyIdentification partyIdentification) {
		this.partyIdentification = partyIdentification;
	}
	public PartyIdentification getPartyIdentification() {
		return partyIdentification;
	}
	
}