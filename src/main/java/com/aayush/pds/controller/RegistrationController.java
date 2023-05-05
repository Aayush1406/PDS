package com.aayush.pds.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aayush.pds.model.UserEncrypted;
import com.aayush.pds.model.UserEntity;
import com.aayush.pds.service.LoginService;
import com.aayush.pds.service.RegistrationService;
import com.aayush.pds.utility.AES;
import com.aayush.pds.utility.DHKeys;
import com.aayush.pds.utility.RSAKeys;
import com.aayush.pds.utility.UserDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
public class RegistrationController {
	
	private final RegistrationService registrationService;
	private final DHKeys dhKeys;	
	private final RSAKeys rsaKeys;
	private final UserEntity userEntity;
	private final UserDTO userDTO;
	private final AES aes;
	
@Autowired
public RegistrationController(RegistrationService registrationService, DHKeys dhKeys, RSAKeys rsaKeys,UserEntity userEntity, UserEncrypted userEncrypted, UserDTO userDTO, LoginService loginService, AES aes) {
		
		this.registrationService = registrationService;
		this.dhKeys = dhKeys;
		this.rsaKeys = rsaKeys;
		this.userEntity = userEntity; 
		this.userDTO = userDTO;
		this.aes = aes;
	
}

	
@RequestMapping(value="/register", method=RequestMethod.POST)
public ResponseEntity<String> registerUser(@RequestBody UserEncrypted userEncrypted) throws Exception {
		
	try {		
		
			
		BigInteger b = new BigInteger(userEncrypted.getUserPublicKey(),16);  
		String newClientPublicKey = b.toString(10); //Convert user hex	public key to decimal 
		userEntity.setUserPublicKey(newClientPublicKey);		
		String dh_SHASharedKey = dhKeys.generateSharedKey(userEntity); // Generate DH Shared Secret Key !
		System.out.println("SHAServerSharedKey = "+dhKeys.getDhSHAServerSharedKey());
		
		
		// retrieves decrypted user credentials as json string.
		String jsonStrDecryptedUserCredentials = aes.decryptData(dhKeys.getDhSHAServerSharedKey(),userEncrypted.getEncryptedData());  
		JsonObject jsonObject = new Gson().fromJson(jsonStrDecryptedUserCredentials, JsonObject.class);
		
		//Set UserTable fields for userEntity object.
		userEntity.setPassword(jsonObject.get("password").getAsString());   
		userEntity.setUserName(jsonObject.get("username").getAsString());
		userEntity.setRsaUserpublickey(userEncrypted.getRsaUserPublicKey());
		userEntity.setSHASharedKey(dh_SHASharedKey);
		System.out.println("Check 1");

		String rsaDecryptedSignature = rsaKeys.getRSADecryptedData(userEncrypted.getRsaSignature()); //decrypting the Digital Signature. 					
		String SHAOfUserCredentials = dhKeys.generateSHA256(jsonStrDecryptedUserCredentials); // generating SHA256 of decrypted data i.e username and password.
		System.out.println("SHA Hash = "+SHAOfUserCredentials);
		
		
		if(rsaDecryptedSignature.equals(SHAOfUserCredentials)) {          // comparing SHA of Digital Signature with SHA of username and password  
		
			registrationService.saveUser(userEntity); // if equal than store the userEntity to database (usertable).
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Registered Sucessfully !");
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Failed !");
			
			}
		
		}catch(InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException | IllegalArgumentException e) {
			return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error = "+e.getMessage());
		}
		
	}

	
	@RequestMapping(value="/initSecure", method=RequestMethod.GET)
	public UserDTO getServerKeys() throws IOException {
	
		//returns diffie-hellman server public key and RSA server public key
		userDTO.setDhServerPublicKey(dhKeys.getServerPublicKey().toString(16));
		userDTO.setRsaServerPublicKey(rsaKeys.getRsaServerPublicKey());
		return userDTO;
	}

}
