package com.batch.model;

import javax.xml.bind.annotation.XmlElement;

public class PartyIdentification{
	public String getID() {
		return ID;
	}

    @XmlElement(name="ID",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setID(String iD) {
		ID = iD;
	}

	String ID;
}