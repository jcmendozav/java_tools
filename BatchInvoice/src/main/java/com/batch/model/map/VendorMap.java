package com.batch.model.map;

public class VendorMap {

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	String partyId; 
	String vendorId; 
	String vendorName;

	
	public VendorMap(String[] fields) {

		init(fields);

	}
	
	public VendorMap(String line, String delimiter
			) {
		
		String[] fields = line.split(delimiter);
		init(fields);
	}
	
	public void init(String[] fields){

		this.partyId=fields[1];
		this.vendorId=fields[2];
		this.vendorName=fields[0];
		
	}

}
