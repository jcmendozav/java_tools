package com.ssh.services;

import com.ssh.batch.model.SshParamsInput;
import com.ssh.batch.model.SshResult;

public interface SshInterface {
	
	public SshResult run(SshParamsInput input);

}
