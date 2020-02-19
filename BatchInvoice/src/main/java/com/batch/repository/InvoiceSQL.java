package com.batch.repository;


public interface InvoiceSQL {
	
	
	public final String FETCH_REPORT_BY_JOB="SELECT IV.ID AS ID\r\n" + 
	  		",'FB01' AS TX_CODE\r\n" + 
	  		",'2016' AS COMP_CODE\r\n" + 
	  		",COALESCE(VM.VENDOR_ID,'NOT_FOUND' ) AS VENDOR_ID\r\n" + 
	  		",IV.ISSUE_DATE as DOC_DATE \r\n" + 
	  		",CURRENT_DATE as POSTING_DATE \r\n" + 
//	  		",RIGHT('0' || RTRIM(month(IV.DOC_DATE)),2) as PERIOD \r\n" + 
	  		",RIGHT('0' || RTRIM(month(CURRENT_DATE)),2) as PERIOD \r\n" + 
	  		",'KU' AS DOC_TYPE\r\n" + 
	  		",IV.CURRENCY_CODE \r\n" + 
	  		",IV.NUMBER_SERIE as REF_NO \r\n" + 
	  		",IV.CUSTOM_SERIE as DOC_HDR_TXT \r\n" + 
	  		",COALESCE(PKC.POST_KEY,'NOT_FOUND' ) AS POST_KEY\r\n" + 
	  		", case IV.Amnt_local_Type " +
	  		"	when 'sub-total' 	then '621202' " +
	  		"	when 'igv' 			then '165303' " +
	  		"	when  'total' 		then COALESCE(VM.VENDOR_ID,'NOT_FOUND' ) " +
	  		" 	else 'NOT_FOUND' END " +
	  		"as ACCOUNT \r\n" + 
	  		",IV.AMNT_DOC_CURR \r\n" + 
	  		",IV.AMNT_LOCAL_TYPE \r\n" + 
	  		",'ZP07' AS PAYM_TRM \r\n" + 
//	  		",IV.ISSUE_DATE AS BASE_DATE\r\n" + 	  		
	  		",CURRENT_DATE AS BASE_DATE\r\n" + 

//	  		",COALESCE(pc.ccntr,'NOT_FOUND') AS CCNTR\r\n" + 
	  		", case IV.Amnt_local_Type " +
	  		"	when 'sub-total' 	then COALESCE(pc.ccntr,'NOT_FOUND' ) " +
	  		" 	else '' END " +
	  		"as CCNTR \r\n" + 
	  		
	  		",COALESCE(VM.assign_no,'NOT_FOUND' ) AS ASSIGN_NO\r\n" + 
	  		",COALESCE(IV.unique_phone_item,'NOT_FOUND')||' - '||COALESCE(pc.full_name,'NOT_FOUND') AS ITM_TXT\r\n" + 
	  		",case IV.ta "+
	  		"	when 0 then 'I0' "+
	  		"	else 'I2' END as TAX_CODE \r\n" + 
	  		",FILE_ID\r\n" + 
	  		"FROM (select *,ta as Amnt_Doc_Curr, 'igv' as Amnt_local_Type  from invoice where 1=1   \r\n" + 
	  		"union all\r\n" + 
	  		"select *,subtotal as Amnt_Doc_Curr, 'sub-total' as Amnt_local_Type  from invoice where 1=1   \r\n" + 
	  		"union all\r\n" + 
	  		"select *,pa as Amnt_Doc_Curr, 'total' as Amnt_local_Type  from invoice where 1=1     \r\n" + 
	  		" ) AS IV\r\n" + 
	  		" 	LEFT JOIN VENDOR_MAP AS VM ON IV.PARTY_ID = VM.PARTY_ID\r\n" + 
	  		"	LEFT JOIN POST_KEY_CONF AS PKC ON \r\n" + 
	  		"	IV.AMNT_LOCAL_TYPE=PKC.AMNT_LOCAL_TYPE \r\n" + 
	  		"	AND IV.INVOICE_TYPE_CODE=PKC.DOC_TYPE_CODE\r\n" + 
	  		" 	LEFT JOIN phone_ccntr_map AS pc ON IV.unique_phone_item = pc.phone\r\n" + 
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

	public final String CREATE_INVOICE_ROW="INSERT INTO invoice"
			+ "("
			+ "custom_serie"
			+ ",issue_date"
			+ ",issue_time"
			+ ",number_serie"
			+ ",ta"
			+ ",pa"
			+ ",lea"
			+ ",party_id"
			+ ",tax_code"
			+ ",invoice_type_code"
			+ ",doc_date"
			+ ",job_execution_id"
			+ ",file_id"
			+ ",currency_code"
			+ ",proc_status"
			+ ",proc_desc"
			+ ",unique_phone_item"
			+ ",phone_desc_item"
			+ ",subtotal"
			+ ") "
			+ "values "
			+ "("
			+ ":customSerie"
			+ ",:issueDate"
			+ ",:issueTime"
			+ ",:numberSerie"
			+ ",:taxAmount"
			+ ",:payableAmount"
			+ ",:lineExtensionAmount"
			+ ",:partyId"
			+ ",:taxCode"
			+ ",:invoiceTypeCode"
			+ ",:docDate"
			+ ",:jobExecutionID"
			+ ",:fileID"
			+ ",:currencyCode"
			+ ",:procStatus"
			+ ",:procDesc"
			+ ",:uniquePhoneItem"
			+ ",:phoneDescItem"
			+ ",:subtotal"
			+ ")"	;
}
