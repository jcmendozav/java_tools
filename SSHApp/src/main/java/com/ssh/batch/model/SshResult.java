package com.ssh.batch.model;

public class SshResult {

	

	public SshResult(SshParamsInput item) {
		// TODO Auto-generated constructor stub
		this.host=item.host;
		this.command1= item.command1;
		this.user=item.user;
	}

	public void setStatus(int status) {
		// TODO Auto-generated method stub
		this.status=status;
	}

	public void setDescription(String description) {
		// TODO Auto-generated method stub
		this.description=description;
	}
	
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getCommand1() {
		return command1;
	}

	public void setCommand1(String command1) {
		this.command1 = command1;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCommandResult() {
		return commandResult.toString().replaceAll("\\n", newLineDelimiter);
	}

	public void setCommandResult(String commandResult) {
		this.commandResult.append(commandResult);
	}

	public int getStatus() {
		return status;
	}

	public String getDescription() {
		return description;
	}
	
	public void setNewLineDelimiter(String newLineDelimiter) {
		this.newLineDelimiter = newLineDelimiter;
	}
	
	public String getNewLineDelimiter() {
		return newLineDelimiter;
	}

	private String host;
	private String command1;
	private String user;
	private int status;
	private String description;
	private StringBuffer commandResult = new StringBuffer();
	private String newLineDelimiter;
	


}
