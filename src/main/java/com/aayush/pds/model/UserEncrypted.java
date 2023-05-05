package com.aayush.pds.model;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aayush.pds.utility.DHKeys;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component
public class UserEncrypted {
	
	private static final String ALGORITHM = "AES";
    private static final String SHA_256 = "SHA-256";
	private String encryptedData;
	private String dhUserPublicKey;
	private String rsaUserPublicKey;
	private String rsaSignature;

	@Autowired
	DHKeys dhKeys; 
	
	public String getRsaUserPublicKey() {
		return rsaUserPublicKey;
	}

	public void setRsaUserPublicKey(String rsaUserPublicKey) {
		this.rsaUserPublicKey = rsaUserPublicKey;
	}

	public String getEncryptedData() {
		return encryptedData;
	}
	
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}
	
	public String getUserPublicKey() {
		return dhUserPublicKey;
	}
	
	public void setUserPublicKey(String userPublicKey) {
		this.dhUserPublicKey = userPublicKey;
	}
	
	public String getRsaSignature() {
		return rsaSignature;
	}

	public void setRsaSignature(String rsaSignature) {
		this.rsaSignature = rsaSignature;
	}
	
	/*
	 * public String[] decryptData(String dhSHAServerSharedKey) throws Exception {
	 * byte[] key = hexStringToByteArray(dhSHAServerSharedKey); byte[] ciphertext =
	 * hexStringToByteArray(this.encryptedData); String[] decryptedString =
	 * decrypt(ciphertext, key); return decryptedString; }
	 * 
	 * 
	 * public String[] decrypt(byte[] ciphertext, byte[] key) throws Exception {
	 * 
	 * String[] userData = new String[3]; SecretKeySpec secretKeySpec = new
	 * SecretKeySpec(key, ALGORITHM); Cipher cipher =
	 * Cipher.getInstance("AES/CBC/PKCS5Padding"); byte[] iv = new byte[16];
	 * cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
	 * byte[] plaintext = cipher.doFinal(ciphertext); String decryptedData = new
	 * String(plaintext, StandardCharsets.UTF_8);
	 * System.out.println("Decrypted Message = "+decryptedData);
	 * 
	 * String jsonString = decryptedData; JsonObject jsonObject = new
	 * Gson().fromJson(jsonString, JsonObject.class); String userName =
	 * jsonObject.get("username").getAsString(); String password =
	 * jsonObject.get("password").getAsString();
	 * 
	 * 
	 * System.out.println("Username: " + userName); System.out.println("Password: "
	 * + password);
	 * 
	 * userData[0] = userName; userData[1] = password; userData[2] = decryptedData;
	 * return userData;
	 * 
	 * }
	 * 
	 * public String bytesToHex(byte[] bytes) { StringBuilder sb = new
	 * StringBuilder(); for (byte b : bytes) { sb.append(String.format("%02x", b));
	 * } return sb.toString(); }
	 * 
	 * public byte[] hexStringToByteArray(String hexString) { int length =
	 * hexString.length(); byte[] data = new byte[length / 2]; for (int i = 0; i <
	 * length; i += 2) { data[i / 2] = (byte) ((Character.digit(hexString.charAt(i),
	 * 16) << 4) + Character.digit(hexString.charAt(i+1), 16)); } return data; }
	 */
}
