package com.batch.repository;


public interface invoiceSQL {
	
	
	public final String FETCH_REPORT_BY_JOB="SELECT IV.ID AS ID\r\n" + 
	  		",'FB01' AS TX_CODE\r\n" + 
	  		",'2016' AS COMP_CODE\r\n" + 
	  		",COALESCE(VM.VENDOR_ID,'NOT_FOUND') AS VENDOR_ID\r\n" + 
	  		",IV.ISSUE_DATE as DOC_DATE \r\n" + 
	  		",CURRENT_DATE as POSTING_DATE \r\n" + 
	  		",month(IV.DOC_DATE) as PERIOD \r\n" + 
	  		",'KU' AS DOC_TYPE\r\n" + 
	  		",IV.CURRENCY_CODE \r\n" + 
	  		",IV.NUMBER_SERIE as REF_NO \r\n" + 
	  		",IV.CUSTOM_SERIE as DOC_HDR_TXT \r\n" + 
	  		",COALESCE(PKC.POST_KEY,'NOT_FOUND' ) AS POST_KEY\r\n" + 
	  		",VM.vendor_id as ACCOUNT \r\n" + 
	  		",IV.AMNT_DOC_CURR \r\n" + 
	  		",IV.AMNT_LOCAL_TYPE \r\n" + 
	  		",'ZP07' AS PAYM_TRM \r\n" + 
	  		",IV.ISSUE_DATE AS BASE_DATE\r\n" + 
	  		",'CCntr' AS CCNTR\r\n" + 
	  		",'Assign. No' AS ASSIGN_NO\r\n" + 
	  		",'Itm txt' AS ITM_TXT\r\n" + 
	  		",IV.TAX_CODE \r\n" + 
	  		",FILE_ID\r\n" + 
	  		"FROM (select *,ta as Amnt_Doc_Curr, 'igv' as Amnt_local_Type from invoice where 1=1   \r\n" + 
	  		"union all\r\n" + 
	  		"select *,lea as Amnt_Doc_Curr, 'sub-total' as Amnt_local_Type from invoice where 1=1   \r\n" + 
	  		"union all\r\n" + 
	  		"select *,pa as Amnt_Doc_Curr, 'total' as Amnt_local_Type from invoice where 1=1     \r\n" + 
	  		" ) AS IV\r\n" + 
	  		" 	LEFT JOIN VENDOR_MAP AS VM ON IV.PARTY_ID = VM.PARTY_ID\r\n" + 
	  		"	LEFT JOIN POST_KEY_CONF AS PKC ON \r\n" + 
	  		"	IV.AMNT_LOCAL_TYPE=PKC.AMNT_LOCAL_TYPE \r\n" + 
	  		"	AND IV.INVOICE_TYPE_CODE=PKC.DOC_TYPE_CODE\r\n" + 
	  		" WHERE 1=1 \r\n "+ 
	  		" and proc_status=? "+ 
	  		" and JOB_EXECUTION_ID=? \r\n" + 
	  		"order by 1,2;";
	
	public final String FETCH_FILES_BY_JOB=""
  	  		+ "select iv.*, ifj.file_path, ifj.file_name from invoice iv"
  	  		+ " , invoice_file_job ifj	"
  	  		+ "where 1=1 "
  	  		+ "and ifj.id = iv.file_id	"
  	  		+ "and iv.job_execution_id = ? ;";
	
	public final String INSERT_SQL = "INSERT INTO invoice_file_job"
			+ "("
			+ "file_name"
			+ ",file_path"
			+ ",uuid"
			+ ",lines"
			+ ",size"
			+ ",job_id"
			+ ") "
			+ "values "
			+ "("
			+ ":fileName"
			+ ",:filePath"
			+ ",:uuid"
			+ ",:lines"
			+ ",:size"
			+ ",:jobID"
			+ ")"		;
	
	public final String UPDATE_SQL = "UPDATE invoice_file_job "
			+ "set object_counter=:objCounter "
			+ ", last_update_date=CURRENT_TIMESTAMP "
			+ "where id=:id";

}
