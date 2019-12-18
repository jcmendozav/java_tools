package com.batch.config;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import com.batch.listener.InvoiceFileStepListener;
import com.batch.listener.JobCompletionNotificationListener;
import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;
import com.batch.model.InvoiceExpDTO;
import com.batch.partitioner.CustomMultiResourcePartitioner;
import com.batch.procesor.InvoiceItemExportProcessor;
import com.batch.procesor.InvoiceItemImportProcessor;
import com.batch.reader.InvoiceExpRowMapper;
import com.batch.reader.InvoiceRowMapper;
import com.batch.skipPolicy.ImportInvoiceSkipPolicy;
import com.batch.tasklet.FileCopyTasklet;
import com.batch.tasklet.FileDeletingTasklet;
import com.batch.tasklet.uploadFileInfo;
import com.batch.writer.InvoiceItemExportWriter;
import com.batch.writer.InvoiceItemImportWriter;


@Configuration
@EnableBatchProcessing
public class ImportInvoice {
	
	
	private static final Logger log = LoggerFactory.getLogger(ImportInvoice.class);

	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
//	@Autowired
//	public DataSource dataSource;

	//@Value("file:D:\\Users\\jcmendozav\\Documentos\\Ericsson\\Dev\\Contabilidad\\input\\*.xml")
	@Value("${batch.invoice.import.inputResources}")
	private Resource[] inputResources;

	//@Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/backup")
	@Value("${batch.invoice.import.backupPath}")
	private String backupPath;
	
	@Value("${batch.invoice.import.inputPath}")
	private String inputPath;
	
	@Value("${batch.invoice.import.outputPath}")
	private String outputPath;
	
	@Value(".bu")
	private String backupExt;
	
	@Value("yyyyMMdd_HHmmss")
	private String dateFormat;
	
	@Value("${batch.invoice.import.parallelFile.int}")
	private Integer maxPoolSize;
	
	@Value("${batch.invoice.export.fileTemplate.row.indexStart}")
	private Integer rowStart;

	//@Value("D:/Users/jcmendozav/Documentos/Ericsson/Dev/Contabilidad/template/invoice_exp_template.xlsx")
	@Value("${batch.invoice.export.outputFileTemplatePath}")
	private String fileTemplatePath;
	
	@Value("${batch.invoice.export.fromDaysAgo}")
	private int daysAgo;

	@Value("${batch.invoice.import.chunk}")
	private int importChunk;

	@Value("${batch.invoice.export.chunk}")
	private int exportChunk;
	
	@Bean
	@Primary
	//@ConfigurationProperties("spring.datasource")
	@ConfigurationProperties("inmemory.datasource")
	public DataSourceProperties firstDataSourceProperties() {
	    return new DataSourceProperties();
	}

	@Bean
	@Primary
//@ConfigurationProperties("app.datasource.first")
	public DataSource dataSource() {
	    return firstDataSourceProperties().initializeDataSourceBuilder().build();
	}

	
    @Bean
    @StepScope
    StaxEventItemReader<InvoiceDTO> reader(
    		@Value("#{stepExecutionContext[filePath]}") String filePath
    		)  {
        StaxEventItemReader<InvoiceDTO> xmlFileReader = new StaxEventItemReader<>();
        xmlFileReader.setFragmentRootElementName("Invoice");
        xmlFileReader.setResource(new FileSystemResource(filePath));
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
		InvoiceItemImportWriter invoiceItemImportWriter = new InvoiceItemImportWriter(dataSource());
		
        return invoiceItemImportWriter;
    }
	
	@Bean
	public SkipPolicy fileVerificationSkipper() {
	    return new ImportInvoiceSkipPolicy();
	}
	

	
	@Bean
	
	public Step uploadFileContentStep() {
		
		InvoiceFileStepListener listener = new InvoiceFileStepListener();
		listener.setDataSource(dataSource());
		return stepBuilderFactory.get("uploadFileContentStep")
				.<InvoiceDTO,Invoice>chunk(importChunk)
				.reader(reader(null))
				.processor(processor(null, null))
				.writer(insertItemWriter(null,null))
				.faultTolerant()
				.skipPolicy(fileVerificationSkipper())
				.allowStartIfComplete(true)
				.listener(listener)
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
    public Step deleteInputFilesStep() {
        FileDeletingTasklet task = new FileDeletingTasklet();
        task.setResources(inputResources);
        return stepBuilderFactory.get("deleteInputFilesStep")
                .tasklet(task)
                .build();
    }
    
    @Bean
    public Step fileInfoStep() {
    	uploadFileInfo task = new uploadFileInfo();
        task.setDataSource(dataSource());
        return stepBuilderFactory.get("backUpStep")
                .tasklet(task)
                .build();
    }

    
 

    @Bean
    public Step partitionStep() throws UnexpectedInputException, MalformedURLException, ParseException {
        return stepBuilderFactory.get("partitionStep")
          .partitioner("slaveStep", partitioner())
          .step(uploadFileContentStep())
          .taskExecutor(asynctaskExecutor()).taskExecutor(threadpooltaskExecutor())
          .build();
    }
    
    @Bean
    public CustomMultiResourcePartitioner partitioner() {
        CustomMultiResourcePartitioner partitioner = new CustomMultiResourcePartitioner();
        partitioner.setResources(inputResources);
        return partitioner;
    }
    
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(maxPoolSize);
        //taskExecutor.setCorePoolSize(maxPoolSize);
        //taskExecutor.setQueueCapacity(maxPoolSize);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
    
	@Bean
	public ThreadPoolTaskExecutor threadpooltaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(maxPoolSize);
		//taskExecutor.setCorePoolSize(maxPoolSize);
		//taskExecutor.setQueueCapacity(maxPoolSize);
		taskExecutor.setThreadNamePrefix("INVIMP-");
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}

