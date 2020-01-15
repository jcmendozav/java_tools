package com.batch.repository;


public interface PhoneCcntrSQL {
	


	
	public final String CREATE = "INSERT INTO PHONE_CCNTR_MAP"
			+ "("
			+ "phone"
			+ ",ccntr"
			+ ",full_name"

			+ ") "
			+ "values "
			+ "("
			+ ":phone"
			+ ",:ccntr"
			+ ",:fullName"
			+ ")"		;
	
	public final String DELETE_ALL = "DELETE PHONE_CCNTR_MAP ";

}
