package com.batch.config;

import java.net.MalformedURLException;
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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
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

import com.batch.listener.InvoiceFileStepListener;
import com.batch.listener.JobCompletionNotificationListener;
import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;
import com.batch.model.InvoiceExpDTO;
import com.batch.partitioner.CustomMultiResourcePartitioner;
import com.batch.procesor.InvoiceItemExportProcessor;
import com.batch.procesor.InvoiceItemImportProcessor;
import com.batch.reader.InvoiceExpRowMapper;
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
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties firstDataSourceProperties() {
	    return new DataSourceProperties();
	}

	@Bean
	@Primary
	@ConfigurationProperties("app.datasource.first")
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
    
	@Bean
    @StepScope
	 public JdbcCursorItemReader<InvoiceExpDTO> expInvoiceReader(
			 @Value("#{stepExecution.jobExecution.id}") Long jobID
			 ){
	  JdbcCursorItemReader<InvoiceExpDTO> reader = new JdbcCursorItemReader<InvoiceExpDTO>();
	  reader.setDataSource(dataSource());
	  reader.setSql("select " +
	  		"iv.id\r\n" + 
	  		",'FB01' as tx_code\r\n" + 
	  		",'2016' as comp_code\r\n" + 
	  		",COALESCE(vm.vendor_id,'NOT_FOUND') as vendor_id\r\n" + 
	  		",iv.Doc_Date \r\n" + 
	  		",iv.Posting_Date \r\n" + 
	  		",iv.Period \r\n" + 
	  		",'KU' as doc_type\r\n" + 
	  		",iv.currency_code \r\n" + 
	  		",iv.ref_no \r\n" + 
	  		",iv.doc_hdr_txt \r\n" + 
	  		",COALESCE(pkc.post_key,'NOT_FOUND' ) as post_key\r\n" + 
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
	  		" and job_execution_id="+jobID+"\r\n" + 
	  		" ) as iv\r\n" + 
	  		" 	left join vendor_map as vm on iv.party_id = vm.party_id\r\n" + 
	  		"	left join post_key_conf as pkc on \r\n" + 
	  		"	iv.Amnt_local_Type=pkc.Amnt_local_Type \r\n" + 
	  		"	and iv.invoice_type_code=pkc.doc_type_code\r\n" + 
	  		" where 1=1 \r\n" + 
	  		" ORDER BY 1, 2 ;    "
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
                .from(partitionStep()).on("COMPLETED").to(deleteInputFilesStep()).next(stepExportInvoiceStep())
        		.end()
        		.build();
    }
}
