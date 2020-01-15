package com.batch.model.map;

import java.util.regex.Pattern;

public class PhoneCcntr implements GenModelMap {
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCcntr() {
		return ccntr;
	}
	public void setCcntr(String ccntr) {
		this.ccntr = ccntr;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getFullName() {
		return fullName;
	}


	String phone;
	String ccntr;
	String fullName;
	
	@Override
	public void init(String[] fields) {
		// TODO Auto-generated method stub
		phone=fields[0];
		ccntr=fields[1];
		fullName=fields[2];
	}

	@Override
	public void init(String line, String delimiter) {
		// TODO Auto-generated method stub
		String[] fields = line.split(Pattern.quote(delimiter),-1);
		init(fields);
	}

}
