package com.batch.repository;


public interface PostKeyConfSQL {
	


	
	public final String CREATE = "INSERT INTO POST_KEY_CONF"
			+ "("
			+ "amnt_local_type"
			+ ",doc_type_code"
			+ ",post_key"

			+ ") "
			+ "values "
			+ "("
			+ ":amntLocalType"
			+ ",:docTypeCode"
			+ ",:postKey"
			+ ")"		;
	
	public final String DELETE_ALL = "DELETE POST_KEY_CONF ";

}
