package com.batch.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.batch.model.Invoice;
import com.batch.model.InvoiceExpDTO;
import com.batch.procesor.InvoiceItemExportProcessor;
import com.batch.repository.InvoiceExpRowMapper;
import com.batch.repository.InvoiceRowMapper;
import com.batch.writer.InvoiceItemExportWriter;

//@Configuration
//@EnableBatchProcessing
public class ExportInvoice {

	
	private static final Logger log = LoggerFactory.getLogger(ExportInvoice.class);

	
	 @Autowired
	 public JobBuilderFactory jobBuilderFactory;
	 
	 @Autowired
	 public StepBuilderFactory stepBuilderFactory;
	 
	@Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/output")
	private String outputPath;
	
	@Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/template/invoice_exp_template.xlsx")
	private String fileTemplatePath;
	
	@Value(".ext")
	private String exportExt;
	@Value(",")
	private String delimiter;
	
	@Value("yyyyMMdd_HHmmss")
	private String dateFormat;
	
	@Value("1")
	private int daysAgo;
	
	@Autowired
	DataSource dataSource;
//	 @Bean
//	 public DataSource dataSource() {
//	  final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//	  dataSource.setDriverClassName(@Value("{spring.datasource.url}"));
//	  dataSource.setUrl("jdbc:mysql://localhost/springbatch");
//	  dataSource.setUsername("root");
//	  dataSource.setPassword("root");
//	  
//	  return dataSource;
//	 }
	@Bean
	 public JdbcCursorItemReader<InvoiceExpDTO> expInvoiceReader(){
	  JdbcCursorItemReader<InvoiceExpDTO> reader = new JdbcCursorItemReader<InvoiceExpDTO>();
	  reader.setDataSource(dataSource);
	  reader.setSql("select " +
	  		"iv.id\r\n" + 
	  		",'FB01' as tx_code\r\n" + 
	  		",'2016' as comp_code\r\n" + 
	  		",vm.vendor_id\r\n" + 
	  		",iv.Doc_Date \r\n" + 
	  		",iv.Posting_Date \r\n" + 
	  		",iv.Period \r\n" + 
	  		",'KU' as doc_type\r\n" + 
	  		",iv.currency_code \r\n" + 
	  		",iv.ref_no \r\n" + 
	  		",iv.doc_hdr_txt \r\n" + 
	  		",pkc.post_key\r\n" + 
	  		",iv.Account \r\n" + 
	  		",iv.Amnt_Doc_Curr \r\n" + 
	  		",iv.Amnt_local_Type \r\n" + 
	  		",'ZP07' as paym_trm \r\n" + 
	  		",iv.Doc_Date as base_date\r\n" + 
	  		",'CCntr' as ccntr\r\n" + 
	  		",'Assign. No' as assign_no\r\n" + 
	  		",'Itm txt' as itm_txt\r\n" + 
	  		",iv.tax_code \r\n" + 
	  		",file_id\r\n" + 
	  		"FROM (\r\n" + 
	  		" SELECT ROW_NUMBER() OVER (ORDER BY id) AS group,  \r\n" + 
	  		"unnest(array[id, id, id]) AS id,  \r\n" + 
	  		"unnest(array[currency_code, currency_code, currency_code]) AS currency_code,  \r\n" + 
	  		"unnest(array[file_id, file_id, file_id]) AS file_id,  \r\n" + 
	  		"unnest(array[tax_code, tax_code, tax_code]) AS tax_code,  \r\n" + 
	  		"unnest(array[issue_date, issue_date, issue_date]) AS Doc_Date,  \r\n" + 
	  		"unnest(array[current_Date, current_Date, current_Date]) AS Posting_Date,  \r\n" + 
	  		"unnest(array[date_part('month', doc_date), date_part('month', doc_date), date_part('month', doc_date)]) AS Period,  \r\n" + 
	  		"unnest(array[number_serie, number_serie, number_serie]) AS ref_no,  \r\n" + 
	  		"unnest(array[custom_serie, custom_serie, custom_serie]) AS doc_hdr_txt,  \r\n" + 
	  		"unnest(array['TBD', 'TBD','TBD']) AS Account,  \r\n" + 
	  		"unnest(array[issue_date, issue_date, issue_date]) AS Base_Date,  \r\n" + 
	  		"--unnest(array['TBD', 'TBD','TBD']) AS CCntr,  \r\n" + 
	  		"--unnest(array['TBD', 'TBD','TBD']) AS Assign_no,  \r\n" + 
	  		"--unnest(array['TBD', 'TBD','TBD']) AS itm_txt,  \r\n" + 
	  		"unnest(array[party_id, party_id,party_id]) AS party_id,  \r\n" + 
	  		"unnest(array[invoice_type_code, invoice_type_code,invoice_type_code]) AS invoice_type_code,\r\n" + 
	  		"unnest(array[ta, lea,pa ]) AS Amnt_Doc_Curr,\r\n" + 
	  		"unnest(array['igv', 'sub-total','total']) AS Amnt_local_Type  \r\n" + 
	  		" from invoice\r\n" + 
	  		" where 1=1\r\n" + 
	  		" and creationdate>=current_Date -"+daysAgo+"\r\n" + 
	  		" ) as iv\r\n" + 
	  		" , vendor_map as vm\r\n" + 
	  		" , post_key_conf as pkc\r\n" + 
	  		"where 1=1\r\n" + 
	  		"and iv.party_id = vm.party_id\r\n" + 
	  		"and iv.Amnt_local_Type=pkc.Amnt_local_Type\r\n" + 
	  		"and iv.invoice_type_code=pkc.doc_type_code\r\n" + 
	  		"ORDER BY 1, 2 ;  ");
	  reader.setRowMapper(new InvoiceExpRowMapper());
	  
	  return reader;
	 }
	
	@Bean
	public InvoiceItemExportProcessor expInvoiceProc() {
		return new InvoiceItemExportProcessor();
	}
	
	
	@Bean
	
	public InvoiceItemExportWriter expInvoiceXLSWriter(){
		InvoiceItemExportWriter writer = new InvoiceItemExportWriter();
		writer.setDateFormat(dateFormat);
		writer.setFileTemplatePath(fileTemplatePath);
		writer.setOutputPath(outputPath);
		return writer;
	}
	
	
	@Bean
	public Step stepExportInvoiceStep() {
		return stepBuilderFactory
				.get("stepExportInvoiceStep")
				.<InvoiceExpDTO,InvoiceExpDTO> chunk(2)
				.reader(expInvoiceReader())
				.processor(expInvoiceProc())
				.writer(expInvoiceXLSWriter())
				.build();
	}
	
//	@Bean
//	public Job exportInvoiceJob() {
//		
//		
//		return jobBuilderFactory.get("ExportInvoiceJob")
//				.incrementer(new RunIdIncrementer())
//				.start(stepExportInvoiceStep())
//				//.end()
//				.build();
//		
//	}
}
