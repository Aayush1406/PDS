package com.aayush.pds.model;

import org.springframework.stereotype.Component;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Component
@Entity
@Table(name = "usertable")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private int userId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "dh_userpublickey")
	private String dhUserPublicKey;

	@Column(name = "rsa_userpublickey")
	private String rsaUserPublicKey;

	@Column(name = "SharedKey")
	private String SHASharedKey;

	public String getRsaUserpublickey() {
		return rsaUserPublicKey;
	}

	public void setRsaUserpublickey(String rsa_userpublickey) {
		this.rsaUserPublicKey = rsa_userpublickey;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

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

	public String getUserPublicKey() {
		return dhUserPublicKey;
	}

	public void setUserPublicKey(String userPublicKey) {
		this.dhUserPublicKey = userPublicKey;
	}

	public String getSHASharedKey() {
		return SHASharedKey;
	}

	public void setSHASharedKey(String sHASharedKey) {
		SHASharedKey = sHASharedKey;
	}

}
