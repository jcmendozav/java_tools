package com.batch.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
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
import com.batch.tasklet.FileCopyTasklet;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	
	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);

	
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

	@Bean
	public MultiResourceItemReader<InvoiceDTO> multiResourceItemReader() 
	{
	    MultiResourceItemReader<InvoiceDTO> resourceItemReader = new MultiResourceItemReader<InvoiceDTO>();
	    resourceItemReader.setResources(inputResources);
	    resourceItemReader.setDelegate(reader());
	    return resourceItemReader;
	}
	
	//@StepScope
    @Bean
    StaxEventItemReader<InvoiceDTO> reader()  {
        StaxEventItemReader<InvoiceDTO> xmlFileReader = new StaxEventItemReader<>();
	    //xmlFileReader.setResource(new ClassPathResource("20100017491-01-FNZI-00013980.xml"));
	    //xmlFileReader.setResource(new ClassPathResource("invoice_list.xml"));
        xmlFileReader.setFragmentRootElementName("Invoice");
 
        Jaxb2Marshaller invoiceMarshaller = new Jaxb2Marshaller();
        invoiceMarshaller.setClassesToBeBound(InvoiceDTO.class);
        xmlFileReader.setUnmarshaller(invoiceMarshaller);
//        xmlFileReader.open(new ExecutionContext());
//
//
//			boolean hasNext = true;
//			
//			Invoice trade = null;
//			
//			while (hasNext) {
//			    try {
//					trade = xmlFileReader.read();
//				} catch (UnexpectedInputException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			    if (trade == null) {
//			        hasNext = false;
//			    }
//			    else {
//			        log.info("invoice->"+trade);
//			    }
//			}
 
        return xmlFileReader;
    }
    
    @Bean
    ItemReader<InvoiceDTO> reader2() {
        StaxEventItemReader<InvoiceDTO> xmlFileReader = new StaxEventItemReader<>();
	    //xmlFileReader.setResource(new ClassPathResource("20100017491-01-FNZI-00013980.xml"));
	    xmlFileReader.setResource(new ClassPathResource("invoice_list.xml"));
        xmlFileReader.setFragmentRootElementName("Invoice");
 
        Jaxb2Marshaller invoiceMarshaller = new Jaxb2Marshaller();
        invoiceMarshaller.setClassesToBeBound(InvoiceDTO.class);
        xmlFileReader.setUnmarshaller(invoiceMarshaller);
 
        return xmlFileReader;
    }


	@Bean
	public InvoiceItemProcessor processor() {
		return new InvoiceItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<Invoice> writer(DataSource dataSource){
		return new  JdbcBatchItemWriterBuilder<Invoice>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Invoice>())
				.sql("INSERT INTO invoice(custom_serie,issue_date,issue_time,number_serie,ta,pa,lea) "
						+ "values (:customSerie,:issueDate,:issueTime,:numberSerie,:taxAmount,:payableAmount,:lineExtensionAmount)")
				.dataSource(dataSource)
				.build();
				
	}
	
	@Bean
	public Job ImportInvoiceJob(JobCompletionNotificationListener listener, Step stepUploadFile) {
		return jobBuilderFactory.get("ImportInvoiceJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.start(stepBackUp())
				.next(stepUploadFile)
				.build();
				
	}
	
	@Bean
	
	public Step stepUploadFile(JdbcBatchItemWriter<Invoice> writer) {
		return stepBuilderFactory.get("stepUploadFile")
				.<InvoiceDTO,Invoice>chunk(10)
				.reader(multiResourceItemReader())
				.processor(processor())
				.writer(writer)
				.build();
		
	}
	
    @Bean
    public Step stepBackUp() {
        FileCopyTasklet task = new FileCopyTasklet();
        task.setExt(backupExt);
        task.setNewPath(backupPath);
        task.setResources(inputResources);
        return stepBuilderFactory.get("stepBackUp")
                .tasklet(task)
                .build();
    }
    
    
    
}