	@Bean
	public TaskExecutor asynctaskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("INVIMP-");
		asyncTaskExecutor.setConcurrencyLimit(maxPoolSize); // asyncTaskExecutor.setThreadNamePrefix("CSVtoDB");
		return asyncTaskExecutor;
	}
    
	@Bean(destroyMethod="")
    @StepScope
	 public JdbcCursorItemReader<InvoiceExpDTO> expInvoiceReader(
			 @Value("#{stepExecution.jobExecution.id}") Long jobID
			 ){
	  JdbcCursorItemReader<InvoiceExpDTO> reader = new JdbcCursorItemReader<InvoiceExpDTO>();
	  reader.setDataSource(dataSource());
	  reader.setSql("SELECT IV.ID AS ID\r\n" + 
	  		",'FB01' AS TX_CODE\r\n" + 
	  		",'2016' AS COMP_CODE\r\n" + 
	  		",COALESCE(VM.VENDOR_ID,'NOT_FOUND') AS VENDOR_ID\r\n" + 
	  		",IV.ISSUE_DATE as DOC_DATE \r\n" + 
	  		",CURRENT_DATE as POSTING_DATE \r\n" + 
	  		",month(IV.DOC_DATE) as PERIOD \r\n" + 
	  		",'KU' AS DOC_TYPE\r\n" + 
	  		",IV.CURRENCY_CODE \r\n" + 
	  		",IV.NUMBER_SERIE as REF_NO \r\n" + 
	  		",IV.CUSTOM_SERIE as DOC_HDR_TXT \r\n" + 
	  		",COALESCE(PKC.POST_KEY,'NOT_FOUND' ) AS POST_KEY\r\n" + 
	  		",'TBD' as ACCOUNT \r\n" + 
	  		",IV.AMNT_DOC_CURR \r\n" + 
	  		",IV.AMNT_LOCAL_TYPE \r\n" + 
	  		",'ZP07' AS PAYM_TRM \r\n" + 
	  		",IV.ISSUE_DATE AS BASE_DATE\r\n" + 
	  		",'CCntr' AS CCNTR\r\n" + 
	  		",'Assign. No' AS ASSIGN_NO\r\n" + 
	  		",'Itm txt' AS ITM_TXT\r\n" + 
	  		",IV.TAX_CODE \r\n" + 
	  		",FILE_ID\r\n" + 
	  		"FROM (select *,ta as Amnt_Doc_Curr, 'igv' as Amnt_local_Type from invoice where 1=1 and JOB_EXECUTION_ID="+jobID+" \r\n" + 
	  		"union all\r\n" + 
	  		"select *,lea as Amnt_Doc_Curr, 'sub-total' as Amnt_local_Type from invoice where 1=1 and JOB_EXECUTION_ID="+jobID+"  \r\n" + 
	  		"union all\r\n" + 
	  		"select *,pa as Amnt_Doc_Curr, 'total' as Amnt_local_Type from invoice where 1=1 and JOB_EXECUTION_ID="+jobID+" \r\n" + 
	  		" ) AS IV\r\n" + 
	  		" 	LEFT JOIN VENDOR_MAP AS VM ON IV.PARTY_ID = VM.PARTY_ID\r\n" + 
	  		"	LEFT JOIN POST_KEY_CONF AS PKC ON \r\n" + 
	  		"	IV.AMNT_LOCAL_TYPE=PKC.AMNT_LOCAL_TYPE \r\n" + 
	  		"	AND IV.INVOICE_TYPE_CODE=PKC.DOC_TYPE_CODE\r\n" + 
	  		" WHERE 1=1 \r\n" + 
	  		" and JOB_EXECUTION_ID="+jobID+"\r\n" + 
	  		"order by 1,2;" + 
	  		//" WHERE 1=1 \r\n" + 
	  		//" ORDER BY 1, 2 "+
	  		 ";    "
	  		);
	  reader.setRowMapper(new InvoiceExpRowMapper());
	  
	  return reader;
	 }
	
	@Bean
	public InvoiceItemExportProcessor expInvoiceProc() {
		return new InvoiceItemExportProcessor();
	}
	
	
	@Bean(destroyMethod="")
	public InvoiceItemExportWriter expInvoiceXLSWriter(){
		InvoiceItemExportWriter writer = new InvoiceItemExportWriter();
		writer.setDateFormat(dateFormat);
		writer.setFileTemplatePath(fileTemplatePath);
		writer.setOutputPath(outputPath);
		writer.setRowStart(	rowStart);
		return writer;
	}
	
	
	@Bean
	public Step stepExportInvoiceStep() {
		return stepBuilderFactory
				.get("stepExportInvoiceStep")
				.<InvoiceExpDTO,InvoiceExpDTO> chunk(exportChunk)
				.reader(expInvoiceReader(null))
				.processor(expInvoiceProc())
				.writer(expInvoiceXLSWriter())
				.build();
	}
	
	
	@Bean
	public Step stepExportImportResultStep() {
		return stepBuilderFactory
				.get("stepExportImportResultStep")
				.<Invoice,Invoice> chunk(exportChunk)
				.reader(expImportResultReader(null))
				//.processor(expInvoiceProc())
				.writer(expImportResultWriter(null))
				.build();
	}
	
	
	@Bean(destroyMethod="")
    @StepScope
    public JdbcCursorItemReader<Invoice> expImportResultReader(
			 @Value("#{stepExecution.jobExecution.id}") Long jobID
    		) {
  	  JdbcCursorItemReader<Invoice> reader = new JdbcCursorItemReader<>();
  	  reader.setDataSource(dataSource());
  	  reader.setSql(""
  	  		+ "select * from invoice "
  	  		+ "where 1=1 "
  	  		+ "and job_execution_id = "+jobID+" ;"
  	  		+ "");
  	  reader.setRowMapper(new InvoiceRowMapper());
		return reader;
	}
	
	@Bean
    @StepScope
    public FlatFileItemWriter<Invoice> expImportResultWriter(
			 @Value("#{stepExecution.jobExecution.id}") Long jobID

			) {
		
		String [] fieldNames = new String[] {
				"partyId"
				,"vendorId"
				,"customSerie"
				,"numberSerie"
				,"issueTimeStamp"
				,"issueTime"
				,"issueDate"
				,"currencyCode"
				,"lineExtensionAmount"
				,"taxAmount"
				,"payableAmount"
				,"taxCode"
				,"invoiceTypeCode"
				,"docDate"
				,"jobExecutionID"
				,"fileID"
				,"fileName"
				,"filePath"
				,"procStatus"
				,"procDesc"
				,"ID"
				,"invoiceId"
				,"status"
				,"lastUpdatedDate"
				,"creationDate"};
        FlatFileItemWriter<Invoice> writer = new FlatFileItemWriter<>();
        String timeStamp = new SimpleDateFormat(dateFormat).format(new Date());

        String outputFilePath=String.format("%s/importResult_%s_%s.csv",this.outputPath,jobID,timeStamp );
        writer.setResource(new FileSystemResource(outputFilePath));
        writer.setAppendAllowed(true);
        writer.setHeaderCallback(new FlatFileHeaderCallback() {
			
			@Override
			public void writeHeader(Writer writer) throws IOException {
				// TODO Auto-generated method stub
				writer.append(StringUtils.arrayToDelimitedString(fieldNames, ","));
			}
		});
        writer.setLineAggregator(new DelimitedLineAggregator<Invoice>() {
        	{
        	setDelimiter(",");
        	setFieldExtractor(new BeanWrapperFieldExtractor<Invoice>() {
        		
        		{
        			setNames( fieldNames);
        		}
        			});
        	}}
        		);
        
		return writer;
	}

	@Bean
    public Job ImportInvoiceJob() throws UnexpectedInputException, MalformedURLException, ParseException {
    	
    	JobCompletionNotificationListener listener = new JobCompletionNotificationListener();
    	listener.setDataSource(dataSource());
    	//JobParameters parameters = (new JobParametersBuilder()).addDate("rundate", new Date()).toJobParameters();
        return jobBuilderFactory
        		.get("ImportInvoiceJob")
        		.listener(listener)
        		.incrementer(new RunIdIncrementer())
        		.start(backUpStep())
        		.next(partitionStep())
        		.on("FAILED").end()
                .from(partitionStep()).on("COMPLETED")
                .to(deleteInputFilesStep())
                .next(stepExportImportResultStep())
                .next(stepExportInvoiceStep())
        		.end()
        		.build();
    }
}
