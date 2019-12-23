package com.batch.procesor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
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

import com.batch.constant.InvoiceProcess;
import com.batch.exception.DateFormatInvoiceFields;
import com.batch.exception.MissingMandadoryInvoiceFields;
import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;
import com.batch.model.InvoiceFile;
import com.batch.util.DateValidator;
import com.batch.util.DateValidatorUsingDateTimeFormatter;

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
		String filePath=executionContext.getString("filePath");
		
		
		invoiceDTO.setFilePath(filePath);
		
//		InvoiceFile invoiceFile = (InvoiceFile) executionContext.get("invoiceFile"); 
//		log.info("InvoiceFile from processor "
//				+ ",invoiceFile: {}"
//				+ ",fileID: {}"
//				+ ",jobID: {}"
//				,invoiceFile
//				,stepExecution.getExecutionContext().getInt("fileID")
//				,stepExecution.getJobExecutionId()
//				);
		Invoice invoice = new Invoice();
		invoice.setFilePath(invoiceDTO.getFilePath());
		invoice.setJobExecutionID(stepExecution.getJobExecutionId());
		invoice.setFileID(stepExecution.getExecutionContext().getInt("fileID"));

		try {
			

		//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss.SS");
		
		DateTimeFormatter dtf = new DateTimeFormatterBuilder()
			    .appendPattern("yyyy-MM-ddHH:mm:ss")
			    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
			    .toFormatter();
		
		validateData(invoiceDTO, dtf);
		
		String dateStr = invoiceDTO.getIssueDate()+invoiceDTO.getIssueTime();

		
		LocalDateTime docDate =LocalDateTime.parse(dateStr, dtf);
		
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
//		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SS");  
//		Date docDate = formatter.parse(invoiceDTO.getIssueDate()+invoiceDTO.getIssueTime());
		invoice.setDocDate(docDate);
		invoice.setProcStatus(InvoiceProcess.SUCCESS);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			invoice.setProcStatus(InvoiceProcess.ERROR);
			invoice.setProcDesc(e.getMessage().substring(0, 255));
			log.info("Saving invoice process status with error: {}",invoice);

		}		
		executionContext.put("fileProcCounter",fileProcCount+1);
		
		
		
		return invoice;
	}

	public void setProcessingFileName(String file) {
		// TODO Auto-generated method stub
		this.currentFile=file;
		
	}
	
	public void validateData(InvoiceDTO invoiceDTO,DateTimeFormatter dtf) 
			throws 
			DateFormatInvoiceFields
			, MissingMandadoryInvoiceFields
	{
		DateValidator validator = new DateValidatorUsingDateTimeFormatter(dtf);
		String dateStr = invoiceDTO.getIssueDate()+invoiceDTO.getIssueTime();
		if(!validator.isValid(dateStr)) {
			throw new DateFormatInvoiceFields(invoiceDTO,dtf);
		}
		
		if(
				invoiceDTO.getID()==null 
			|| invoiceDTO.getID()=="EMPTY" 
			|| invoiceDTO.getInvoiceTypeCode()==null 
			|| invoiceDTO.getInvoiceTypeCode()=="EMPTY" 
			|| invoiceDTO.getAccountingSupplierParty()==null 
			|| invoiceDTO.getAccountingSupplierParty().getParty()==null 
			|| invoiceDTO.getAccountingSupplierParty().getParty().getPartyIdentification()==null 
			|| invoiceDTO.getAccountingSupplierParty().getParty().getPartyIdentification().getID()==null 
		) {
			throw new MissingMandadoryInvoiceFields(invoiceDTO);
		}
	}
	

}
