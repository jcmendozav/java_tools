package com.batch.model;

import javax.xml.bind.annotation.XmlElement;

public class InvoiceLine {
	
    @XmlElement(name="ID",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
	public void setID(String iD) {
		ID = iD;
	}
    
    public String getID() {
		return ID;
	}
	
    @XmlElement(name="Item",namespace = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2")
	public void setItem(Item item) {
		this.item = item;
	}
	
    public Item getItem() {
		return item;
	}

    
    
    
	@Override
	public String toString() {
		return "InvoiceLine [ID=" + ID + ", item=" + item + "]";
	}




	private String ID;
	private Item item;

}
