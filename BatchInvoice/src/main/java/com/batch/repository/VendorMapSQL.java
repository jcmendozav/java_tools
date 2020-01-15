package com.batch.repository;


public interface VendorMapSQL {
	


	
	public final String CREATE = "INSERT INTO VENDOR_MAP"
			+ "("
			+ "party_id"
			+ ",vendor_id"
			+ ",vendor_name"
			+ ",assign_no"

			+ ") "
			+ "values "
			+ "("
			+ ":partyId"
			+ ",:vendorId"
			+ ",:vendorName"
			+ ",:assignNo"
			+ ")"		;
	
	public final String DELETE_ALL = "DELETE VENDOR_MAP ";

}
