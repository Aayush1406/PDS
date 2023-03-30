package com.aayush.pds.controller;

import java.io.IOException;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aayush.pds.utility.ServerKey;

@RestController
public class KeyController {

	@Autowired
	ServerKey serverKey;
	
	@RequestMapping(value="/initSecure", method = RequestMethod.POST)
	public void initSecure(@RequestParam String clientPublicKey) throws IOException {
		
		BigInteger b = new BigInteger(clientPublicKey,16);
		 String newClientPublicKey = b.toString(10);
		
		String client_Public_Key = newClientPublicKey;
		serverKey.generateSharedKey(client_Public_Key);
		
	}
}
