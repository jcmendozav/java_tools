package com.batch.repository;


public interface VendorMapSQL {
	


	
	public final String CREATE = "INSERT INTO VENDOR_MAP"
			+ "("
			+ "party_id"
			+ ",vendor_id"
			+ ",vendor_name"

			+ ") "
			+ "values "
			+ "("
			+ ":partyId"
			+ ",:vendorId"
			+ ",:vendorName"
			+ ")"		;
	
	public final String DELETE_ALL = "DELETE VENDOR_MAP ";

}
