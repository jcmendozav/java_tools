package com.batch.config;

import java.net.MalformedURLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.batch.listener.JobCompletionNotificationListener;
import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;
import com.batch.partitioner.CustomMultiResourcePartitioner;
import com.batch.procesor.InvoiceItemImportProcessor;
import com.batch.skipPolicy.ImportInvoiceSkipPolicy;
import com.batch.tasklet.FileCopyTasklet;
import com.batch.tasklet.uploadFileInfo;
import com.batch.writer.InvoiceItemImportWriter;


@Configuration
@EnableBatchProcessing
public class ImportInvoice {
	
	
	private static final Logger log = LoggerFactory.getLogger(ImportInvoice.class);

	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;
	

	//@Value("file:D:\\Users\\jcmendozav\\Documentos\\Ericsson\\Dev\\Contabilidad\\input\\*.xml")
	@Value("file:///D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/input/*.xml")
	private Resource[] inputResources;

	@Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/backup")
	private String backupPath;
	
	@Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/input")
	private String inputPath;
	
	@Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/output")
	private String outputPath;
	
	@Value(".bu")
	private String backupExt;
	
	@Value("yyyyMMdd_HHmmss")
	private String dateFormat;
	
	@Value("3")
	private int maxPoolSize;

//	@Bean
//	public MultiResourceItemReader<InvoiceDTO> multiResourceItemReader() 
//	{
//	    MultiResourceItemReader<InvoiceDTO> resourceItemReader = new MultiResourceItemReader<InvoiceDTO>();
//	    resourceItemReader.setResources(inputResources);
//	    resourceItemReader.setDelegate(reader());
//	    resourceItemReader.getCurrentResource();
//	    return resourceItemReader;
//	    
//	}
	
    @Bean
    @StepScope
    StaxEventItemReader<InvoiceDTO> reader(
    		@Value("#{stepExecutionContext[fileName]}") String filename
    		)  {
        StaxEventItemReader<InvoiceDTO> xmlFileReader = new StaxEventItemReader<>();
        xmlFileReader.setFragmentRootElementName("Invoice");
        xmlFileReader.setResource(new FileSystemResource(inputPath+"/"+filename));
        Jaxb2Marshaller invoiceMarshaller = new Jaxb2Marshaller();
        invoiceMarshaller.setClassesToBeBound(InvoiceDTO.class);
        xmlFileReader.setUnmarshaller(invoiceMarshaller);

        return xmlFileReader;
    }
    


	@Bean
	@StepScope
	public InvoiceItemImportProcessor processor(
			@Value("#{stepExecutionContext['fileName']}") String fileName
			,@Value("#{stepExecution.jobExecution.id}") Long jobID
			) {
		

		InvoiceItemImportProcessor invoiceItemProcessor = new InvoiceItemImportProcessor();
		invoiceItemProcessor.setResources(inputResources);
		System.out.println("----processor----fileName--->" + fileName);
		System.out.println("----processor----jobID--->" + jobID);
		invoiceItemProcessor.setProcessingFileName(fileName);
		//invoiceItemProcessor.setProcessingJobid(jobID);
		return invoiceItemProcessor;
	}
	
	
	@Bean
    public ItemWriter<Invoice> compositeItemWriter() {
        CompositeItemWriter<Invoice> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList( insertItemWriter(null,null)));
        return compositeItemWriter;
    }
	
	@Bean
    @StepScope
    public InvoiceItemImportWriter insertItemWriter(
			@Value("#{stepExecutionContext['fileName']}") String fileName
			,@Value("#{stepExecution.jobExecution.id}") Long jobID
			) {
		InvoiceItemImportWriter invoiceItemImportWriter = new InvoiceItemImportWriter(dataSource);
		
        return invoiceItemImportWriter;
    }
	
