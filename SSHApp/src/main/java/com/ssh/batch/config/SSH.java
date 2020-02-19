package com.ssh.batch.config;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ssh.batch.listener.JobCompletionNotificationListener;
import com.ssh.batch.model.sshParamsInput;
import com.ssh.batch.model.sshResult;
import com.ssh.batch.partitioner.ImportBatchFilePartitioner;
import com.ssh.batch.processor.SshItemProcessor;



@Configuration
@EnableBatchProcessing
public class SSH {
	
	private static final Logger log = LoggerFactory.getLogger(SSH.class);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private BatchAppProperties batchProp;
	
//	@Autowired
//	public void setBatchProp(BatchAppProperties batchProp) {
//		log.info("setBatchProp.batchProp:{}",batchProp);
//		this.batchProp = batchProp;
//	}

	
	public void launchJob() throws IOException, UnexpectedInputException, ParseException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {

		JobParameters param = new JobParametersBuilder()
				.addDate("time", new Date())
				.toJobParameters();
		
		log.info("launchJob.batchProp: {}",batchProp);
			JobExecution execution = jobLauncher.run(partitionerJob(), param);


	}
	
	
	@Bean(name = "partitionerJob")
	public Job partitionerJob() throws UnexpectedInputException, MalformedURLException, ParseException {

		log.info("partitionerJob.batchProp:{}",batchProp);

		JobCompletionNotificationListener listener = new JobCompletionNotificationListener();
		// listener.setDataSource(dataSource);
		return jobBuilderFactory.get("partitionerJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				//.start(backUpStep())
				.start(partitionStep())
				.build();

	}
	
	@Bean
	public Step partitionStep() throws UnexpectedInputException, MalformedURLException, ParseException {
		return stepBuilderFactory
				.get("partitionStep")
				.partitioner("slaveStep", partitioner())
				.step(sshConnectionStep())
		         .taskExecutor(asynctaskExecutor())
//		         .taskExecutor(threadpooltaskExecutor())
				.gridSize(batchProp.gridSize)
				.build();
	}
	
	@Bean
	public ImportBatchFilePartitioner partitioner() {
		ImportBatchFilePartitioner partitioner = new ImportBatchFilePartitioner();
		//partitioner.setResources(inputResources);
		partitioner.setResourcesPath(batchProp.inputPattern);
		return partitioner;
	}
	
	@Bean
	public ThreadPoolTaskExecutor threadpooltaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(batchProp.threadPoolSize);
		//taskExecutor.setCorePoolSize(maxPoolSize);
		//taskExecutor.setQueueCapacity(maxPoolSize);
		taskExecutor.setThreadNamePrefix("SSHIMP-");
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}
	
	@Bean
	public TaskExecutor asynctaskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("SSHIMP-");
		asyncTaskExecutor.setConcurrencyLimit(batchProp.threadPoolSize); // asyncTaskExecutor.setThreadNamePrefix("CSVtoDB");
		return asyncTaskExecutor;
	}
	
//	@Bean
//	public TaskExecutor taskExecutor() {
//		log.info("TaskExecutor.batchProp:{}",batchProp);
//		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		taskExecutor.setMaxPoolSize(batchProp.threadPoolSize);
//		taskExecutor.setCorePoolSize(batchProp.threadPoolSize);
//		taskExecutor.setQueueCapacity(batchProp.threadPoolSize);
//		taskExecutor.setThreadNamePrefix("SSHIMP-");
//		taskExecutor.afterPropertiesSet();
//		return taskExecutor;
//	}

	public Job sshConnectionJob() {
		// TODO Auto-generated method stub
		return jobBuilderFactory
        		.get("sshConnectionJob")
        		.incrementer(new RunIdIncrementer())
        		.start(sshConnectionStep())
        		.build();
	}

	@Bean
	public Step sshConnectionStep() {
		// TODO Auto-generated method stub
		return stepBuilderFactory
				.get("sshConnectionStep")
				.<sshParamsInput,sshResult> chunk(batchProp.chunk)
				.reader(sshParamsInputReader(null))
				.processor(sshParamsInputProcessor())
				.writer(sshResultWriter(null))
				.build()
				;
	}


	@Bean(destroyMethod = "")
	@StepScope
	public FlatFileItemReader<sshParamsInput> sshParamsInputReader(
    		@Value("#{stepExecutionContext[filePath]}") String filePath

			) {
		// TODO Auto-generated method stub
		log.info("filePath: {}",filePath);
		FlatFileItemReader<sshParamsInput> itemReader = new FlatFileItemReader<sshParamsInput>();
		itemReader.setResource(new FileSystemResource(filePath));
		itemReader.setLineMapper(lineMapper());
		itemReader.setLinesToSkip(1);

		return itemReader;
		}
	
	
	@Bean
	public LineMapper<sshParamsInput> lineMapper() {
		DefaultLineMapper<sshParamsInput> lineMapper = new DefaultLineMapper<sshParamsInput>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		String [] names = batchProp.fieldNamesInput.split(Pattern.quote(batchProp.delimiter));
		lineTokenizer.setNames(names);
		lineTokenizer.setDelimiter(batchProp.delimiter);
		//log.info("range from field names: {}",IntStream.range(0, names.length).toArray());
		lineTokenizer.setIncludedFields(IntStream.range(0, names.length).toArray());
		BeanWrapperFieldSetMapper<sshParamsInput> fieldSetMapper = new BeanWrapperFieldSetMapper<sshParamsInput>();
		fieldSetMapper.setTargetType(sshParamsInput.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	
	@Bean(destroyMethod = "")
	@StepScope
	public FlatFileItemWriter<sshResult> sshResultWriter(
			@Value("#{stepExecutionContext[fileName]}") String fileName
			) {
		FlatFileItemWriter<sshResult> writer = new FlatFileItemWriter<sshResult>();
		// TODO Auto-generated method stub
        String timeStamp = new SimpleDateFormat(batchProp.dateFormat).format(new Date());

 
		String [] fieldNames = batchProp.fieldNamesResult.split(Pattern.quote(batchProp.delimiter));

        String outputFilePath=String.format(batchProp.exportFileFormat,fileName,timeStamp );
        log.info("Exporting file: {}, fieldNames: {}",outputFilePath,fieldNames);
        writer.setResource(new FileSystemResource(outputFilePath));
        writer.setAppendAllowed(true);
        writer.setHeaderCallback(new FlatFileHeaderCallback() {
			
			@Override
			public void writeHeader(Writer writer) throws IOException {
				// TODO Auto-generated method stub
				writer.append(batchProp.exportHeader);
			}


		});
        
        writer.setLineAggregator(new DelimitedLineAggregator<sshResult>() {
        	{
        		
    
        		
        
        	
        		
        	setDelimiter(batchProp.delimiter);
        	

        	setFieldExtractor(new BeanWrapperFieldExtractor<sshResult>() {
        		
        		
        		{
        			setNames( fieldNames);
        		}
        			});
        	}}
        		);
        
        return writer;
		
	}

	private ItemProcessor<sshParamsInput, sshResult> sshParamsInputProcessor() {
		SshItemProcessor processor = new SshItemProcessor();
		processor.setNewLineDelimiter(batchProp.newLineDelimiter);
		// TODO Auto-generated method stub
		return processor;
	}

}
