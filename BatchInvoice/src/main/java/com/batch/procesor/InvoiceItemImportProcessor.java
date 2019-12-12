package com.batch.procesor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.batch.exception.MissingMandadoryInvoiceFields;
import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;
import com.batch.model.InvoiceFile;

@Configuration
public class InvoiceItemImportProcessor implements ItemProcessor<InvoiceDTO, Invoice>
{

	
	private static final Logger log = LoggerFactory.getLogger(InvoiceItemImportProcessor.class);

	
	@Value("I2")
	private String taxCodeWithTax;
	
	@Value("I0")
	private String taxCodeWithNoTax;
	
	private Resource[] resources;
	


	private String currentFile;
	
	public void setResources(Resource[] resources) {
		this.resources = resources;
	}

	private StepExecution stepExecution;

	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
	

	
	
	
	@Override
	public Invoice process(InvoiceDTO invoiceDTO) throws Exception {
		// TODO Auto-generated method stub

		ExecutionContext executionContext = stepExecution.getExecutionContext();
		int fileProcCount=executionContext.getInt("fileProcCounter");
		InvoiceFile invoiceFile = (InvoiceFile) executionContext.get("invoiceFile"); 
		System.out.println("InvoiceFile from processor "
				+ ",invoiceFile: "+invoiceFile+""
				+ ",fileID: "+stepExecution.getExecutionContext().getInt("fileID")+""
				+ ",jobID: "+stepExecution.getJobExecutionId()+""
						+ "");
		
		//int fileID=executionContext.getInt("fileID");
		//String uuid=executionContext.getString("uuid");
//		System.out.println(
//				"File Info from process: "
//						+ ",fileName:"+executionContext.getString("fileName")+""
//						+ ",UUID:"+executionContext.getString("UUID")+""
//						+ ",fileProcCounter:"+executionContext.getInt("fileProcCounter")+""
//						//+ ",filePath:"+executionContext.getString("filePath")+""
//						+ ",fileLength:"+executionContext.getLong("fileLength")+""
//						+ ",fileLines:"+executionContext.getLong("fileLines")+""
//						+ ",no_resources:"+resources.length+""
//				//+ ", processing item = " + invoiceDTO.toString() 
//				);
		
		if(
			invoiceDTO.getID()==null 
			|| invoiceDTO.getInvoiceTypeCode()==null 
			|| invoiceDTO.getAccountingSupplierParty()==null 
			|| invoiceDTO.getAccountingSupplierParty().getParty()==null 
			|| invoiceDTO.getAccountingSupplierParty().getParty().getPartyIdentification()==null 
			|| invoiceDTO.getAccountingSupplierParty().getParty().getPartyIdentification().getID()==null 
		) {
			throw new MissingMandadoryInvoiceFields(invoiceDTO);
		}
		
		Invoice invoice = new Invoice();
		invoice.setCurrencyCode(invoiceDTO.getDocumentCurrencyCode());
		invoice.setCustomSerie(invoiceDTO.getInvoiceTypeCode()+""
				+ "*"+invoiceDTO.getID().split("-")[0]);
		invoice.setNumberSerie(invoiceDTO.getID().split("-")[1]);
		invoice.setIssueDate(invoiceDTO.getIssueDate());
		invoice.setIssueTime(invoiceDTO.getIssueTime());
		invoice.setPartyId(invoiceDTO.getAccountingSupplierParty().getParty().getPartyIdentification().getID());
		invoice.setTaxAmount(invoiceDTO.getTaxTotal().getTaxAmount());
		invoice.setPayableAmount(invoiceDTO.getLegalMonetaryTotal().getPayableAmount());
		invoice.setLineExtensionAmount(invoiceDTO.getLegalMonetaryTotal().getLineExtensionAmount());
		invoice.setPayableAmount(invoiceDTO.getLegalMonetaryTotal().getPayableAmount());
		invoice.setTaxCode((invoiceDTO.getTaxTotal().getTaxAmount()!=0?taxCodeWithTax:taxCodeWithNoTax));
		invoice.setInvoiceTypeCode(invoiceDTO.getInvoiceTypeCode());
		invoice.setJobExecutionID(stepExecution.getJobExecutionId());
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SS");  
		Date docDate = formatter.parse(invoiceDTO.getIssueDate()+invoiceDTO.getIssueTime());
		invoice.setDocDate(docDate);
		invoice.setFileID(invoiceFile.getID());
		executionContext.put("fileProcCounter",fileProcCount+1);
		
		return invoice;
	}

	public void setProcessingFileName(String file) {
		// TODO Auto-generated method stub
		this.currentFile=file;
		
	}
	

}