//	@Bean
//    @StepScope
//	public JdbcBatchItemWriter<Invoice> writer(){
//		return new  JdbcBatchItemWriterBuilder<Invoice>()
//				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Invoice>())
//				.sql("INSERT INTO invoice"
//						+ "("
//						+ "custom_serie"
//						+ ",issue_date"
//						+ ",issue_time"
//						+ ",number_serie"
//						+ ",ta"
//						+ ",pa"
//						+ ",lea"
//						+ ",party_id"
//						+ ",tax_code"
//						+ ",invoice_type_code"
//						+ ",doc_date"
//						+ ",job_execution_id"
//						+ ") "
//						+ "values "
//						+ "("
//						+ ":customSerie"
//						+ ",:issueDate"
//						+ ",:issueTime"
//						+ ",:numberSerie"
//						+ ",:taxAmount"
//						+ ",:payableAmount"
//						+ ",:lineExtensionAmount"
//						+ ",:partyId"
//						+ ",:taxCode"
//						+ ",:invoiceTypeCode"
//						+ ",:docDate"
//						+ ",:jobExecutionID"
//						+ ")")
//				
//				.dataSource(dataSource)
//				.build();
//				
//	}
	
	@Bean
	public SkipPolicy fileVerificationSkipper() {
	    return new ImportInvoiceSkipPolicy();
	}
	

	
	@Bean
	
	public Step uploadFileContentStep() {
		
		JdbcBatchItemWriter<Invoice> writer = new JdbcBatchItemWriter<Invoice>();
		return stepBuilderFactory.get("uploadFileContentStep")
				.<InvoiceDTO,Invoice>chunk(2)
				.reader(reader(null))
				.processor(processor(null, null))
				.writer(insertItemWriter(null,null))
				.faultTolerant()
				.skipPolicy(fileVerificationSkipper())
				.allowStartIfComplete(true)
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
    
    @Bean
    public Step fileInfoStep() {
    	uploadFileInfo task = new uploadFileInfo();
        task.setDataSource(dataSource);
        return stepBuilderFactory.get("backUpStep")
                .tasklet(task)
                .build();
    }
    
//	@Bean
//	public Job ImportInvoiceJob(JobCompletionNotificationListener listener, Step uploadFileContentStep) {
//		return jobBuilderFactory.get("ImportInvoiceJob")
//				.incrementer(new RunIdIncrementer())
//				.listener(listener)
//				.start(backUpStep())
//				.next(uploadFileContentStep)
//				.build();
//				
//	}
	
    
    
    @Bean(name = "partitionerJob")
    public Job partitionerJob() throws UnexpectedInputException, MalformedURLException, ParseException {
    	
    	JobCompletionNotificationListener listener = new JobCompletionNotificationListener();
    	listener.setDataSource(dataSource);
        return jobBuilderFactory
        		.get("ImportInvoicePartitionerJob")
        		.incrementer(new RunIdIncrementer())
        		.listener(listener)
        		.start(partitionStep())
        		.build();
    }

    @Bean
    public Step partitionStep() throws UnexpectedInputException, MalformedURLException, ParseException {
        return stepBuilderFactory.get("partitionStep")
          .partitioner("slaveStep", partitioner())
          .step(uploadFileContentStep())
          .taskExecutor(taskExecutor())
          .build();
    }
    
//    @Bean
//    public Step slaveStep() throws UnexpectedInputException, MalformedURLException, ParseException {
//        return stepBuilderFactory.get("slaveStep")
//          .<Transaction, Transaction>chunk(1)
//          .reader(reader())
//          .writer(writer())
//          .build();
//    }
    
    @Bean
    public CustomMultiResourcePartitioner partitioner() {
        CustomMultiResourcePartitioner partitioner = new CustomMultiResourcePartitioner();
        //Resource[] resources;
//        try {
//            //resources = resoursePatternResolver.getResources("file:src/main/resources/input/partitioner/*.csv");
//        } catch (IOException e) {
//            throw new RuntimeException("I/O problems when resolving the input file pattern.", e);
//        }
        partitioner.setResources(inputResources);
        return partitioner;
    }
    
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setCorePoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(maxPoolSize);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
}
