package com.batch.model;

import javax.xml.bind.annotation.XmlElement;

public class Item {
	
    @XmlElement(name="Description",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setDescription(String description) {
		Description = description;
	}
    
    public String getDescription() {
		return Description;
	}
    

	@Override
	public String toString() {
		return "Item [Description=" + Description + "]";
	}
	
	private String Description;
	
	

	
	

}
