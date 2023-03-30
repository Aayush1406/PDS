package com.aayush.pds.controller;

import java.io.IOException;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aayush.pds.model.UserEntity;
import com.aayush.pds.service.RegistrationService;
import com.aayush.pds.utility.ServerKey;

@RestController
public class RegistrationController {

	@Autowired 
	RegistrationService registrationService;
	
	@Autowired
	ServerKey serverKey;
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public UserEntity createUser(@RequestBody UserEntity userEntity) throws IOException {

		BigInteger b = new BigInteger(userEntity.getUserPublicKey(),16); // client public key is in Hex String format
		String newClientPublicKey = b.toString(10);	 // converting into decimal str
		userEntity.setUserPublicKey(newClientPublicKey); // setting into model class
		System.out.println("Inside !!!");
		System.out.println("Public Key = "+userEntity.getUserPublicKey()); // 
		serverKey.generateSharedKey(userEntity); //passing userEntity
		return registrationService.saveUser(userEntity);
		
	}
	
}
