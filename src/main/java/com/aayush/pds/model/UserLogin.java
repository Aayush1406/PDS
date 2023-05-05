package com.aayush.pds.model;

import org.springframework.stereotype.Component;

@Component
public class UserLogin {


	private String userName;
	private String password;
	private String rsaSignature;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRsaSignature() {
		return rsaSignature;
	}
	
	public void setRsaSignature(String rsaSignature) {
		this.rsaSignature = rsaSignature;
	}
	
	
}
