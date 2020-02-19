package com.ssh.batch.model;

public class SshParamsInput {

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCommand1() {
		return command1;
	}
	public void setCommand1(String command1) {
		this.command1 = command1;
	}
	
	String host;
    String user;
    String password;
    String command1;
}
