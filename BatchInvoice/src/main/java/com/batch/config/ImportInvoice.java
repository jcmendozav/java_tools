package com.batch.config;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.flow.State;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import com.batch.constant.InvoiceProcess;
import com.batch.listener.InvoiceFileStepListener;
import com.batch.listener.JobCompletionNotificationListener;
import com.batch.listener.UnzipListener;
import com.batch.model.Invoice;
import com.batch.model.InvoiceDTO;
import com.batch.model.InvoiceExpDTO;
import com.batch.model.map.PhoneCcntr;
import com.batch.model.map.PostKeyConf;
import com.batch.model.map.VendorMap;
import com.batch.partitioner.CustomMultiResourcePartitioner;
import com.batch.procesor.InvoiceItemExportProcessor;
import com.batch.procesor.InvoiceItemImportProcessor;
import com.batch.repository.InvoiceExpRowMapper;
import com.batch.repository.InvoiceRowMapper;
import com.batch.repository.PhoneCcntrSQL;
import com.batch.repository.PostKeyConfSQL;
import com.batch.repository.VendorMapSQL;
import com.batch.repository.InvoiceSQL;
import com.batch.service.GenMapUpload;
import com.batch.service.MapUploadUtil;
import com.batch.skipPolicy.ImportInvoiceSkipPolicy;
import com.batch.tasklet.FileCopyTasklet;
import com.batch.tasklet.FileDeletingTasklet;
import com.batch.tasklet.UnzipTasklet;
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
	
	@Autowired
	private JobLauncher jobLauncher;
	
	
	@Autowired
	private InvoiceProperties invoiceP;
		
