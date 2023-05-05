package com.aayush.pds.utility;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class AES {

	private static final String ALGORITHM = "AES";

	public String decryptData(String dhSHAServerSharedKey, String encryptedData) throws Exception {
		System.out.println("Encrypted data = " + encryptedData);
		System.out.println("Shared Key = " + dhSHAServerSharedKey);
		byte[] key = hexStringToByteArray(dhSHAServerSharedKey);
		byte[] ciphertext = hexStringToByteArray(encryptedData);
		String decryptedString = decrypt(ciphertext, key);
		return decryptedString;
	}

	public String encryptData(String dhSHAServerSharedKey, String plainText) throws Exception {

		byte[] key = hexStringToByteArray(dhSHAServerSharedKey);
		String encryptedString = encrypt(plainText, key, dhSHAServerSharedKey);
		return encryptedString;
	}

	public String decrypt(byte[] ciphertext, byte[] key) throws Exception {

		SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] iv = new byte[16];
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
		byte[] plaintext = cipher.doFinal(ciphertext);
		String decryptedData = new String(plaintext, StandardCharsets.UTF_8);
		System.out.println("Decrypted Message = " + decryptedData);
		return decryptedData;

	}

	public static String encrypt(String inputData, byte[] keyBytes, String dhSHAServerSharedKey) throws Exception {

		byte[] ivBytes = new byte[16];

		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] ciphertext = cipher.doFinal(inputData.getBytes(StandardCharsets.UTF_8));

		// bytes to hex
		StringBuilder sb = new StringBuilder();
		for (byte b : ciphertext) {
			sb.append(String.format("%02x", b));
		}
		String ciphertextHex = sb.toString();

		System.out.println(ciphertextHex);
		return ciphertextHex;

	}

	public String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public byte[] hexStringToByteArray(String hexString) {
		int length = hexString.length();
		byte[] data = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return data;
	}

}
