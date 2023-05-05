package com.aayush.pds.utility;

import org.springframework.stereotype.Component;

@Component
public class UserDTO {

	
	private String rsaServerPublicKey;
	private String dhServerPublicKey;
	
	public String getRsaServerPublicKey() {
		return rsaServerPublicKey;
	}
	public void setRsaServerPublicKey(String rsa_ServerPublicKey) {
		this.rsaServerPublicKey = rsa_ServerPublicKey;
	}
	public String getDhServerPublicKey() {
		return dhServerPublicKey;
	}
	public void setDhServerPublicKey(String dh_ServerPublicKey) {
		this.dhServerPublicKey = dh_ServerPublicKey;
	}
	
	
}
