package com.ssh.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration

public class SshServiceConfig {


	@Bean(name = "jshAdapterTemplate")
	public SshInterface prepareObjectForSSH2() {
		return new JShAdapter(new JshService());
	}
}
