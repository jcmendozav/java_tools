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
import com.batch.procesor.InvoiceItemExportProcessor;

@Configuration
@EnableBatchProcessing
public class ExportInvoice {

	
	private static final Logger log = LoggerFactory.getLogger(ExportInvoice.class);

	
	 @Autowired
	 public JobBuilderFactory jobBuilderFactory;
	 
	 @Autowired
	 public StepBuilderFactory stepBuilderFactory;
	 
	@Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/output")
	private String exportPath;
	
	@Value(".ext")
	private String exportExt;
	
	@Value("yyyyMMdd_HHmmss")
	private String dateFormat;
	
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
	 public JdbcCursorItemReader<Invoice> expInvoiceReader(){
	  JdbcCursorItemReader<Invoice> reader = new JdbcCursorItemReader<Invoice>();
	  reader.setDataSource(dataSource);
	  reader.setSql("SELECT ID,currency_code,number_serie,custom_serie,lea,ta,pa,currency_code FROM invoice");
	  reader.setRowMapper(new InvoiceRowMapper());
	  
	  return reader;
	 }
	
	@Bean
	public InvoiceItemExportProcessor expInvoiceProc() {
		return new InvoiceItemExportProcessor();
	}
	
	@Bean
	public FlatFileItemWriter<Invoice> expInvoiceWriter(){
		
		FlatFileItemWriter<Invoice> fileItemWriter = new FlatFileItemWriter<Invoice>();
        String timeStamp = new SimpleDateFormat(dateFormat).format(new Date());
        String newFileName=exportPath+"/"+"invoices"+"_"+timeStamp+".csv";
        Resource res = new FileSystemResource(newFileName);
        try {
			res.getFile().createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileItemWriter.setResource( res );
		fileItemWriter.setLineAggregator(new DelimitedLineAggregator<Invoice>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<Invoice>() {{
					
					setNames(new String[] {"customSerie","numberSerie","LineExtensionAmount","TaxAmount","PayableAmount"});
				}});
				
			}
		});
		return fileItemWriter;
	}
	
	@Bean
	public Step stepExportInvoiceStep() {
		return stepBuilderFactory
				.get("stepExportInvoiceStep")
				.<Invoice,Invoice> chunk(2)
				.reader(expInvoiceReader())
				.processor(expInvoiceProc())
				.writer(expInvoiceWriter())
				.build();
	}
	
	@Bean
	public Job exportInvoiceJob() {
		
		return jobBuilderFactory.get("ExportInvoiceJob")
				.incrementer(new RunIdIncrementer())
				.start(stepExportInvoiceStep())
				//.end()
				.build();
		
	}
}
