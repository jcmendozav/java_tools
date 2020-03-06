package com.ssh.services;

import com.ssh.batch.model.SshParamsInput;
import com.ssh.batch.model.SshResult;

public class JShAdapter implements SshInterface {

	JshService service;

	
	public JShAdapter(JshService service) {
		// TODO Auto-generated constructor stub
		this.service=service;
	}
	
	@Override
	public SshResult run(SshParamsInput input) {
		// TODO Auto-generated method stub
		return service.run(input);
	}

}
