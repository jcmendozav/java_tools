package com.batch.procesor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.batch.config.InvoiceProperties;
import com.batch.constant.InvoiceProcess;
import com.batch.exception.DateFormatInvoiceFields;
import com.batch.exception.MissingMandadoryInvoiceFields;
import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;
import com.batch.model.InvoiceFile;
import com.batch.model.InvoiceLine;
import com.batch.util.DateValidator;
import com.batch.util.DateValidatorUsingDateTimeFormatter;

@Configuration
public class InvoiceItemImportProcessor implements ItemProcessor<InvoiceDTO, Invoice>
{

	
	private static final Logger log = LoggerFactory.getLogger(InvoiceItemImportProcessor.class);

	@Autowired
	InvoiceProperties invoiceProperties;
	
	@Value("I2")
	private String taxCodeWithTax;
	
	@Value("I0")
	private String taxCodeWithNoTax;

	
	Pattern msisdnPattern ;
	

//	public void setMsisdnRegex(String msisdnRegex) {
//		log.info("msisdnRegex: {}",msisdnRegex);
//		this.msisdnRegex=msisdnRegex;
//		this.msisdnPattern = Pattern.compile(msisdnRegex);
//	}

	

	private StepExecution stepExecution;

private String currentFile;

	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		this.msisdnPattern = Pattern.compile(invoiceProperties.importData.getMsisdnRegex());

	}
	

	
	
	
	@Override
	public Invoice process(InvoiceDTO invoiceDTO) throws Exception {
		// TODO Auto-generated method stub
		log.info("importProperties: "+invoiceProperties);

		log.info("importProperties.getMsisdnRegex: "+invoiceProperties.importData.getMsisdnRegex());

		ExecutionContext executionContext = stepExecution.getExecutionContext();
		int fileProcCount=executionContext.getInt("fileProcCounter");
		String filePath=executionContext.getString("filePath");
		
		
		invoiceDTO.setFilePath(filePath);

		Invoice invoice = new Invoice();
		invoice.setFilePath(invoiceDTO.getFilePath());
		invoice.setJobExecutionID(stepExecution.getJobExecutionId());
		invoice.setFileID(stepExecution.getExecutionContext().getInt("fileID"));

		try {
			

		//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss.SS");
		
		DateTimeFormatter dtf = new DateTimeFormatterBuilder()
			    .appendPattern("[yyyy-MM-ddHH:mm:ss][yyyy-MM-dd]")
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
		List<String> phoneList = getDescPhoneNumber(
				invoiceDTO.getInvoiceLines().stream().map(x -> x.getItem().getDescription()).collect(Collectors.toList()), 
				msisdnPattern);
		invoice.setPhoneItemList(phoneList);
		invoice.setUniquePhoneItem((phoneList.size()==1?phoneList.get(0):"INVALID_UNIQUE_VALUE"));
		String phoneDescItem = phoneList.toString();
		phoneDescItem = phoneDescItem.substring(0, phoneDescItem.length()<255? phoneDescItem.length(): 255);
		invoice.setPhoneDescItem(phoneList.size()!=1?phoneDescItem:"");
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

	private List<String> getDescPhoneNumber(List<String> descList, Pattern pattern) {
		// TODO Auto-generated method stub
		HashSet<String> phoneSet = new HashSet<String>();
		
		for(String desc : descList) {
			//String msisdn = line.getItem().getDescription();
			Matcher matcher = pattern.matcher(desc);
			if(matcher.find()) {
				phoneSet.add(matcher.group());
			}
			//phoneSet.add(msisdn);
		}
		
		return new ArrayList<String>(phoneSet);
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