//	@Autowired
//	private InvoiceProperties.ExportData invoiceP.exportData = invoiceP.exportData;
	
	
//	@Autowired
//	private InvoiceProperties.ImportData invoiceP.importData = invoiceP.exportData;
	
	/*
	 * this bean must be primary due to this issue:
	 * 
	 * Parameter 1 of constructor in org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker required a single bean, but 2 were found:
	- firstDataSourceProperties: defined by method 'firstDataSourceProperties' in class path resource [com/batch/config/ImportInvoice.class]
	- spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties: defined in null
	 * */
	@Bean
	@Primary
	//@ConfigurationProperties("spring.datasource")
	@ConfigurationProperties("inmemory.datasource")
	public DataSourceProperties firstDataSourceProperties() {
	    return new DataSourceProperties();
	}

	/*
	 * Without this primary, we are going to enter in a circular dependency when running from command line
	 * */
	
	@Bean
	@Primary
	//@ConfigurationProperties("app.datasource.first")
	public DataSource dataSource() {
	    return firstDataSourceProperties().initializeDataSourceBuilder().build();
	}
	
	
	/*
	 * job: upload maps 
	 * start
	 * */
	

	
	public Step uploadMap(MapUploadUtil vm) {

//		MapUploadUtil vm = new VendorMapUpload(dataSource(),vendorMapRes,mapDelimiter, backupPath);
		log.info("Uploading map");
		return stepBuilderFactory
				.get("uploadMap")
				.allowStartIfComplete(true)
				.tasklet(new Tasklet() {
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						// TODO Auto-generated method stub
						vm.upload();
						return RepeatStatus.FINISHED;
					}
				}).build();
	}
	
	/*
	 * job: upload maps
	 * end 
	 * */
	
    @Bean
    @StepScope
    StaxEventItemReader<InvoiceDTO> reader(
    		@Value("#{stepExecutionContext[filePath]}") String filePath
    		)  {
    	
    	log.debug("StaxEventItemReader start	: {}",filePath);
        StaxEventItemReader<InvoiceDTO> xmlFileReader = new StaxEventItemReader<>();
        xmlFileReader.setFragmentRootElementName("Invoice");
        xmlFileReader.setResource(new FileSystemResource(filePath));
        Jaxb2Marshaller invoiceMarshaller = new Jaxb2Marshaller();
        invoiceMarshaller.setClassesToBeBound(InvoiceDTO.class);
        xmlFileReader.setUnmarshaller(invoiceMarshaller);
    	log.debug("StaxEventItemReader end		: {}",filePath);
        return xmlFileReader;
    }
    


	@Bean
	@StepScope
	public InvoiceItemImportProcessor processor(
			@Value("#{stepExecutionContext['fileName']}") String fileName
			,@Value("#{stepExecution.jobExecution.id}") Long jobExecutionID
			) {
		

		InvoiceItemImportProcessor invoiceItemProcessor = new InvoiceItemImportProcessor();
//		invoiceItemProcessor.setResources(inputResources);
		invoiceItemProcessor.setProcessingFileName(fileName);
//		invoiceItemProcessor.setMsisdnRegex(msisdnRegex);
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
			,@Value("#{stepExecution.jobExecution.id}") Long jobExecutionID
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
		listener.setBackupPath(invoiceP.importData.getBackupPath());
		return stepBuilderFactory.get("uploadFileContentStep")
				.<InvoiceDTO,Invoice>chunk(invoiceP.importData.getChunk())
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
        task.setNewPath(invoiceP.importData.getBackupPath());
//        task.setResources(inputResources);
        task.setLocationPattern(invoiceP.importData.getFilesToBackupStr());
        task.setDateFormat(invoiceP.getDateFormat());
        return stepBuilderFactory.get("backUpStep")
                .tasklet(task)
                .build();
    }
    
    @Bean
    public Step deleteInputFilesStep() {
        FileDeletingTasklet task = new FileDeletingTasklet();
        task.setLocationPattern( invoiceP.importData.getFilesToDeleteStr());
//        task.setResources(inputResources);
        return stepBuilderFactory.get("deleteInputFilesStep")
                .tasklet(task)
                .build();
    }
    
    
    @Bean
    public Step upzipStep() {
    	
    	log.info("upzipStep - invoiceP: {}",invoiceP);
    	UnzipTasklet task = new UnzipTasklet(
    			invoiceP.importData.getInputPath(), 
    			invoiceP.importData.getZipInputResources()
    			);
    	
    	UnzipListener unzipListener = new UnzipListener();
    	unzipListener.setBackupPath(invoiceP.importData.getBackupPath());
    	unzipListener.setResourcesStr(invoiceP.importData.getZipInputResources());
		return stepBuilderFactory.get("upzipStep")
        		.listener(unzipListener )
                .tasklet(task)
                .build();
    }
    
 

    @Bean
    public Step partitionStep() throws UnexpectedInputException, MalformedURLException, ParseException {
        return stepBuilderFactory.get("partitionStep")
          .partitioner("slaveStep", partitioner())
          
          .step(uploadFileContentStep())
          .taskExecutor(asynctaskExecutor()).taskExecutor(threadpooltaskExecutor())
          .gridSize(invoiceP.importData.getGridSize())
          .build();
    }
    
    @Bean
    public CustomMultiResourcePartitioner partitioner() {
        CustomMultiResourcePartitioner partitioner = new CustomMultiResourcePartitioner( invoiceP.importData.getInputResourcesStr());
        
        //partitioner.setResources(inputResources);
       // partitioner.setResourcesPatter(inputResourcesStr);
        return partitioner;
    }
    
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(invoiceP.importData.getMaxPoolSize() );
        //taskExecutor.setCorePoolSize(maxPoolSize);
        //taskExecutor.setQueueCapacity(maxPoolSize);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
    
	@Bean
	public ThreadPoolTaskExecutor threadpooltaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(invoiceP.importData.getMaxPoolSize());
		//taskExecutor.setCorePoolSize(maxPoolSize);
		//taskExecutor.setQueueCapacity(maxPoolSize);
		taskExecutor.setThreadNamePrefix("INVIMP-");
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}

	@Bean
	public TaskExecutor asynctaskExecutor() {
		log.info("invoiceP:{}",invoiceP.toString());
		log.info("getImportData:{}",invoiceP.getImportData());
		log.info("getZipInputResources: {}",invoiceP.getImportData().getZipInputResources());
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("INVIMP-");
		asyncTaskExecutor.setConcurrencyLimit(invoiceP.importData.getMaxPoolSize()); // asyncTaskExecutor.setThreadNamePrefix("CSVtoDB");
		return asyncTaskExecutor;
	}
    
	@Bean(destroyMethod="")
    @StepScope
	 public JdbcCursorItemReader<InvoiceExpDTO> expInvoiceReader(
			 @Value("#{stepExecution.jobExecution.id}") Long jobExecutionID
			 ){
	  JdbcCursorItemReader<InvoiceExpDTO> reader = new JdbcCursorItemReader<InvoiceExpDTO>();
	  reader.setDataSource(dataSource());
	  reader.setSql(InvoiceSQL.FETCH_REPORT_BY_JOB);
	  reader.setPreparedStatementSetter(new PreparedStatementSetter() {
		
		@Override
		public void setValues(PreparedStatement ps) throws SQLException {
			// TODO Auto-generated method stub
			ps.setInt(1, InvoiceProcess.SUCCESS);
			ps.setLong(2, jobExecutionID);
		}
	});
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
		writer.setDateFormat(invoiceP.getDateFormat());
//		writer.setFileTemplatePath(expor);
//		writer.setOutputPath(outputPath);
//		writer.setRowStart(	rowStart);
		return writer;
	}
	
	
	@Bean
	public Step stepExportInvoiceStep() {
		return stepBuilderFactory
				.get("stepExportInvoiceStep")
				.<InvoiceExpDTO,InvoiceExpDTO> chunk(invoiceP.exportData.getExportChunk())
				.reader(expInvoiceReader(null))
				.processor(expInvoiceProc())
				.writer(expInvoiceXLSWriter())
				.build();
	}
	
	/*
	 * start
	 * stepExportImportResultStep: seems confusing, however this process export the result of the previous
	 * import process.
	 * 
	 * 
	 * */
	
	@Bean
	public Step stepExportImportResultStep() {
		return stepBuilderFactory
				.get("stepExportImportResultStep")
				.<Invoice,Invoice> chunk(invoiceP.exportData.getExportChunk())
				.reader(expImportResultReader(null))
				//.processor(expInvoiceProc())
				.writer(expImportResultWriter(null))
				.build();
	}
	
	
	
	@Bean(destroyMethod="")
    @StepScope
    public JdbcCursorItemReader<Invoice> expImportResultReader(
			 @Value("#{stepExecution.jobExecution.id}") Long jobExecutionID
    		) {
  	  JdbcCursorItemReader<Invoice> reader = new JdbcCursorItemReader<>();
  	  reader.setDataSource(dataSource());
  	  reader.setSql(InvoiceSQL.FETCH_FILES_BY_JOB);
  	  reader.setPreparedStatementSetter(new PreparedStatementSetter() {
		
		@Override
		public void setValues(PreparedStatement ps) throws SQLException {
			// TODO Auto-generated method stub
			ps.setLong(1, jobExecutionID);
		}
	});
  	  reader.setRowMapper(new InvoiceRowMapper());
		return reader;
	}
	
	@Bean
    @StepScope
    public FlatFileItemWriter<Invoice> expImportResultWriter(
			 @Value("#{stepExecution.jobExecution.id}") Long jobExecutionID

			) {
		
		log.info("invoiceP.exportData.getFieldNames: {}",invoiceP.exportData.getFieldNames());
		String[] fieldNames = invoiceP.exportData.getFieldNames().toArray(new String[invoiceP.exportData.getFieldNames().size()]);
		log.info("fieldNames: {}",Arrays.toString(fieldNames));

		FlatFileItemWriter<Invoice> writer = new FlatFileItemWriter<>();
        String timeStamp = new SimpleDateFormat(invoiceP.getDateFormat()).format(new Date());

        String outputFilePath=String.format(invoiceP.exportData.getExportfilePath(),jobExecutionID,timeStamp );
        writer.setResource(new FileSystemResource(outputFilePath));
        writer.setAppendAllowed(true);
        writer.setHeaderCallback(new FlatFileHeaderCallback() {
			
			@Override
			public void writeHeader(Writer writer) throws IOException {
				// TODO Auto-generated method stub
//				writer.append(StringUtils.arrayToDelimitedString(fieldNames, exportDelimiter));
				writer.append(invoiceP.exportData.getFieldNames().toString());
			}
		});
        writer.setLineAggregator(new DelimitedLineAggregator<Invoice>() {
        	{
        	setDelimiter(invoiceP.exportData.getDelimiter());
        	setFieldExtractor(new BeanWrapperFieldExtractor<Invoice>() {
        		
        		{
        			setNames( fieldNames);
        		}
        			});
        	}}
        		);
        
		return writer;
	}

	
	/*
	 * end
	 * stepExportImportResultStep
	 * 
	 * 
	 * */
	
	
	@Bean
    public Job ImportInvoiceJob() throws UnexpectedInputException, MalformedURLException, ParseException {
    	
    	JobCompletionNotificationListener listener = new JobCompletionNotificationListener();
    	listener.setDataSource(dataSource());
    	//JobParameters parameters = (new JobParametersBuilder()).addDate("rundate", new Date()).toJobParameters();
        return jobBuilderFactory
        		.get("ImportInvoiceJob")
        		.listener(listener)
        		.incrementer(new RunIdIncrementer())
        		.start(uploadMap(new GenMapUpload<VendorMap>(dataSource(),VendorMap.class
        				,invoiceP.getMap().getVendor()
        				,invoiceP.getMap().getDelimiter()
        				, invoiceP.importData.getBackupPath(), VendorMapSQL.CREATE, VendorMapSQL.DELETE_ALL))

        				)
        		//.next(uploadMap(new PostKeyConfUpload(dataSource(),postKeyConfMapRes,mapDelimiter, backupPath)))
        		.next(uploadMap(new GenMapUpload<PostKeyConf>(dataSource(),PostKeyConf.class
        				,invoiceP.getMap().getPostKeyConf()
        				,invoiceP.getMap().getDelimiter()
        				, invoiceP.importData.getBackupPath(), PostKeyConfSQL.CREATE, PostKeyConfSQL.DELETE_ALL)))
        		.next(uploadMap(new GenMapUpload<PhoneCcntr>(dataSource(),PhoneCcntr.class
        				,invoiceP.getMap().getPhoneCcnrt()
        				,invoiceP.getMap().getDelimiter()
        				, invoiceP.importData.getBackupPath(), PhoneCcntrSQL.CREATE, PhoneCcntrSQL.DELETE_ALL)))
        		.next(upzipStep())
        		.next(backUpStep())
        		.next(partitionStep())
//        		.on("FAILED").end()
//                .from(partitionStep()).on("COMPLETED")
//                .to(deleteInputFilesStep())
        		.next(deleteInputFilesStep())
                .next(stepExportImportResultStep())
                .next(stepExportInvoiceStep())
//        		.end()
        		.build();
    }
	
	public void launchJob() throws IOException, UnexpectedInputException, ParseException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
//		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();   
//		this.inputResources = patternResolver.getResources(inputResourcesPath);
//		log.info("Resources found: {}",this.inputResources.length);
//		

		JobParameters param = new JobParametersBuilder()
				.addDate("time", new Date())
				.toJobParameters();
			JobExecution execution = jobLauncher.run(ImportInvoiceJob(), param);


	}
}
