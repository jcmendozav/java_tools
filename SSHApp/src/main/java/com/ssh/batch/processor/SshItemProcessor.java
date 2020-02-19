package com.ssh.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.ssh.batch.config.BatchAppProperties;
import com.ssh.batch.model.SshParamsInput;
import com.ssh.batch.model.SshResult;
import com.ssh.services.SshService;


public class SshItemProcessor implements ItemProcessor<SshParamsInput, SshResult> {

	
	
	
	private String newLineDelimiter;
	private int timeout;
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public void setNewLineDelimiter(String newLineDelimiter) {
		this.newLineDelimiter = newLineDelimiter;
	}

	@Override
	public SshResult process(SshParamsInput item) throws Exception {
		// TODO Auto-generated method stub
		
		SshResult result = SshService.run(item,timeout);
		result.setNewLineDelimiter(newLineDelimiter);
		return result;
	}

}
