package com.batch.model.map;

import java.util.regex.Pattern;

public class VendorMap implements GenModelMap{

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

	public void setAssignNo(String assignNo) {
		this.assignNo = assignNo;
	}
	
	public String getAssignNo() {
		return assignNo;
	}
	

	public VendorMap(String[] fields) {

		init(fields);

	}
	
	public VendorMap(String line, String delimiter
			) {
		
		String[] fields = line.split(delimiter,-1);
		init(fields);
	}
	
	
	public VendorMap() {
		// TODO Auto-generated constructor stub
	}
	
	public void init(String[] fields){

		this.vendorName=fields[0];
		this.partyId=fields[1];
		this.vendorId=fields[2];
		this.assignNo=fields[3];

	}

	@Override
	public void init(String line, String delimiter) {
		// TODO Auto-generated method stub
		String[] fields = line.split(Pattern.quote(delimiter),-1);
		init(fields);
	}
	
	
	String partyId; 
	String vendorId; 
	String vendorName;
	private String assignNo;

	

}
