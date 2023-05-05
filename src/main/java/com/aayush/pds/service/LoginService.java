package com.aayush.pds.service;

import com.aayush.pds.model.UserEntity;


public interface LoginService {
	
	boolean existByUserName(String userName);
	
	UserEntity getUserEntity(int id);
	
	String getSharedSecretKey(int id);
}
