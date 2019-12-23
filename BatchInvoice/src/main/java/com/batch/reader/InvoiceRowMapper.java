package com.batch.reader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;
import com.batch.model.InvoiceExpDTO;

public class InvoiceRowMapper implements RowMapper<Invoice> {

	
	private static final Logger log = LoggerFactory.getLogger(InvoiceRowMapper.class);

	@Override
	public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		// vendor_id | post_key |  doc_date  | posting_date | period |  ref_no  | doc_hdr_txt | account | amnt_doc_curr | amnt_local_type | file_id
		
		
		log.debug("Found: rowNum:"+rowNum+",ID:"+rs.getString("ID"));
		Invoice invoice = new Invoice();
		invoice.setId(rs.getInt("id"));
		invoice.setInvoiceId(rs.getString("invoice_id"));
		invoice.setIssueDate(rs.getString("issue_date"));
		invoice.setIssueTime(rs.getString("issue_time"));
		invoice.setPartyId(rs.getString("party_id"));
		invoice.setVendorId(rs.getString("vendor_id"));
		invoice.setCustomSerie(rs.getString("custom_serie"));
		invoice.setNumberSerie(rs.getString("number_serie"));
		invoice.setIssueTimeStamp(rs.getString("issue_time_stamp"));
		invoice.setCurrencyCode(rs.getString("currency_code"));
		invoice.setTaxCode(rs.getString("tax_code"));
		invoice.setLineExtensionAmount(rs.getDouble("lea"));
		invoice.setTaxAmount(rs.getDouble("ta"));
		invoice.setPayableAmount(rs.getDouble("pa"));
		invoice.setStatus(rs.getInt("status"));
		invoice.setFileID(rs.getInt("file_id"));
		invoice.setJobExecutionID(rs.getLong("job_execution_id"));
		invoice.setDocDate(rs.getTimestamp("doc_date")==null?null:rs.getTimestamp("doc_date").toLocalDateTime());
		invoice.setLastUpdatedDate(rs.getTimestamp("last_update_date")==null?null:rs.getTimestamp("last_update_date").toLocalDateTime());
		invoice.setCreationDate(rs.getTimestamp("creationdate")==null?null:rs.getTimestamp("creationdate").toLocalDateTime());
		invoice.setInvoiceTypeCode(rs.getString("invoice_type_code"));
		invoice.setProcStatus(rs.getInt("proc_status"));
		invoice.setProcDesc(rs.getString("proc_desc"));
		invoice.setFilePath(rs.getString("file_path"));
		
		return invoice ;
	}

}
