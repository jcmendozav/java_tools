package com.ssh.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.ssh.batch.config.BatchAppProperties;
import com.ssh.batch.model.sshParamsInput;
import com.ssh.batch.model.sshResult;
import com.ssh.services.SshService;


public class SshItemProcessor implements ItemProcessor<sshParamsInput, sshResult> {

	
	
	
	private String newLineDelimiter;
	
	public void setNewLineDelimiter(String newLineDelimiter) {
		this.newLineDelimiter = newLineDelimiter;
	}

	@Override
	public sshResult process(sshParamsInput item) throws Exception {
		// TODO Auto-generated method stub
		
		sshResult result = SshService.run(item);
		result.setNewLineDelimiter(newLineDelimiter);
		return result;
	}

}
