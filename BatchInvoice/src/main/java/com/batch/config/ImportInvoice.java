package com.batch.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.batch.listener.JobCompletionNotificationListener;
import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;
import com.batch.procesor.InvoiceItemProcessor;
import com.batch.skipPolicy.ImportInvoiceSkipPolicy;
import com.batch.tasklet.FileCopyTasklet;


@Configuration
@EnableBatchProcessing
public class ImportInvoice {
	
	
	private static final Logger log = LoggerFactory.getLogger(ImportInvoice.class);

	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	//@Value("file:D:\\Users\\jcmendozav\\Documentos\\Ericsson\\Dev\\Contabilidad\\input\\*.xml")
	@Value("file:///D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/input/*.xml")
	private Resource[] inputResources;

	@Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/backup")
	private String backupPath;
	
	@Value(".bu")
	private String backupExt;
	
	@Value("yyyyMMdd_HHmmss")
	private String dateFormat;
	
	

	@Bean
	public MultiResourceItemReader<InvoiceDTO> multiResourceItemReader() 
	{
	    MultiResourceItemReader<InvoiceDTO> resourceItemReader = new MultiResourceItemReader<InvoiceDTO>();
	    resourceItemReader.setResources(inputResources);
	    resourceItemReader.setDelegate(reader());
	    resourceItemReader.getCurrentResource();
	    return resourceItemReader;
	    
	}
	
	//@StepScope
    @Bean
    StaxEventItemReader<InvoiceDTO> reader()  {
        StaxEventItemReader<InvoiceDTO> xmlFileReader = new StaxEventItemReader<>();
        xmlFileReader.setFragmentRootElementName("Invoice");
        Jaxb2Marshaller invoiceMarshaller = new Jaxb2Marshaller();
        invoiceMarshaller.setClassesToBeBound(InvoiceDTO.class);
        xmlFileReader.setUnmarshaller(invoiceMarshaller);

        return xmlFileReader;
    }
    


	@Bean
	@StepScope
	public InvoiceItemProcessor processor(@Value("#{stepExecutionContext['fileName']}") String file,
			@Value("#{stepExecution.jobExecution.id}") Long jobID) {
		

		InvoiceItemProcessor invoiceItemProcessor = new InvoiceItemProcessor();
		invoiceItemProcessor.setResources(inputResources);
		System.out.println("----processor----fileName--->" + file);
		System.out.println("----processor----jobID--->" + jobID);
		invoiceItemProcessor.setProcessingFileName(file);
		//invoiceItemProcessor.setProcessingJobid(jobID);
		return invoiceItemProcessor;
	}
	
	@Bean
	public JdbcBatchItemWriter<Invoice> writer(DataSource dataSource){
		return new  JdbcBatchItemWriterBuilder<Invoice>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Invoice>())
				.sql("INSERT INTO invoice"
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
						+ ")")
				
				.dataSource(dataSource)
				.build();
				
	}
	
	@Bean
	public SkipPolicy fileVerificationSkipper() {
	    return new ImportInvoiceSkipPolicy();
	}
	
	@Bean
	public Job ImportInvoiceJob(JobCompletionNotificationListener listener, Step uploadFileContentStep) {
		return jobBuilderFactory.get("ImportInvoiceJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.start(backUpStep())
				.next(uploadFileContentStep)
				.build();
				
	}
	
	@Bean
	
	public Step uploadFileContentStep(JdbcBatchItemWriter<Invoice> writer) {
		return stepBuilderFactory.get("uploadFileContentStep")
				.<InvoiceDTO,Invoice>chunk(10)
				.reader(multiResourceItemReader())
				.processor(processor(null, null))
				.writer(writer)
				.faultTolerant()
				.skipPolicy(fileVerificationSkipper())
				.build();
		
	}
	
	
	
	
    @Bean
    public Step backUpStep() {
        FileCopyTasklet task = new FileCopyTasklet();
        task.setExt(backupExt);
        task.setNewPath(backupPath);
        task.setResources(inputResources);
        task.setDateFormat(dateFormat);
        return stepBuilderFactory.get("backUpStep")
                .tasklet(task)
                .build();
    }
    
    
    
}
