package com.batch.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;
import com.batch.model.InvoiceExpDTO;

public class InvoiceExpRowMapper implements RowMapper<InvoiceExpDTO> {

	
	private static final Logger log = LoggerFactory.getLogger(InvoiceExpRowMapper.class);

	@Override
	public InvoiceExpDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		// vendor_id | post_key |  doc_date  | posting_date | period |  ref_no  | doc_hdr_txt | account | amnt_doc_curr | amnt_local_type | file_id
		log.debug("Found: "
				+ "rowNum:{} "
				+ ",id:{}"
				,rowNum
				,rs.getString("id"));
		InvoiceExpDTO invoice = new InvoiceExpDTO();
		
		invoice.setTxCode(rs.getString("tx_code"));
		invoice.setCompCode(rs.getString("comp_code"));
		invoice.setVendorId(rs.getString("vendor_id"));
		invoice.setPostKey(rs.getString("post_key"));
		invoice.setDocDate(rs.getDate("doc_date"));		
		invoice.setPostingDate(rs.getDate("posting_date"));
		invoice.setBaseDate(rs.getDate("base_date"));
		invoice.setPeriod(rs.getString("period"));
		invoice.setDocType(rs.getString("doc_type"));
		invoice.setCurrencyCode(rs.getString("currency_code"));
		invoice.setRefNo(rs.getString("ref_no"));
		invoice.setDocHdr(rs.getString("doc_hdr_txt"));
		invoice.setAccount(rs.getString("account"));
		invoice.setAmntDocCurr(rs.getDouble("amnt_doc_curr"));
		invoice.setAmntLocalType(rs.getString("amnt_local_type"));
		invoice.setPaymTrm(rs.getString("paym_trm"));
		invoice.setCcntr(rs.getString("ccntr"));
		invoice.setAssignNo(rs.getString("assign_no"));
		invoice.setItmTxt(rs.getString("itm_txt"));
		invoice.setTaxCode(rs.getString("tax_code"));
		invoice.setFileId(rs.getInt("file_id"));
		
		return invoice ;
	}

}
