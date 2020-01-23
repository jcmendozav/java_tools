package com.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.batch.config.InvoiceProperties;

@SpringBootApplication
@EnableConfigurationProperties(
		InvoiceProperties.class
		)

public class BatchInvoiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchInvoiceApplication.class, args);
	}

}
