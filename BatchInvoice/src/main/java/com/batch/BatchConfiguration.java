package com.batch;

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
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.batch.model.InvoiceDTO;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	
	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);

	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;


	//@StepScope
    @Bean
    ItemReader<InvoiceDTO> reader()  {
        StaxEventItemReader<InvoiceDTO> xmlFileReader = new StaxEventItemReader<>();
	    xmlFileReader.setResource(new ClassPathResource("20100017491-01-FNZI-00013980.xml"));
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
	public JdbcBatchItemWriter<InvoiceDTO> writer(DataSource dataSource){
		return new  JdbcBatchItemWriterBuilder<InvoiceDTO>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<InvoiceDTO>())
				.sql("INSERT INTO invoice(invoice_id,issue_date,issue_time) values (:ID,:issueDate,:issueTime)")
				.dataSource(dataSource)
				.build();
				
	}
	
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory.get("ImportUserJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end()
				.build();
				
	}
	
	@Bean
	
	public Step step1(JdbcBatchItemWriter<InvoiceDTO> writer) {
		return stepBuilderFactory.get("step1")
				.<InvoiceDTO,InvoiceDTO>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer)
				.build();
		
	}
}
